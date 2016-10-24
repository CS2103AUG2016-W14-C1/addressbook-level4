package seedu.manager.model.activity;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
     */
    public void remove(Activity toRemove) {
        assert toRemove != null;
        assert internalList.contains(toRemove);
        internalList.remove(toRemove);
        Collections.sort(internalList);
    }
    
    /**
     * Updates the equivalent activity in the list.
     */
    
    public void update(Activity toUpdate, String newName, String newDateTime, String newEndDateTime) {
    	assert toUpdate != null;
    	assert internalList.contains(toUpdate);
    	
    	int toUpdateIndex = internalList.indexOf(toUpdate);
    	Activity toUpdateInList = internalList.get(toUpdateIndex);
    	// Update Activity name (if there is new name)
    	if (newName != null && !newName.equals("")) {
    	    toUpdateInList.setName(newName);
    	}
    	// Update task to event
    	if (newDateTime != null && newEndDateTime != null) {
    	    toUpdateInList.setDateTime(newDateTime);
    	    toUpdateInList.setEndDateTime(newEndDateTime);
    	    toUpdateInList.setType(ActivityType.EVENT);
    	// Update task to deadline
	    } else if (newDateTime != null) {
    		toUpdateInList.setDateTime(newDateTime);
    		toUpdateInList.setType(ActivityType.DEADLINE);
    		toUpdateInList.setEndDateTime(null);
    	}
    	Collections.sort(internalList);
    }
    
    /**
     * Marks the equivalent activity in the list as completed.
     */
    
    public void mark(Activity toMark) {
    	assert toMark != null;
    	assert internalList.contains(toMark);
    	toMark.setStatus(true);
    }
    
    /**
     * Marks the equivalent activity in the list as pending.
     */
    
    public void unmark(Activity toUnmark) {
    	assert toUnmark != null;
    	assert internalList.contains(toUnmark);
    	toUnmark.setStatus(false);
    }
	
    public ObservableList<Activity> getInternalList() {
        return internalList;
    }
    
    public FilteredList<Activity> getPendingInternalList() {
    	return internalList.filtered(new Predicate<Activity>() {
    		public boolean test(Activity activity) {
    			return !activity.getStatus().isCompleted();
    		}
    	});
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
