package seedu.manager.model.activity;

/**
 * Represents an activity's status in Remindaroo 
 */
//@@author A0144704L
public class Status{
	private static final String MESSAGE_STATUS_PENDING = "pending";
	private static final String MESSAGE_STATUS_COMPLETED = "completed";
	
	private boolean status;
	
	public Status() {
		this.status = false;
	}
	
	public Status(Status other) {
	    this.status = other.status;
	}
	
	@Override
	public boolean equals(Object o) {
	    return this.status == ((Status)o).status;
	}
	
	@Override
	public String toString() {
		if (status) {
			return MESSAGE_STATUS_COMPLETED;
		} else {
			return MESSAGE_STATUS_PENDING;
		}
	}
	
	public void setStatus(boolean isCompleted) {
		this.status = isCompleted;
	}
	
	public boolean isCompleted() {
		return this.status;
	}
}
