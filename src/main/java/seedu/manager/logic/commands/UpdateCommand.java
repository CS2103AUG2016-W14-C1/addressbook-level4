package seedu.manager.logic.commands;

import seedu.manager.commons.core.Messages;
import seedu.manager.commons.core.UnmodifiableObservableList;
import seedu.manager.model.activity.*;

/**
 * Updates an activity in Remindaroo
 */
//@@author A0144881Y
public class UpdateCommand extends Command {
	public static final String COMMAND_WORD = "update";

	public static final String USAGE = "update ACTIVITY_ID NEW_NAME\n" +
	                                   "update ACTIVITY_ID NEW_NAME on DATE_TIME\n" +
	                                   "update ACTIVITY_ID NEW_NAME from DATE_TIME to DATE_TIME\n";

	public static final String EXAMPLES = "update 1 Walk the dog\n" +
                                          "update 2 New homework deadline on 17 Nov\n" +
                                          "update 3 Postponed talk from 2 Nov 3pm to 2 Nov 5pm\n";

	
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
	public UpdateCommand(int targetIndex, String newName, String dateTime, String endDateTime) {
		this(targetIndex, newName);
		this.newDateTime = dateTime;
		this.newEndDateTime = endDateTime;
	}
	
	@Override
	public CommandResult execute() {
		UnmodifiableObservableList<Activity> lastShownList = model.getFilteredActivityList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_ACTIVITY_DISPLAYED_INDEX);
        }

        Activity activityToUpdate = lastShownList.get(targetIndex - 1);
        model.updateActivity(activityToUpdate, newName, newDateTime, newEndDateTime);

        return new CommandResult(String.format(MESSAGE_UPDATE_ACTIVITY_SUCCESS, activityToUpdate.getName()));
    }
}

