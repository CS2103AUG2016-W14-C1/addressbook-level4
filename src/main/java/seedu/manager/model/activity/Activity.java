package seedu.manager.model.activity;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Activity implements ReadOnlyActivity {
	public String name;
	public Status status;
	public AMDate dateTime;
	public AMDate endDateTime;
	
	
	public Activity(String name) {
	    this.name = name;
		this.status = new Status();
	}
	
	public Activity(String name, String newDateTime) {
        this(name);
        this.dateTime = new AMDate(newDateTime);
    }
	
	public Activity(String name, Long newEpochDateTime) {
        this(name);
        this.dateTime = new AMDate(newEpochDateTime);
    }

    public Activity(String name, String newStartDateTime, String newEndDateTime) {
        this(name);
        this.dateTime = new AMDate(newStartDateTime);
        this.endDateTime = new AMDate(newEndDateTime);
    }
	
	public Activity(String name, Long newEpochStartDateTime, Long newEpochEndDateTime) {
        this(name);
        this.dateTime = new AMDate(newEpochStartDateTime);
        this.endDateTime = new AMDate(newEpochEndDateTime);
    }
	
	/**
	 * Copy constructor
	 */
	public Activity(ReadOnlyActivity source) {
	    this(source.getName());
	}
	
	@Override
	public String getName() {
	    return name;
	}
	
	@Override
    public AMDate getDate() {
        return dateTime;
    }
	
	@Override
    public AMDate getEndDate() {
        return endDateTime;
    }
	
	@Override
	public void setName(String newName) {
		this.name = newName;
	}
	
	@Override
    public void setDateTime(String newDateTime) {
	    this.dateTime.setAMDate(newDateTime);
	    }
	
	@Override
    public void setEndDateTime(String newEndDateTime) {
	    this.endDateTime.setAMDate(newEndDateTime);
	}
	
	@Override
	public void setStatus(boolean completed) {
		if (completed) {
			(this.status).setCompleted();
		} else {
			(this.status).setPending();
		}
	}
	
	@Override
	public String getStatus() {
		return (this.status).toString();
	}
	
	// TODO: Re-implement equality if necessary when more details are added
	@Override
	public boolean equals(Object o) {
	    // Check for name equality
	    return o == this ||
	           (o instanceof Activity &&
	            this.name.equals(((Activity)o).name));
	}
}
