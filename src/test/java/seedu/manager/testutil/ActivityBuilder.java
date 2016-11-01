package seedu.manager.testutil;

import seedu.manager.commons.exceptions.IllegalValueException;
import seedu.manager.model.activity.ActivityType;

/**
 *
 */
public class ActivityBuilder {

    private TestActivity activity;

    public ActivityBuilder() {
        this.activity = new TestActivity();
    }

    public ActivityBuilder withName(String name) throws IllegalValueException {
        this.activity.setName(name);
        return this;
    }
    
    //@@author A0144704L
    public ActivityBuilder withNameandStatus(String name, boolean status) throws IllegalValueException {
        this.activity.setName(name);
        this.activity.setStatus(status);
        return this;
    }
    
    public ActivityBuilder withNameandTime(String name, String dateTime) throws IllegalValueException {
        this.activity.setName(name);
        this.activity.setType(ActivityType.DEADLINE);
        this.activity.setDateTime(dateTime);
        return this;
    }
    
    public TestActivity build() {
        return this.activity;
    }

}
