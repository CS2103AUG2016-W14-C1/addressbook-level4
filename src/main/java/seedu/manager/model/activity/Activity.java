package seedu.manager.model.activity;

/**
 * Abstract class for all types of activity in Remindaroo.
 */

public abstract class Activity implements ReadOnlyActivity, Comparable<Activity> {
	protected String name;
	protected Status status;
	
	public Activity(String name) {
	    this.name = name;
		this.status = new Status();
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
	
	public void setName(String newName) {
		this.name = newName;
	}
	
	public void setStatus(boolean completed) {
		if (completed) {
			(this.status).setCompleted();
		} else {
			(this.status).setPending();
		}
	}
	
	@Override
	public Status getStatus() {
		return this.status;
	}
	
	@Override
	public boolean equals(Object o) {
	    return o == this
	                // Activity equality
	            || (o instanceof Activity
	                && this.name.equals(((Activity)o).name)
	                && this.status.equals(((Activity)o).status)
	                // FloatingActivity equality
	                && ((this instanceof FloatingActivity && o instanceof FloatingActivity)
	                // DeadlineActivity equality
	                || (this instanceof DeadlineActivity && o instanceof DeadlineActivity
	                    && ((DeadlineActivity)this).getDateTime().equals(((DeadlineActivity)o).getDateTime()))
	                // EventActivity equality
	                || (this instanceof EventActivity && o instanceof EventActivity
	                    && ((EventActivity)this).getDateTime().equals(((EventActivity)o).getDateTime())
	                    && ((EventActivity)this).getEndDateTime().equals(((EventActivity)o).getEndDateTime()))
	                ));
	}
	
    @Override
    public int compareTo(Activity other) {
        // Check for floating tasks
        if (this instanceof FloatingActivity && other instanceof FloatingActivity) {
            return 0;
        } else if (other instanceof FloatingActivity) {
            return -1;
        } else if (this instanceof FloatingActivity) {
            return 1;
        // Comparison between 2 deadlines
        } else if (this instanceof DeadlineActivity && other instanceof DeadlineActivity) {
            return (int)(((DeadlineActivity) this).getDateTime().getTime() - ((DeadlineActivity) other).getDateTime().getTime());
        // Comparisons between a deadline and an event
        } else if (this instanceof EventActivity && other instanceof DeadlineActivity) {
            return (int)(((EventActivity) this).getDateTime().getTime() - ((DeadlineActivity) other).getDateTime().getTime());     
        } else if (this instanceof DeadlineActivity && other instanceof EventActivity) {
            return (int)(((DeadlineActivity) this).getDateTime().getTime() - ((EventActivity) other).getDateTime().getTime());   
        // Comparisons between 2 events
        } else {
           long startTimeDifference = ((EventActivity) this).getDateTime().getTime() - ((EventActivity) other).getDateTime().getTime();     
           if (startTimeDifference == 0) {
               return (int)(((EventActivity) this).getEndDateTime().getTime() - ((EventActivity) other).getEndDateTime().getTime());
           } else {
               return ((int) startTimeDifference);
           }
        }
    }
}
