package seedu.manager.commons.events.ui;


import javafx.collections.ObservableList;
import seedu.manager.commons.events.BaseEvent;
import seedu.manager.model.activity.Activity;

/**
 * Represents a selection change in the Person List Panel
 */
public class FloatingTaskPanelUpdateEvent extends BaseEvent {


    private final ObservableList<Activity> updatedFloatingActivityList;

    public FloatingTaskPanelUpdateEvent(ObservableList<Activity> observableList){
        this.updatedFloatingActivityList = observableList;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
    
    public ObservableList<Activity> getUpdatedList() {
    	return updatedFloatingActivityList;
    }
}
