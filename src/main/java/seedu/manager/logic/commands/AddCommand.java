package seedu.manager.logic.commands;

import java.util.HashSet;
import java.util.Set;

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
              + "add complete assignment 0 on 25 Oct 10:00"
              + "add attend conference from 23 Oct 10:00 to 23 Oct 12:00";

    public static final String MESSAGE_SUCCESS = "New activity added: %1$s";
    public static final String MESSAGE_RECUR_SUCCESS = "New recurring activity added: %1$s";

    private Activity toAdd;
    private ActivityList toAddList;

    /**
     * Constructor for floating tasks
     */
    public AddCommand(String name) {
        this.toAdd = new Activity(name);
    }
    
    /**
     * Constructor for deadlines
     */
    public AddCommand(String name, String dateTime) {
        this.toAdd = new Activity(name, dateTime);
    }
    
    /**
     * Constructor for recurring deadlines
     */
    public AddCommand(String name, String dateTime, int recurNum, String recurUnit) {
        this.toAddList = new ActivityList();
        for (int numLater=0; numLater<recurNum; numLater++) {
            this.toAddList.add(new Activity(name, dateTime, numLater, recurUnit));
        }
    }

    /**
     * Constructor for events
     */
    public AddCommand(String name, String startDateTime, String endDateTime) {
        this.toAdd = new Activity(name, startDateTime, endDateTime);
    }
    
    /**
     * Constructor for recurring events
     */
    public AddCommand(String name, String startDateTime, String endDateTime, int recurNum, String recurUnit) {
        this.toAddList = new ActivityList();
        for (int numLater=0; numLater<recurNum; numLater++) {
            this.toAddList.add(new Activity(name, startDateTime, endDateTime, numLater, recurUnit));
        }
    }
    
    @Override
    public CommandResult execute() {
        assert model != null;
        // add recurring
        if (this.toAddList != null) {
            String addName = null;
            for (int i = 0; i < this.toAddList.size(); i++) {
                Activity add = this.toAddList.getInternalList().get(i);
                addName = add.getName();
                model.addActivity(add, i == this.toAddList.size() - 1);
            }
            return new CommandResult(String.format(MESSAGE_RECUR_SUCCESS, addName));
        // add normal
        } else {
            assert toAdd != null;
            model.addActivity(toAdd, true);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd.getName()));
        }
    }

}
