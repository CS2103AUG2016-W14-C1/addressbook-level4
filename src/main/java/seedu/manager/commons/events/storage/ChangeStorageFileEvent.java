package seedu.manager.commons.events.storage;

import seedu.manager.commons.events.BaseEvent;


public class ChangeStorageFileEvent extends BaseEvent {

    public String file;

    public ChangeStorageFileEvent(String file) {
        this.file = file;
    }
    
    @Override
    public String toString() {
    	return file;
    }
}

