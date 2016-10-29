package seedu.manager.logic.commands;

import java.util.HashSet;
import java.util.Set;

import seedu.manager.commons.exceptions.IllegalValueException;
import seedu.manager.model.activity.*;

/**
 * Undoes a command (or multiple commands) that changes state.
 */
//@@author A0139797E
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String USAGE = "undo\n" + "undo NUMBER_OF_TIMES";

    public static final String EXAMPLES = "undo\n" + "undo 2";
    
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Reverts to-do list to a previous state.\n"
              + "\nUsage:\nundo [NUMBER_OF_COMMANDS]\n";

    public static final String MESSAGE_SUCCESS = "Reverted to previous state. (%d commands undone)";
    
    public static final String MESSAGE_INDEX_LESS_THAN_ZERO = "Nothing left to undo.";
    
    public static final String MESSAGE_OFFSET_OUT_OF_BOUNDS = "Insufficient number of commands to perform undo operation.";
    
    private int offset = 0;

    
    public UndoCommand() {
        offset = 1;
    }
    
    public UndoCommand(int newOffset) {
        offset = newOffset;
    }
    
    @Override
    public CommandResult execute() {
        assert model != null;
        if (model.getHistoryIndex() <= 0) {
            return new CommandResult(MESSAGE_INDEX_LESS_THAN_ZERO);
        } else if (model.getHistoryIndex() - offset < 0) {
            return new CommandResult(MESSAGE_OFFSET_OUT_OF_BOUNDS);
        } else {
            model.undoCommand(offset);
            return new CommandResult(String.format(MESSAGE_SUCCESS, offset));
        }
    }

}
