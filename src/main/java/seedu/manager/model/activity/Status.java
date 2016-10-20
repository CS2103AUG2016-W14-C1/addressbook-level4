package seedu.manager.model.activity;

/**
 * Represents an activity's status in Remindaroo 
 */

public class Status{
	private static final String MESSAGE_STATUS_PENDING = "pending";
	private static final String MESSAGE_STATUS_COMPLETED = "completed";
	
	private boolean status;
	
	public Status() {
		this.status = false;
	}
	
	public Status(boolean status) {
		this.status = status;
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
