package seedu.address.model;

import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.model.activity.Activity;
import seedu.address.model.activity.ActivityList.ActivityNotFoundException;
import seedu.address.model.activity.Status;
import seedu.address.model.activity.UniqueActivityList.DuplicateActivityException;

import java.util.Set;

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
    
    /** Updates the given activity */
    void updateActivity(Activity target, String newName) throws ActivityNotFoundException;
    
    /** Marks the given activity */
    void markActivity(Activity target, boolean status) throws ActivityNotFoundException;

    /** Returns the filtered person list as an {@code UnmodifiableObservableList<ReadOnlyPerson>} */
    UnmodifiableObservableList<Activity> getFilteredActivityList();

    /** Updates the filter of the filtered person list to show all persons */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered person list to filter by the given keywords*/
    void updateFilteredActivityList(Set<String> keywords);

}
