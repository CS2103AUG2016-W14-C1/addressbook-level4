package seedu.manager.model.activity;

/**
 *  Class for all types of activity in Remindaroo.
 */

public class Activity implements ReadOnlyActivity, Comparable<Activity> {
    
    public static final String MESSAGE_DATE_CONSTRAINTS = "Event has already ended before it starts.";
    
    private ActivityType type;
	private String name;
	private Status status;
	private AMDate dateTime;
	private AMDate endDateTime;
	
	// Floating activity constructor
	
	public Activity(String name) {
	    this.type = ActivityType.FLOATING;
	    this.name = name;
		this.status = new Status();
		this.dateTime = null;
		this.endDateTime = null;
	}
	
	// Deadline activity constructor
	
	/**
     * Constructor which gets dateTime in natural English from user input
     */
    public Activity(String name, String newDateTime) {
        this(name);
        this.type = ActivityType.DEADLINE;
        this.dateTime = new AMDate(newDateTime);
    }
	
	/**
     * Constructor which gets dateTime in epoch format from XML file
     */
    public Activity(String name, Long newEpochDateTime) {
        this(name);
        this.type = ActivityType.DEADLINE;
        this.dateTime = new AMDate(newEpochDateTime);
    }
    
    // Event activity constructor
    
    /**
     * Constructor which gets start and end dateTime in natural English from user input
     */
    public Activity(String name, String newStartDateTime, String newEndDateTime) {
        this(name);
        this.type = ActivityType.EVENT;
        this.dateTime = new AMDate(newStartDateTime);
        this.endDateTime = new AMDate(newEndDateTime);
    }
    
    /**
     * Constructor which gets start and end dateTime in epoch format from XML file
     */
    public Activity(String name, Long newEpochStartDateTime, Long newEpochEndDateTime) {
        this(name);
        this.type = ActivityType.EVENT;
        this.dateTime = new AMDate(newEpochStartDateTime);
        this.endDateTime = new AMDate(newEpochEndDateTime);
    }
    
    // Wrapper constructor for ReadOnlyActivity
    public Activity(ReadOnlyActivity source) {
        this.type = source.getType();
        this.name = source.getName();
        this.status = source.getStatus();
        this.dateTime = source.getDateTime();
        this.endDateTime = source.getDateTime();
    }
	
    @Override
    public ActivityType getType() {
        return type;
    }
    
    public void setType(ActivityType type) {
        this.type = type;
    }
    
	@Override
	public String getName() {
	    return name;
	}
	
	public void setName(String newName) {
		this.name = newName;
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
        assert newDateTime != null;
        assert !this.type.equals(ActivityType.FLOATING);
        if (this.dateTime == null) {
            this.dateTime = new AMDate(newDateTime);
        } else {
            this.dateTime.setAMDate(newDateTime);
        }
    }
    
    
    public void setEndDateTime(String newEndDateTime) {
        assert !this.type.equals(ActivityType.FLOATING);
        // remove endDateTime if activity is converted to deadline
        if (this.type.equals(ActivityType.DEADLINE)) {
            assert newEndDateTime == null;
            this.endDateTime = null;
        } else if (this.type.equals(ActivityType.EVENT)) {
            if (this.endDateTime == null) {
                this.endDateTime = new AMDate(newEndDateTime);
            } else {
                this.endDateTime.setAMDate(newEndDateTime);
            }
        }
    }
	
	@Override
	public boolean equals(Object o) {
	    return o == this
	                // Activity equality
	            || (o instanceof Activity
	                && this.name.equals(((Activity)o).name)
	                && this.status.equals(((Activity)o).status)
	                && (this.dateTime == null && ((Activity)o).dateTime == null
	                   || this.dateTime.equals(((Activity)o).dateTime))
	                && (this.endDateTime == null && ((Activity)o).endDateTime == null 
	                   || this.endDateTime.equals(((Activity)o).endDateTime))
	                );
	}
	
    @Override
    public int compareTo(Activity other) {
        // Check for floating tasks
        if (this.type.equals(ActivityType.FLOATING) && other.type.equals(ActivityType.FLOATING)) {
            return 0;
        } else if (other.type.equals(ActivityType.FLOATING)) {
            return -1;
        } else if (this.type.equals(ActivityType.FLOATING)) {
            return 1;
        // Comparison between 2 deadlines
        } else if (this.type.equals(ActivityType.DEADLINE) && other.type.equals(ActivityType.DEADLINE)) {
            return (int)(this.getDateTime().getTime() - other.getDateTime().getTime());
        // Comparisons between a deadline and an event
        } else if (this.type.equals(ActivityType.EVENT) && other.type.equals(ActivityType.DEADLINE)) {
            return (int)(this.getDateTime().getTime() - other.getDateTime().getTime());     
        } else if (this.type.equals(ActivityType.DEADLINE) && other.type.equals(ActivityType.EVENT)) {
            return (int)(this.getDateTime().getTime() - other.getDateTime().getTime());   
        // Comparisons between 2 events
        } else {
           long startTimeDifference = this.getDateTime().getTime() - other.getDateTime().getTime();     
           if (startTimeDifference == 0) {
               return (int)(this.getEndDateTime().getTime() - other.getEndDateTime().getTime());
           } else {
               return ((int) startTimeDifference);
           }
        }
    }
}
