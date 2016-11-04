package seedu.manager.model.activity;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.Predicate;

import com.google.common.base.Function;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class ActivityList implements Iterable<Activity> {
	
	private final ObservableList<Activity> internalList = FXCollections.observableArrayList();
	
	//@@author A0139797E
	/**
     * Adds a activity to the list.
     * 
     */
    public void add(Activity toAdd){
        assert toAdd != null;
        internalList.add(toAdd);
        Collections.sort(internalList);
    }
    
  //@@author A0144881Y
    /**
     * Removes the equivalent activity from the list.
     */
    public void remove(Activity toRemove) {
        assert toRemove != null;
        assert internalList.contains(toRemove);
        internalList.remove(toRemove);
        Collections.sort(internalList);
    }
    
    //@@author A0135730M
    /**
     * Updates the equivalent activity in the list.
     */
    public void update(Activity toUpdate, String newName, String newDateTime, String newEndDateTime) {
    	assert toUpdate != null;
    	assert internalList.contains(toUpdate);
    	
    	int toUpdateIndex = internalList.indexOf(toUpdate);
    	Activity toUpdateInList = internalList.get(toUpdateIndex);
    	
    	// construct the existing activity on a new activity
    	Activity newActivity = new Activity(toUpdateInList);
        
        // Update Activity name (if there is new name)
    	if (newName != null && !"".equals(newName)) {
    	    newActivity.setName(newName);
    	}
    	// Update task to event
    	if (newDateTime != null && newEndDateTime != null) {
    	    newActivity.setType(ActivityType.EVENT);
    	    newActivity.setDateTime(newDateTime);
    	    newActivity.setEndDateTime(newEndDateTime);
    	// Update task to deadline
	    } else if (newDateTime != null) {
	        newActivity.setType(ActivityType.DEADLINE);
    		newActivity.setDateTime(newDateTime);
    		newActivity.removeEndDateTime();
    	}
    	internalList.set(toUpdateIndex, newActivity);
    	Collections.sort(internalList);
    }
    
    //@@author A0144704L
    /**
     * Marks the equivalent activity in the list as completed.
     */
    public void mark(Activity toMark) {
    	assert toMark != null;
    	assert internalList.contains(toMark);
    	Activity newActivity;
    	switch (toMark.getType()) {
            case DEADLINE:
                newActivity = new Activity(toMark.getName(), toMark.getDateTime().getTime());
                break;
            case EVENT:
                newActivity = new Activity(toMark.getName(), toMark.getDateTime().getTime(), toMark.getEndDateTime().getTime());
                break;
            case FLOATING:
            default:
                newActivity = new Activity(toMark.getName());
                break;
        }
    	newActivity.setStatus(true);
    	int toMarkIndex = internalList.indexOf(toMark);
    	internalList.set(toMarkIndex, newActivity);
    	Collections.sort(internalList);
    	
    }
    
    /**
     * Marks the equivalent activity in the list as pending.
     */
    public void unmark(Activity toUnmark) {
    	assert toUnmark != null;
    	assert internalList.contains(toUnmark);
    	Activity newActivity;
        switch (toUnmark.getType()) {
            case DEADLINE:
                newActivity = new Activity(toUnmark.getName(), toUnmark.getDateTime().getTime());
                break;
            case EVENT:
                newActivity = new Activity(toUnmark.getName(), toUnmark.getDateTime().getTime(), toUnmark.getEndDateTime().getTime());
                break;
            case FLOATING:
            default:
                newActivity = new Activity(toUnmark.getName());
                break;
        }
        newActivity.setStatus(false);
        int toUnmarkIndex = internalList.indexOf(toUnmark);
        internalList.set(toUnmarkIndex, newActivity);
        Collections.sort(internalList);
    }
    
    public void list() {
    	Collections.sort(internalList);
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
