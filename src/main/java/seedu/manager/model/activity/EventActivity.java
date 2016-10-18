package seedu.manager.model.activity;

import seedu.manager.commons.exceptions.IllegalValueException;

/**
 * Represents an event activity in Remindaroo.
 * Guarantees: details are present and not null, field values are validated.
 */

public class EventActivity extends Activity {
    public static final String MESSAGE_DATE_CONSTRAINTS = "Event has already ended before it starts.";
    private AMDate dateTime;
    private AMDate endDateTime;

    /**
     * Default constructor, should not be called
     */
    public EventActivity(String name) throws IllegalValueException {
        super(name);
        throw new IllegalValueException("Event has no start and/or end date/time.");
    }
    
    /**
     * Constructor which gets start and end dateTime in natural English from user input
     */
    public EventActivity(String name, String newStartDateTime, String newEndDateTime) throws IllegalValueException {
        super(name);
        this.dateTime = new AMDate(newStartDateTime);
        this.endDateTime = new AMDate(newEndDateTime);
        if (this.dateTime.getTime() > this.endDateTime.getTime()) {
            throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
        }
    }
    
    /**
     * Constructor which gets start and end dateTime in epoch format from XML file
     */
    public EventActivity(String name, Long newEpochStartDateTime, Long newEpochEndDateTime) {
        super(name);
        this.dateTime = new AMDate(newEpochStartDateTime);
        this.endDateTime = new AMDate(newEpochEndDateTime);
    }
    
    /**
     * Constructor which gets start and end dateTime in AMDate, used for testing
     */
    public EventActivity(String name, AMDate testDateTime, AMDate testEndDateTime) {
        super(name);
        this.dateTime = testDateTime;
        this.endDateTime = testEndDateTime;
    }
    
    public AMDate getDateTime() {
        return dateTime;
    }
    
    public AMDate getEndDateTime() {
        return endDateTime;
    }
    
    public void setDateTime(String newDateTime) throws IllegalValueException {
        if ((new AMDate(newDateTime)).getTime() > this.endDateTime.getTime()) {
            throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
        }
        this.dateTime.setAMDate(newDateTime);
    }
    
    
    public void setEndDateTime(String newEndDateTime) throws IllegalValueException {
        if (this.dateTime.getTime() > (new AMDate(newEndDateTime)).getTime()) {
            throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
        }
        this.endDateTime.setAMDate(newEndDateTime);
    }
}
