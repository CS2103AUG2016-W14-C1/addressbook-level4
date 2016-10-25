package seedu.manager.model;

import javafx.collections.transformation.FilteredList;
import seedu.manager.commons.core.ComponentManager;
import seedu.manager.commons.core.LogsCenter;
import seedu.manager.commons.core.UnmodifiableObservableList;
import seedu.manager.commons.events.model.ActivityManagerChangedEvent;
import seedu.manager.commons.events.ui.ActivityPanelUpdateEvent;
import seedu.manager.commons.events.ui.ActivityListPanelUpdateEvent;
import seedu.manager.commons.exceptions.IllegalValueException;
import seedu.manager.commons.util.StringUtil;
import seedu.manager.model.activity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private ActivityManager activityManager;
    private FilteredList<Activity> filteredActivities;
    
    ArrayList<ActivityManager> managerHistory = new ArrayList<ActivityManager>();
    int historyIndex = -1;
    
    /**
     * Initializes a ModelManager with the given ActivityManager
     * ActivityManager and its variables should not be null
     */
    public ModelManager(ActivityManager src, UserPrefs userPrefs) {
        super();
        assert src != null;
        assert userPrefs != null;

        logger.fine("Initializing with activity manager: " + src + " and user prefs " + userPrefs);

        activityManager = new ActivityManager(src);
        filteredActivities = new FilteredList<>(activityManager.getActivities());
        // recordManagerHistory(activityManager);
    }

    public ModelManager() {
        this(new ActivityManager(), new UserPrefs());
    }

    public ModelManager(ReadOnlyActivityManager initialData, UserPrefs userPrefs) {
        activityManager = new ActivityManager(initialData);
        filteredActivities = new FilteredList<>(activityManager.getActivities());
        updateFilteredActivityList();
        recordManagerHistory(activityManager);
    }

    @Override
    public void resetData(ReadOnlyActivityManager newData) {
        activityManager.resetData(newData);
        recordManagerHistory(activityManager);
        indicateActivityManagerChanged();
    }

    @Override
    public ReadOnlyActivityManager getActivityManager() {
        return activityManager;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateActivityManagerChanged() {
        raise(new ActivityManagerChangedEvent(activityManager));
        
    }
    
    /** Raises an event to indicate that the list of activities need to be updated */
    private void indicateActivityPanelUpdate(Activity updatedActivity) {
        raise(new ActivityPanelUpdateEvent(updatedActivity));
    }
    
    private void indicateActivityListPanelUpdate(){
    	raise(new ActivityListPanelUpdateEvent(getFilteredFloatingActivityList(), getFilteredDeadlineAndEventList()));
    }

    @Override
    public synchronized void deleteActivity(Activity target) {
        activityManager.removeActivity(target);
        updateFilteredListToShowAll();
        indicateActivityListPanelUpdate();
        indicateActivityManagerChanged();
        recordManagerHistory(activityManager);
    }

    @Override
    public synchronized void addActivity(Activity activity, boolean isLastRecurring) {
        activityManager.addActivity(activity);
        updateFilteredListToShowAll();
        indicateActivityListPanelUpdate();
        indicateActivityManagerChanged();
        // Record state only for the last addition (esp. for recurring tasks)
        if (isLastRecurring) {
            recordManagerHistory(activityManager);
        }
    }
    
    @Override
    public synchronized void updateActivity(Activity activity, String newName, String newDateTime, String newEndDateTime) {
        activityManager.updateActivity(activity, newName, newDateTime, newEndDateTime);
        updateFilteredListToShowAll();
        //indicateActivityPanelUpdate(activity);
        indicateActivityManagerChanged();
        indicateActivityListPanelUpdate();
        recordManagerHistory(activityManager);
    }

    @Override
    public synchronized void markActivity(Activity activity) {
        activityManager.markActivity(activity);
        updateFilteredActivityList();
        indicateActivityManagerChanged();
        recordManagerHistory(activityManager);
    }

    @Override
    public synchronized void unmarkActivity(Activity activity) {
        activityManager.unmarkActivity(activity);
        updateFilteredActivityList();
        indicateActivityManagerChanged();
        recordManagerHistory(activityManager);
    }
    
    private void recordManagerHistory(ActivityManager am) {
        // Overwrite alternate history
        while (managerHistory.size() - 1 > historyIndex) {
            managerHistory.remove(managerHistory.size() - 1);
        }
        ActivityManager savedAM = new ActivityManager();
        for (Activity activity : am.getActivities()) {
            savedAM.addActivity(new Activity(activity));   
        }
        managerHistory.add(savedAM);
        historyIndex++;
    }
    
    public int getHistoryIndex() {
        return historyIndex;
    }
    
    @Override
    public synchronized void undoCommand(int offset) {
        historyIndex -= offset;
        activityManager = managerHistory.get(historyIndex);
        filteredActivities = new FilteredList<>(activityManager.getActivities());
        for (Activity act : filteredActivities) {
            System.out.println(act.getName());
        }
        updateFilteredListToShowAll();
        indicateActivityListPanelUpdate();
        indicateActivityManagerChanged();
    }

    //=========== Filtered Activity List Accessors ===============================================================

    @Override
    public UnmodifiableObservableList<Activity> getFilteredActivityList() {
        return new UnmodifiableObservableList<>(filteredActivities);
    }
    
    @Override
    public UnmodifiableObservableList<Activity> getFilteredDeadlineAndEventList() {
    	FilteredList<Activity> deadlineAndEventList = filteredActivities.filtered(new Predicate<Activity>() {
    		public boolean test(Activity activity) {
    			return activity.getType() != ActivityType.FLOATING;
    		}
		});
    	return new UnmodifiableObservableList<>(deadlineAndEventList);
    }
    
    @Override
    public UnmodifiableObservableList<Activity> getFilteredFloatingActivityList() {
    	FilteredList<Activity> deadlineAndEventList = filteredActivities.filtered(new Predicate<Activity>() {
    		public boolean test(Activity activity) {
    			return activity.getType() == ActivityType.FLOATING;
    		}
		});
    	return new UnmodifiableObservableList<>(deadlineAndEventList);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredActivities.setPredicate(null);
    }

    @Override
    public void updateFilteredActivityList(Set<String> keywords){
        updateFilteredActivityList(new PredicateExpression(new NameQualifier(keywords)));
    }
    
    public void updateFilteredActivityList(AMDate dateTime, AMDate endDateTime){
        updateFilteredActivityList(new PredicateExpression(new DateQualifier(dateTime, endDateTime)));
    }
    

    private void updateFilteredActivityList(Expression expression) {
        filteredActivities.setPredicate(expression::satisfies);
    }
    
    private void updateFilteredActivityList(Predicate<Activity> predicate) {
        filteredActivities.setPredicate(predicate);
    }
    
    public void updateFilteredActivityList() {
    	updateFilteredActivityList(new Predicate<Activity>() {
    		public boolean test(Activity activity) {
    			return !activity.getStatus().isCompleted();
    		}
    	});
    }
    
    public void updateFilteredActivityList(boolean isCompleted) {
    	updateFilteredActivityList(new Predicate<Activity>() {
    		public boolean test(Activity activity) {
    			if (isCompleted) {
    				return activity.getStatus().isCompleted();
    			} else {
    				return !activity.getStatus().isCompleted();
    			}
    		}
    	});
    }

    //========== Inner classes/interfaces used for filtering ==================================================

    interface Expression {
        boolean satisfies(Activity activity);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(Activity activity) {
            return qualifier.run(activity);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(Activity activity);
        String toString();
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(Activity activity) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(activity.getName(), keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }
    
    private class DateQualifier implements Qualifier {
        private AMDate dateTime;
        private AMDate endDateTime;

        DateQualifier(AMDate dateTime, AMDate endDateTime) {
            this.dateTime = dateTime;
            this.endDateTime = endDateTime;
        }

        @Override
        public boolean run(Activity activity) {
            if (activity.getType().equals(ActivityType.FLOATING)) {
                // no need check dateTime for floating activity, but should not return either
                return false;
            } else if (activity.getType().equals(ActivityType.DEADLINE)) {
                // return true if deadline falls within dateTime range 
                Long deadlineTime = activity.getDateTime().getTime(); 
                return deadlineTime >= dateTime.getTime() && deadlineTime <= endDateTime.getTime(); 
            } else if (activity.getType().equals(ActivityType.EVENT)) {
                // return true if either start or end of event falls within dateTime range
                Long eventTime = activity.getDateTime().getTime();
                Long endEventTime = activity.getEndDateTime().getTime();
                return !(endEventTime < dateTime.getTime() || eventTime > endDateTime.getTime());
            } else {
                return false; // should not happen
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("dateTime=");
            sb.append(dateTime.toString());
            sb.append("\nendDateTime=");
            sb.append(endDateTime.toString());
            return sb.toString();
        }
    }

}
