package seedu.manager.model.activity;

import java.util.Collections;
import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.manager.commons.exceptions.IllegalValueException;

public class ActivityList implements Iterable<Activity> {
	
	private final ObservableList<Activity> internalList = FXCollections.observableArrayList();
	
	/* Construct an empty ActivityList */
	public ActivityList() {}

    /**
     * Adds a activity to the list.
     * 
     */
    public void add(Activity toAdd){
        assert toAdd != null;
        internalList.add(toAdd);
        Collections.sort(internalList);
    }
    
    /**
     * Removes the equivalent activity from the list.
     *
     * @throws ActivityNotFoundException if no such person could be found in the list.
     */
    public boolean remove(Activity toRemove) throws ActivityNotFoundException {
        assert toRemove != null;
        final boolean activityFoundAndDeleted = internalList.remove(toRemove);
        if (!activityFoundAndDeleted) {
            throw new ActivityNotFoundException();
        }
        Collections.sort(internalList);
        return activityFoundAndDeleted;
    }
    
    /**
     * Updates the equivalent activity in the list.
     *
     * @throws ActivityNotFoundException if no such activity could be found in the list.
     * @throws IllegalValueException if the dateTime and endDateTime are invalid
     */
    
    public boolean update(Activity toUpdate, String newName, String newDateTime, String newEndDateTime) throws ActivityNotFoundException, IllegalValueException {
    	assert toUpdate != null;
    	final boolean activityFound = internalList.contains(toUpdate);
    	if (activityFound) {
    		try {
		    	int toUpdateIndex = internalList.indexOf(toUpdate);
		    	Activity toUpdateInList = internalList.get(toUpdateIndex);
		    	// Update Activity name
		    	if (newName != null && !newName.equals("")) toUpdateInList.setName(newName);
		    	// Handle Deadline tasks
		    	if (toUpdate instanceof DeadlineActivity) {
		    		if (newDateTime != null) ((DeadlineActivity) toUpdateInList).setDateTime(newDateTime);
		    	}
		    	// Handle Event tasks
		    	if (toUpdate instanceof EventActivity) {
		    		if (newDateTime != null && !newDateTime.equals("")) ((EventActivity) toUpdateInList).setDateTime(newDateTime);
		    		if (newEndDateTime != null && !newEndDateTime.equals("")) ((EventActivity) toUpdateInList).setEndDateTime(newEndDateTime);
		    	}
    		} catch (Exception e) {
				throw new IllegalValueException(e.getMessage());
			}
    	} else {
    		throw new ActivityNotFoundException();
    	}
    	Collections.sort(internalList);
    	return activityFound;
    }
    
    /**
     * Marks the equivalent activity in the list as pending or completed.
     *
     * @throws ActivityNotFoundException if no such activity could be found in the list.
     */
    
    public boolean mark(Activity toMark, boolean status) throws ActivityNotFoundException {
    	assert toMark != null;
    	final boolean activityFound = internalList.contains(toMark);
    	if (activityFound) {
    		toMark.setStatus(status);
    	} else {
    		throw new ActivityNotFoundException();
    	}
    	return activityFound;
    }
    
    
    /**
     * Signals that an operation targeting a specified activity in the list would fail because
     * there is no such matching activity in the list.
     */
    public static class ActivityNotFoundException extends Exception {}
    
	
    public ObservableList<Activity> getInternalList() {
        return internalList;
    }

    public int size() {
        return internalList.size();
    }
    
    @Override
    public Iterator<Activity> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ActivityList // instanceof handles nulls
                && this.internalList.equals(
                ((ActivityList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

}
