package seedu.manager.logic.commands;

import seedu.manager.model.activity.*;

/**
 * Adds an activity to the activity manager.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String USAGE = "add TASK\n" +
                                       "add DEADLINE on DATE_TIME\n" +
                                       "add EVENT from DATE_TIME to DATE_TIME";
    
    public static final String EXAMPLES = "add buy groceries\n" +
                                          "add do homework on 21 oct\n" +
                                          "add attend talk from 1 oct 10am to 1 oct 12pm";
    
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a activity to the activity manager.\n"
              + "Usage: " + COMMAND_WORD + " ACTIVITY / "
              + COMMAND_WORD + " ACTIVITY on DATETIME / "
              + COMMAND_WORD + " ACTIVITY from DATETIME to DATETIME";

    public static final String MESSAGE_SUCCESS = "New activity added: %1$s";
    public static final String MESSAGE_RECUR_SUCCESS = "New recurring activity added: %1$s";

    private Activity toAdd;
    private ActivityList toAddList;

    //@@author A0135730M
    /**
     * Constructor for required field (name)
     */
    public AddCommand(String name) {
        this.toAdd = new Activity(name);
    }
    
    /**
     * Setters to add in optional/derived fields
     */
    public void setType(ActivityType type) {
        this.toAdd.setType(type);
    }
    
    public void setDateTime(String dateTime) {
        this.toAdd.setDateTime(dateTime);
    }
    
    public void setEndDateTime(String endDateTime) {
        this.toAdd.setEndDateTime(endDateTime);
    }
    
    /**
     * Creates a list of recurring activities to be added
     */
    public void setRecurring(int recurNum, String recurUnit) {
        this.toAddList = new ActivityList();
        for (int numLater=0; numLater<recurNum; numLater++) {
            // use toAdd as the base activity for each recurring activity
            Activity newActivity = new Activity(this.toAdd);
            newActivity.setOffset(numLater, recurUnit);
            this.toAddList.add(newActivity);
        }
    }
    
    //@@author A0139797E
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
