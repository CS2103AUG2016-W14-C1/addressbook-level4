package seedu.manager.testutil;

import seedu.manager.model.activity.*;

/**
 * A mutable activity object. For testing only.
 */ 
//@@author A0135730M
public class TestActivity implements ReadOnlyActivity {

    private ActivityType type;
    private String name;
    private AMDate dateTime;
    private AMDate endDateTime;
    private Status status;
    private boolean selected;

    public TestActivity() {
        type = ActivityType.FLOATING; // default floating
        status = new Status();
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean getSelected() {
        return selected;
    }
    
    @Override
    public String toString() {
        return this.getName();
    }

    public String getAddCommand() {
        StringBuilder sb = new StringBuilder();
        sb.append("add " + this.getName());
        if (this.type.equals(ActivityType.DEADLINE)) {
        	sb.append(" by " + this.getDateTime());
        } else if (this.type.equals(ActivityType.EVENT)) {
        	sb.append(" from " + this.getDateTime() + " to " + this.getEndDateTime());
        }
        return sb.toString();
    }

    
	public void setStatus(boolean completed) {
		(this.status).setStatus(completed);
	}

    @Override
    public Status getStatus() {
        return this.status;
    }

    @Override
    public AMDate getDateTime() {
        return dateTime;
    }
    
    @Override
    public AMDate getEndDateTime() {
        return endDateTime;
    }

    
    public void setDateTime(String newDateTime) {
        if (this.dateTime == null) {
            this.dateTime = new AMDate(newDateTime);
        } else {
            this.dateTime.setAMDate(newDateTime);
        }
    }

    
    public void setEndDateTime(String newEndDateTime) {
        if (this.endDateTime == null) {
            this.endDateTime = new AMDate(newEndDateTime);
        } else {
            this.endDateTime.setAMDate(newEndDateTime); 
        }
    }

    @Override
    public ActivityType getType() {
        return type;
    }
    
    public void setType(ActivityType type) {
        this.type = type;
    }
}
