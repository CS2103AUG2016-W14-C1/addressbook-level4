package seedu.manager.testutil;

import seedu.manager.model.ActivityManager;
import seedu.manager.model.activity.Activity;

/**
 * A utility class to help with building ActivityManager objects.
 * Example usage: <br>
 *     {@code ActivityManager ab = new ActivityManagerBuilder().withActivity(new Activity("buy milk")).withTag("Friend").build();}
 */
public class ActivityManagerBuilder {

    private ActivityManager activityManager;

    public ActivityManagerBuilder(ActivityManager activityManager){
        this.activityManager = activityManager;
    }

    public ActivityManagerBuilder withActivity(Activity activity) {
        activityManager.addActivity(activity);
        return this;
    }

    public ActivityManager build(){
        return activityManager;
    }
}
