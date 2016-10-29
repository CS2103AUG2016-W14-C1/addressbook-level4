package seedu.manager.model;


import java.util.List;

import seedu.manager.model.activity.Activity;
import seedu.manager.model.activity.ActivityList;
import seedu.manager.model.tag.Tag;
import seedu.manager.model.tag.UniqueTagList;

/**
 * Unmodifiable view of an activity manager
 */
public interface ReadOnlyActivityManager {

    UniqueTagList getUniqueTagList();

    ActivityList getActivityList();

    /**
     * Returns an unmodifiable view of activities list
     */
    List<Activity> getListActivity();

    /**
     * Returns an unmodifiable view of tags list
     */
    List<Tag> getTagList();

}
