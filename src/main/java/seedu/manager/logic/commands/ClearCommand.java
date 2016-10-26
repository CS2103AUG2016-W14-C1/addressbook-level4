package seedu.manager.logic.commands;

import seedu.manager.model.ActivityManager;

/**
 * Clears the address book.
 */
//@@author A0139797E
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String USAGE = "clear";
    public static final String EXAMPLES = "clear";
    public static final String MESSAGE_SUCCESS = "Your activities have been removed!";

    public ClearCommand() {}


    @Override
    public CommandResult execute() {
        assert model != null;
        model.resetData(ActivityManager.getEmptyActivityManager());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
