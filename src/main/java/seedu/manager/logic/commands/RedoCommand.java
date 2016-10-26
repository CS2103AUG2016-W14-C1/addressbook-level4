package seedu.manager.logic.commands;

/**
 * Redo a previous undo command.
 */
//@@author A0144881Y
public class RedoCommand extends Command {
	
    public static final String COMMAND_WORD = "redo";

    public static final String USAGE = "redo\n" + "redo NUMBER_OF_TIMES";

    public static final String EXAMPLES = "redo\n" + "redo 2";
    
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Can only be used right after undo commands, to revert the to-do list to the state before undo.\n"
              + "\nUsage:\nredo [NUMBER_OF_COMMANDS]\n";

    public static final String MESSAGE_SUCCESS = "Reverted to later state. (%d commands redone)";
    
    public static final String MESSAGE_INDEX_LARGER_THAN_MAX = "Nothing left to redo. You are at the latest state.";
    
    public static final String MESSAGE_OFFSET_OUT_OF_BOUNDS = "Insufficient number of commands to perform redo operation. You can redo maximum %d times.";
    
    private int offset = 0;
	
    
    public RedoCommand() {
        offset = 1;
    }
    
    public RedoCommand(int newOffset) {
        offset = newOffset;
    }
    
    @Override
    public CommandResult execute() {
        assert model != null;
        if (model.getHistoryIndex() >= model.getMaxHistoryIndex()) {
            return new CommandResult(MESSAGE_INDEX_LARGER_THAN_MAX);
        } else if (model.getHistoryIndex() + offset > model.getMaxHistoryIndex()) {
            return new CommandResult(String.format(MESSAGE_OFFSET_OUT_OF_BOUNDS, model.getMaxHistoryIndex() - model.getHistoryIndex()));
        } else {
            model.redoCommand(offset);
            return new CommandResult(String.format(MESSAGE_SUCCESS, offset));
        }
    }

}
