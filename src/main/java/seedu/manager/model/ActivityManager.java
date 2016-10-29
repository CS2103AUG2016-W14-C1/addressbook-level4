package seedu.manager.model;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.manager.model.activity.*;

import java.util.*;

/**
 * Wraps all data at the activity-manager level
 * Duplicates are not allowed (by .equals comparison)
 */
//@@author A0144881Y
public class ActivityManager implements ReadOnlyActivityManager {

    private final ActivityList activities;
    
    {
        activities = new ActivityList();
    }

    public ActivityManager() {}

    /**
     * Activities and Tags are copied into this activity manager
     */
    public ActivityManager(ReadOnlyActivityManager toBeCopied) {
        this(toBeCopied.getActivityList());
    }

    /**
     * Activities and Tags are copied into this activity manager
     */
    public ActivityManager(ActivityList activities) {
        resetData(activities.getInternalList());
    }

    public static ReadOnlyActivityManager getEmptyActivityManager() {
        return new ActivityManager();
    }

//// list overwrite operations

    public ObservableList<Activity> getActivities() {
        return activities.getInternalList();
    }

    public void setActivties(List<Activity> activities) {
        this.activities.getInternalList().setAll(activities);
    }

    public void resetData(Collection<? extends Activity> newActivities) {
        setActivties(new ArrayList<Activity>(newActivities));
    }

    public void resetData(ReadOnlyActivityManager newData) {
        resetData(newData.getActivityList().getInternalList());
    }

//// activity-level operations

    /**
     * Adds an activity to the activity manager.
     * Also checks the new acitivity's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the activity to point to those in {@link #tags}.
     *
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void removeActivity(Activity key) {
        activities.remove(key);
    }

    public void updateActivity(Activity key, String newName, String newDateTime, String newEndDateTime) {
    	activities.update(key, newName, newDateTime, newEndDateTime);
    }

    public void markActivity(Activity key) {
    	activities.mark(key);
    }
    
    public void unmarkActivity(Activity key) {
    	activities.unmark(key);
    }
    
    public void listActivities() {
    	activities.list();
    }

//// util methods

    @Override
    public String toString() {
        return activities.getInternalList().size() + " activities";
        // TODO: refine later
    }

    @Override
    public List<Activity> getListActivity() {
        return Collections.unmodifiableList(activities.getInternalList());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ActivityManager // instanceof handles nulls
                && this.toString().equals(other.toString()));
                // TODO: check if activities are actually equal
                /* this.activities.equals(((ActivityManager) other).getActivities()); */       
                
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(activities);
    }

	@Override
	public ActivityList getActivityList() {
		return activities;
	}
	
	public FilteredList<Activity> getPendingActivityList() {
		return activities.getPendingInternalList();
	}
}
