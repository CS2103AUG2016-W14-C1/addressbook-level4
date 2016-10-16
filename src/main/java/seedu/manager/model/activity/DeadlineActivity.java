package seedu.manager.model.activity;

import seedu.manager.commons.exceptions.IllegalValueException;

/**
 * Represents a deadline activity in Remindaroo.
 * Guarantees: details are present and not null, field values are validated.
 */

public class DeadlineActivity extends Activity {
    private AMDate dateTime;
    
    /**
     * Default constructor, should not be called
     */
    public DeadlineActivity(String name) throws IllegalValueException {
        super(name);
        throw new IllegalValueException("Deadline should has a dateTime");
    }
    
    /**
     * Constructor which gets dateTime in natural English from user input
     */
    public DeadlineActivity(String name, String newDateTime) {
        super(name);
        this.dateTime = new AMDate(newDateTime);
    }
    
    /**
     * Constructor which gets dateTime in epoch format from XML file
     */
    public DeadlineActivity(String name, Long newEpochDateTime) {
        super(name);
        this.dateTime = new AMDate(newEpochDateTime);
    }
    
    public AMDate getDateTime() {
        return dateTime;
    }
    
    public void setDateTime(String newDateTime) {
        this.dateTime.setAMDate(newDateTime);
    }
}
