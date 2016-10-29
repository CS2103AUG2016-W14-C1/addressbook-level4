//@@author A0144704L
package seedu.manager.commons.events.ui;

import seedu.manager.commons.events.BaseEvent;

/**
 * Represents a selection change in the Activity List Panel
 */
public class ChangeStorageFileDisplayEvent extends BaseEvent {


    public String file;

    public ChangeStorageFileDisplayEvent(String file){
        this.file = file;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
