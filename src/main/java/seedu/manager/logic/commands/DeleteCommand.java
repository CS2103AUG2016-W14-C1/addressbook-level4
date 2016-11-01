package seedu.manager.logic.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import seedu.manager.commons.core.Messages;
import seedu.manager.commons.core.UnmodifiableObservableList;
import seedu.manager.model.activity.Activity;

/**
 * Deletes a activity identified using it's last displayed index from the activity manager.
 */
//@@author A0144881Y
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";
    
    public static final String USAGE = "delete INDEX";
    
    public static final String EXAMPLES = "delete 1";
    
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the activity identified by the index number used in the last activity listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_ACTIVITY_SUCCESS = "Deleted Activity / Activities:%1$s";
    
    public static final String ACTIVITY_SEPERATOR = " ";

    public final ArrayList<Integer> targetIndexes;

    public DeleteCommand(ArrayList<Integer> targetIndexes) {
        this.targetIndexes = targetIndexes;
    }


    @Override
    public CommandResult execute() {
        
        UnmodifiableObservableList<Activity> lastShownList = model.getFilteredActivityList();
        Collections.sort(targetIndexes);
        Collections.reverse(targetIndexes);
        
        StringBuilder activitiesDeleted = new StringBuilder();
        for (int i = 0; i < targetIndexes.size(); i++) {
            int targetIndex = targetIndexes.get(i);
            if (lastShownList.size() < targetIndex) {
                indicateAttemptToExecuteIncorrectCommand();
                return new CommandResult(Messages.MESSAGE_INVALID_ACTIVITY_DISPLAYED_INDEX);
            }

            Activity activityToDelete = lastShownList.get(targetIndex - 1); 
            activitiesDeleted.append(ACTIVITY_SEPERATOR + activityToDelete.getName());
            model.deleteActivity(activityToDelete, i == targetIndexes.size() - 1);
        }
        return new CommandResult(String.format(MESSAGE_DELETE_ACTIVITY_SUCCESS, activitiesDeleted.toString()));
    }

}
