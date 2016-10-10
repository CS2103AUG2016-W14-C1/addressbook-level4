package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.activity.Activity;
import seedu.address.model.activity.ActivityList.ActivityNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

import java.util.HashSet;
import java.util.Set;

/**
 * Updates an activity in Remindaroo
 */

public class UpdateCommand extends Command {
	public static final String COMMAND_WORD = "update";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Updates the activity identified by the index number used in the last activity listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1" + " assignment1";

    public static final String MESSAGE_UPDATE_ACTIVITY_SUCCESS = "Updated Activity: %1$s";
	public final int targetIndex;
	public final String newName;

	public UpdateCommand(int targetIndex, String newName) {
		this.targetIndex = targetIndex;
		this.newName = newName;
	}
	
	@Override
	public CommandResult execute() {
		UnmodifiableObservableList<Activity> lastShownList = model.getFilteredActivityList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_ACTIVITY_DISPLAYED_INDEX);
        }

        Activity activityToUpdate = lastShownList.get(targetIndex - 1);
        try {
            model.updateActivity(activityToUpdate, newName);
        } catch (ActivityNotFoundException anfe) {
            assert false : "The target activity cannot be found";
        }

        return new CommandResult(String.format(MESSAGE_UPDATE_ACTIVITY_SUCCESS, activityToUpdate.name));
    }
}