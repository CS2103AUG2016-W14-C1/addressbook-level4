//@@author A0139797E
package seedu.manager.commons.events.ui;

import seedu.manager.commons.events.BaseEvent;

/**
 * Represents a selection change in the Activity List Panel
 */
public class ActivityListPanelUpdateEvent extends BaseEvent {


    private final int targetIndex;

    public ActivityListPanelUpdateEvent(int index){
        this.targetIndex = index;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
    
    public int getTargetIndex() {
        return targetIndex;
    }
}
