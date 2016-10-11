package seedu.address.model.activity;

/**
 * Represents an activity's status in Remindaroo 
 */

public class Status{
	private static final String MESSAGE_STATUS_PENDING = "Pending";
	private static final String MESSAGE_STATUS_COMPLETED = "Completed";
	
	private boolean status;
	
	public Status() {
		this.status = false;
	}
	
	
	@Override
	public String toString() {
		if (status) {
			return MESSAGE_STATUS_COMPLETED;
		} else {
			return MESSAGE_STATUS_PENDING;
		}
	}
	
	public void setPending() {
		this.status = false;
	}
	
	public void setCompleted() {
		this.status = true;
	}
	
	public boolean isCompleted() {
		return this.status;
	}
}