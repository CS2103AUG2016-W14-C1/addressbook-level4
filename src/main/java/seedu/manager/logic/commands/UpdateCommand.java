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
            + "Parameters: INDEX (must be a positive integer) [NEW_NAME] [on|by DATE_TIME] [from START_DATE_TIME to END_DATE_TIME]";

    public static final String MESSAGE_UPDATE_ACTIVITY_SUCCESS = "Updated Activity: %1$s";
	public final int targetIndex;
	public String newName;
	public String newDateTime;
	public String newEndDateTime;

	/**
	 * Constructor for required field (targetIndex)
	 */
	public UpdateCommand(int targetIndex) {
	    this.targetIndex = targetIndex;
	}
	
	/**
     * Setters to add in optional fields
     */
	public void setNewName(String name) {
	    this.newName = name;
	}

    public void setNewDateTime(String date) {
        this.newDateTime = date;
    }

    public void setNewEndDateTime(String endDate) {
        this.newEndDateTime = endDate;
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
        String activityName = (newName == null) ? activityToUpdate.getName() : newName;
        return new CommandResult(String.format(MESSAGE_UPDATE_ACTIVITY_SUCCESS, activityName));
    }
}

