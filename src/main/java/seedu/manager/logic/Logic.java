package seedu.manager.logic;

import javafx.collections.ObservableList;
import seedu.manager.logic.commands.CommandResult;
import seedu.manager.model.activity.Activity;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     */
    CommandResult execute(String commandText);

    /** Returns the filtered list of activities */
    ObservableList<Activity> getFilteredActivitiesList();
    
    /** Returns the filtered list of deadlines and events */
    ObservableList<Activity> getFilteredDeadlineAndEventList();
    
    /** Returns the filtered list of floating activities */
    ObservableList<Activity> getFilteredFloatingActivityList();


}
