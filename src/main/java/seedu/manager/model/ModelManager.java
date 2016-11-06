package seedu.manager.model;

import javafx.collections.transformation.FilteredList;
import seedu.manager.commons.core.ComponentManager;
import seedu.manager.commons.core.LogsCenter;
import seedu.manager.commons.core.UnmodifiableObservableList;
import seedu.manager.commons.events.model.ActivityManagerChangedEvent;
import seedu.manager.commons.events.ui.ActivityListPanelUpdateEvent;
import seedu.manager.commons.util.StringUtil;
import seedu.manager.model.activity.*;

import java.util.ArrayList;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

/**
 * Represents the in-memory model of the activity manager data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private ActivityManager activityManager;
    private FilteredList<Activity> filteredActivities;
    
    private ArrayList<ActivityManager> managerHistory = new ArrayList<ActivityManager>();
    private int historyIndex = -1;
    
    /**
     * Initializes a ModelManager with the given ActivityManager
     * ActivityManager and its variables should not be null
     */
    public ModelManager(ReadOnlyActivityManager initialData) {
        logger.fine("Initializing with activity manager: " + initialData);
        activityManager = new ActivityManager(initialData);
        filteredActivities = new FilteredList<>(activityManager.getActivities());
        updateFilteredActivityList(false);
        recordManagerHistory(activityManager);
    }
    
    public ModelManager() {
        this(new ActivityManager());
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
    
    //@@author A0139797E
    public void indicateActivityListPanelUpdate(){
        raise(new ActivityListPanelUpdateEvent(-1));
    }

    
    public void indicateActivityListPanelUpdate(Activity newActivity){
    	// Find index of new/updated activity and set it as our target to scroll to
    	int index = -1;
        UnmodifiableObservableList<Activity> activities = getFilteredActivityList();
    	for (int i = activities.size() - 1; i >= 0; i--) {
    	    Activity activity = activities.get(i);
    	    if (activity.equals(newActivity)) {
    	        index = i;
    	        activity.setSelected(true);
    	        break;
    	    }
    	}
    	for (int i = 0; i < activities.size(); i++) {
    	    if (index == i) continue;
    	    activities.get(i).setSelected(false);
    	}
    	raise(new ActivityListPanelUpdateEvent(index));
    }

    @Override
    public synchronized void addActivity(Activity activity, boolean isLastRecurring) {
        activityManager.addActivity(activity);
        indicateActivityListPanelUpdate(activity);
        indicateActivityManagerChanged();
        // Record state only for the last addition (esp. for recurring tasks)
        if (isLastRecurring) {
            recordManagerHistory(activityManager);
        }
    }
    
    //@@author A0144881Y
    @Override
    public synchronized void deleteActivity(Activity target, boolean isLastActivity) {
        activityManager.removeActivity(target);
        indicateActivityListPanelUpdate();
        indicateActivityManagerChanged();
        if (isLastActivity) {
            recordManagerHistory(activityManager);
        }
    }
    
    @Override
    public synchronized void updateActivity(Activity activity, String newName, String newDateTime, String newEndDateTime) {
        activityManager.updateActivity(activity, newName, newDateTime, newEndDateTime);
        indicateActivityManagerChanged();
        indicateActivityListPanelUpdate(activity);
        recordManagerHistory(activityManager);
    }

    //@@author A0144704L
    @Override
    public synchronized String markActivity(Activity activity) {
        activityManager.markActivity(activity);
        updateFilteredActivityList(false);
        indicateActivityManagerChanged();
        indicateActivityListPanelUpdate();
        recordManagerHistory(activityManager);
        return activity.getName();
    }

    @Override
    public synchronized String unmarkActivity(Activity activity) {
        activityManager.unmarkActivity(activity);
        indicateActivityManagerChanged();
        indicateActivityListPanelUpdate(activity);
        recordManagerHistory(activityManager);
        return activity.getName();
    }
    
    //@@author A0139797E
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
    
    //@@author A0144881Y
    public int getMaxHistoryIndex() {
    	return managerHistory.size() - 1;
    }
    
    //@@author A0139797E
    @Override
    public synchronized void undoCommand(int offset) {
        historyIndex -= offset;
        activityManager = new ActivityManager(managerHistory.get(historyIndex));
        filteredActivities = new FilteredList<>(activityManager.getActivities(), filteredActivities.getPredicate());
        indicateActivityListPanelUpdate();
        indicateActivityManagerChanged();
    }
    
    //@@author A0144881Y
    @Override
    public synchronized void redoCommand(int offset) {
        historyIndex += offset;
        activityManager = new ActivityManager(managerHistory.get(historyIndex));
        filteredActivities = new FilteredList<>(activityManager.getActivities(), filteredActivities.getPredicate());
        indicateActivityListPanelUpdate();
        indicateActivityManagerChanged();
    }
    
    //@@author A0144704L
    @Override
    public void listCommand() {
    	activityManager.listActivities();
    	indicateActivityListPanelUpdate();
    }

    //=========== Filtered Activity List Accessors ===============================================================

    @Override
    public UnmodifiableObservableList<Activity> getFilteredActivityList() {
        return new UnmodifiableObservableList<>(filteredActivities);
    }
    
    @Override
    //@@author A0144881Y
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
    
    //@@author A0135730M
    public void updateFilteredActivityList(AMDate dateTime, AMDate endDateTime){
        updateFilteredActivityList(new PredicateExpression(new DateQualifier(dateTime, endDateTime)));
    }
    

    private void updateFilteredActivityList(Expression expression) {
        filteredActivities.setPredicate(expression::satisfies);
    }
    
    private void updateFilteredActivityList(Predicate<Activity> predicate) {
        filteredActivities.setPredicate(predicate);
    }
    
    //@@author A0144704L
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

    }
    
    //@@author A0135730M
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

    }

}
