package seedu.manager.logic.commands;

import seedu.manager.commons.core.Messages;
import seedu.manager.commons.core.UnmodifiableObservableList;
import seedu.manager.model.activity.Activity;

/**
 * Updates an activity in Remindaroo
 */

public class UnmarkCommand extends Command {
	public static final String COMMAND_WORD = "unmark";
	
    public static final String USAGE = "unmark ACTIVITY_ID";

    public static final String EXAMPLES = "unmark 1";

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
        
        model.unmarkActivity(activityToUnmark);

        return new CommandResult(String.format(MESSAGE_UNMARK_ACTIVITY_SUCCESS, activityToUnmark.getName()));
    }
}

