package seedu.manager.model;

import java.util.Set;

import seedu.manager.commons.core.UnmodifiableObservableList;
import seedu.manager.commons.exceptions.IllegalValueException;
import seedu.manager.model.activity.Activity;
import seedu.manager.model.activity.AMDate;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyActivityManager newData);

    /** Returns the ActivityManager */
    ReadOnlyActivityManager getActivityManager();

    /** Deletes the given activity. */
    void deleteActivity(Activity target);

    /** Adds the given activity */
    void addActivity(Activity activity, boolean isLastRecurring);
    
    /** Updates the given activity */
    void updateActivity(Activity target, String newName, String newDateTime, String newEndDateTime);

    /** Marks the given activity */
    void markActivity(Activity target);
    
    /** Unmarks the given activity */
    void unmarkActivity(Activity target);
    
    /** Undo up to n commands */
    void undoCommand(int offset);
    
    /** Redo up to n commands */
    void redoCommand(int offset);
    
    /** List all activities */
    void listCommand();
    
    /** Get index of current referenced history */
    int getHistoryIndex();
    
    /** Get the highest index of current referenced history */
    int getMaxHistoryIndex();

    /** Returns the filtered activity list as an {@code UnmodifiableObservableList<Activity>} */
    UnmodifiableObservableList<Activity> getFilteredActivityList();
    
    /** Returns the filtered deadline and event list as an {@code UnmodifiableObservableList<Activity>} */
    UnmodifiableObservableList<Activity> getFilteredDeadlineAndEventList();
    
    /** Returns the filtered floating activity list as an {@code UnmodifiableObservableList<Activity>} */
    UnmodifiableObservableList<Activity> getFilteredFloatingActivityList();

    /** Updates the filter of the filtered activity list to show all activities */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered activity list to filter by the given keywords*/
    void updateFilteredActivityList(Set<String> keywords);
    
    /** Updates the filter of the filtered activity list to filter by the given dateTime range*/
    void updateFilteredActivityList(AMDate dateTime, AMDate endDateTime);
    
    /** Updates the filter of the filtered activity list to filter by the given status*/
    void updateFilteredActivityList(boolean isCompleted);

}
