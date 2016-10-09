package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.activity.Activity;

/**
 * Represents a selection change in the Person List Panel
 */
public class ActivityPanelSelectionChangedEvent extends BaseEvent {


    private final Activity newSelection;

    public ActivityPanelSelectionChangedEvent(Activity newValue){
        this.newSelection = newValue;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public Activity getNewSelection() {
        return newSelection;
    }
}
