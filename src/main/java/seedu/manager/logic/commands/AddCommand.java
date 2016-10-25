package seedu.manager.logic.commands;

import java.util.HashSet;
import java.util.Set;

import seedu.manager.commons.exceptions.IllegalValueException;
import seedu.manager.model.activity.*;
import seedu.manager.model.tag.Tag;
import seedu.manager.model.tag.UniqueTagList;

/**
 * Adds an activity to the activity manager.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a activity to the activity manager.\n"
              + "\nUsage:\nadd ACTIVITY\n"
              + "add ACTIVITY on DATETIME"
              + "add ACTIVITY from DATETIME to DATETIME\n"
              + "\nExamples:\n"
              + "add buy bread"
              + "add complete assignment 0 on 25 Oct 1000"
              + "add attend conference from 23 Oct 1000 to 23 Oct 1200";

    public static final String MESSAGE_SUCCESS = "New activity added: %1$s";
    public static final String MESSAGE_DUPLICATE_ACTIVITY = "This activity already exists in the address book";

    private final Activity toAdd;

    /**
     * Constructor for floating tasks
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(String name) {
        this.toAdd = new Activity(name);
    }
    
    /**
     * Constructor for deadlines
     * 
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(String name, String dateTime) {
        this.toAdd = new Activity(name, dateTime);
    }

    /**
     * Constructor for events
     * 
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(String name, String startDateTime, String endDateTime)
            throws IllegalValueException {
        this.toAdd = new Activity(name, startDateTime, endDateTime);
    }
    
    @Override
    public CommandResult execute() {
        assert model != null;
        model.addActivity(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd.getName()));
    }

}
