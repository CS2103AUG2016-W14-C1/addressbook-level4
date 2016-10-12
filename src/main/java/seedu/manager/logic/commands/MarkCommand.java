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

public class MarkCommand extends Command {
	public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the activity identified by the index number used in the last activity listing as pending or completed.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1" + " pending\n"
            + "Example: " + COMMAND_WORD + " 1" + " completed";

    public static final String MESSAGE_MARK_ACTIVITY_SUCCESS = "Marked Activity as %1$s: %2$s";
	public final int targetIndex;
	public final boolean status;
	
	
	public MarkCommand(int targetIndex, boolean status) {
		this.targetIndex = targetIndex;
		this.status = status;
	}
	
	@Override
	public CommandResult execute() {
		UnmodifiableObservableList<seedu.manager.model.activity.Activity> lastShownList = model.getFilteredActivityList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_ACTIVITY_DISPLAYED_INDEX);
        }

        Activity activityToMark = lastShownList.get(targetIndex - 1);
        try {
            model.markActivity(activityToMark, status);
        } catch (ActivityNotFoundException anfe) {
            assert false : "The target activity cannot be found";
        }
        String stringStatus = activityToMark.getStatus();
        return new CommandResult(String.format(MESSAGE_MARK_ACTIVITY_SUCCESS, stringStatus, activityToMark.name));
    }
}

