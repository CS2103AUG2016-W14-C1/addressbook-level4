package seedu.address.model.activity;

import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.UniquePersonList.DuplicatePersonException;
import seedu.address.model.person.UniquePersonList.PersonNotFoundException;

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
        return activityFoundAndDeleted;
    }
    
    /**
     * Signals that an operation targeting a specified person in the list would fail because
     * there is no such matching person in the list.
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
                || (other instanceof UniquePersonList // instanceof handles nulls
                && this.internalList.equals(
                ((ActivityList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

}
