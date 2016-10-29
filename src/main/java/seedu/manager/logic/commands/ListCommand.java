package seedu.manager.logic.commands;


/**
 * Lists all activities in the activity manager to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String USAGE = "list";

    public static final String EXAMPLES = "list";

    public static final String MESSAGE_SUCCESS = "Listed all activities";

    public ListCommand() {}

    @Override
    public CommandResult execute() {
        model.updateFilteredListToShowAll();
        model.listCommand();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
