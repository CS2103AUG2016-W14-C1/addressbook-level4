package seedu.manager.model;


import java.util.List;

import seedu.manager.model.activity.Activity;
import seedu.manager.model.activity.ActivityList;

/**
 * Unmodifiable view of an activity manager
 */
public interface ReadOnlyActivityManager {

        ActivityList getActivityList();

    /**
     * Returns an unmodifiable view of activities list
     */
    List<Activity> getListActivity();
}
