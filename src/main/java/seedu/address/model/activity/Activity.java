package seedu.address.model.activity;

import java.util.Objects;

public class Activity implements ReadOnlyActivity {
	public String name;
	public Status status;
	
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
	
	@Override
	public void setName(String newName) {
		this.name = newName;
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
