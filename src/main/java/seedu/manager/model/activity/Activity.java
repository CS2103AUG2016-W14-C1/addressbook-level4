package seedu.manager.model.activity;

/**
 * Abstract class for all types of activity in Remindaroo.
 */

public abstract class Activity implements ReadOnlyActivity {
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
	
	// TODO: Re-implement equality if necessary when more details are added
	@Override
	public boolean equals(Object o) {
	    // Check for name equality
	    return o == this ||
	           (o instanceof Activity &&
	            this.name.equals(((Activity)o).name));
	}
}
