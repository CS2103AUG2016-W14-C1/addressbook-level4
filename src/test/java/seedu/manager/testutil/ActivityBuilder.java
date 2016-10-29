package seedu.manager.testutil;

import seedu.manager.commons.exceptions.IllegalValueException;
import seedu.manager.commons.util.StringUtil;
import seedu.manager.model.activity.ActivityType;

/**
 *
 */
public class ActivityBuilder {

    private TestActivity activity;

    public ActivityBuilder() {
        this.activity = new TestActivity();
    }

    public ActivityBuilder withName(String name) {
        this.activity.setName(name);
        return this;
    }
    
    //@@author A0144704L
    public ActivityBuilder withNameAndStatus(String name, boolean status) {
        this.activity.setName(name);
        this.activity.setStatus(status);
        return this;
    }
    
    public ActivityBuilder withNameAndTime(String name, String dateTime) throws IllegalValueException {
        StringUtil.validateAMDate(dateTime);
        
        this.activity.setName(name);
        this.activity.setType(ActivityType.DEADLINE);
        this.activity.setDateTime(dateTime);
        return this;
    }
    
    public ActivityBuilder withNameAndStartEndTime(String name, String startDateTime, String endDateTime) throws IllegalValueException {
        StringUtil.validateAMDate(startDateTime, endDateTime);
        
        this.activity.setName(name);
        this.activity.setType(ActivityType.EVENT);
        this.activity.setDateTime(startDateTime);
        this.activity.setEndDateTime(endDateTime);
        return this;
    }
    
    public TestActivity build() {
        return this.activity;
    }

}
