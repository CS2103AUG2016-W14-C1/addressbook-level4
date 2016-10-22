package seedu.manager.logic.commands;

import seedu.manager.commons.core.Messages;
import seedu.manager.commons.core.UnmodifiableObservableList;
import seedu.manager.commons.exceptions.IllegalValueException;
import seedu.manager.model.activity.*;

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
	public String newDateTime;
	public String newEndDateTime;

	/**
	 * Constructor for updating name only
	 */
	public UpdateCommand(int targetIndex, String newName) {
		this.targetIndex = targetIndex;
		this.newName = newName;
		this.newDateTime = null;
		this.newEndDateTime = null;
	}
	
	/**
	 * Constructor for updating name and dateTime
	 */
	public UpdateCommand(int targetIndex, String newName, String dateTime) {
		this(targetIndex, newName);
		this.newDateTime = dateTime;
	}
	
	/**
	 * Constructor for updating name, dateTime and endDateTime
	 */
	public UpdateCommand(int targetIndex, String newName, String dateTime, String endDateTime) throws IllegalValueException {
		this(targetIndex, newName);
		this.newDateTime = dateTime;
		this.newEndDateTime = endDateTime;
		// TODO: Handle IllegalValueException in AMParser
		if (new AMDate(this.newDateTime).getTime() > new AMDate(this.newEndDateTime).getTime()) {
            throw new IllegalValueException(Activity.MESSAGE_DATE_CONSTRAINTS);
        }
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
            model.updateActivity(activityToUpdate, newName, newDateTime, newEndDateTime);
        } catch (ActivityList.ActivityNotFoundException anfe) {
            assert false : "The target activity cannot be found";
        }

        return new CommandResult(String.format(MESSAGE_UPDATE_ACTIVITY_SUCCESS, activityToUpdate.getName()));
    }
}

