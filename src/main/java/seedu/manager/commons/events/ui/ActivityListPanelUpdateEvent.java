//@@author A0139797E
package seedu.manager.commons.events.ui;


import javafx.collections.ObservableList;
import seedu.manager.commons.events.BaseEvent;
import seedu.manager.model.activity.Activity;

/**
 * Represents a selection change in the Activity List Panel
 */
public class ActivityListPanelUpdateEvent extends BaseEvent {


    private final ObservableList<Activity> updatedFloatingActivityList;
    private final ObservableList<Activity> updatedScheduleList;

    public ActivityListPanelUpdateEvent(ObservableList<Activity> observableFloatingTaskList, 
                                        ObservableList<Activity> observableScheduleList){
        this.updatedFloatingActivityList = observableFloatingTaskList;
        this.updatedScheduleList = observableScheduleList;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
    
    public ObservableList<Activity> getUpdatedFloatingActivityList() {
    	return updatedFloatingActivityList;
    }
    
    public ObservableList<Activity> getUpdatedScheduleList() {
        return updatedScheduleList;
    }
}
