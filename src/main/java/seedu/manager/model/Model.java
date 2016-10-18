package seedu.manager.model;

import java.util.Set;

import seedu.manager.commons.core.UnmodifiableObservableList;
import seedu.manager.commons.exceptions.IllegalValueException;
import seedu.manager.model.activity.Activity;
import seedu.manager.model.activity.AMDate;
import seedu.manager.model.activity.ActivityList.ActivityNotFoundException;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyActivityManager newData);

    /** Returns the ActivityManager */
    ReadOnlyActivityManager getActivityManager();

    /** Deletes the given activity. */
    void deleteActivity(Activity target) throws ActivityNotFoundException;

    /** Adds the given activity */
    void addActivity(Activity activity);
    
    /** Updates the given activity 
     * @throws IllegalValueException */
    void updateActivity(Activity target, String newName, String newDateTime, String newEndDateTime) throws ActivityNotFoundException, IllegalValueException;

    /** Marks the given activity */
    void markActivity(Activity target, boolean status) throws ActivityNotFoundException;

    /** Returns the filtered activity list as an {@code UnmodifiableObservableList<ReadOnlyPerson>} */
    UnmodifiableObservableList<Activity> getFilteredActivityList();

    /** Updates the filter of the filtered activity list to show all persons */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered activity list to filter by the given keywords*/
    void updateFilteredActivityList(Set<String> keywords);
    
    /** Updates the filter of the filtered activity list to filter by the given dateTime range*/
    void updateFilteredActivityList(AMDate dateTime, AMDate endDateTime);

}
