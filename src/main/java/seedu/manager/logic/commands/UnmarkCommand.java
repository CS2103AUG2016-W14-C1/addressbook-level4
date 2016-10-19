package seedu.manager.logic.commands;

import seedu.manager.commons.core.Messages;
import seedu.manager.commons.core.UnmodifiableObservableList;
import seedu.manager.commons.exceptions.IllegalValueException;
import seedu.manager.model.activity.Activity;
import seedu.manager.model.activity.ActivityList.ActivityNotFoundException;
import seedu.manager.model.activity.Status;
import seedu.manager.model.tag.Tag;
import seedu.manager.model.tag.UniqueTagList;

import java.util.HashSet;
import java.util.Set;

/**
 * Updates an activity in Remindaroo
 */

public class UnmarkCommand extends Command {
	public static final String COMMAND_WORD = "unmark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the activity identified by the index number used in the last activity listing as pending.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNMARK_ACTIVITY_SUCCESS = "Pending Activity: %1$s";
	public final int targetIndex;
	
	
	public UnmarkCommand(int targetIndex) {
		this.targetIndex = targetIndex;
	}
	
	@Override
	public CommandResult execute() {
		UnmodifiableObservableList<seedu.manager.model.activity.Activity> lastShownList = model.getFilteredActivityList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_ACTIVITY_DISPLAYED_INDEX);
        }

        Activity activityToUnmark = lastShownList.get(targetIndex - 1);
        try {
            model.unmarkActivity(activityToUnmark);
        } catch (ActivityNotFoundException anfe) {
            assert false : "The target activity cannot be found";
        }
        String stringStatus = activityToUnmark.getStatus().toString();
        return new CommandResult(String.format(MESSAGE_UNMARK_ACTIVITY_SUCCESS, activityToUnmark.getName()));
    }
}

