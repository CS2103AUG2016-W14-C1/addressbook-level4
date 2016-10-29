package seedu.manager.storage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.manager.commons.exceptions.IllegalValueException;
import seedu.manager.model.ReadOnlyActivityManager;
import seedu.manager.model.activity.Activity;
import seedu.manager.model.activity.ActivityList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An Immutable ActivityManager that is serializable to XML format
 */
@XmlRootElement(name = "activitymanager")
public class XmlSerializableActivityManager implements ReadOnlyActivityManager {

    @XmlElement(name = "activity")
    private List<XmlAdaptedActivity> activities;
    
    {
        activities = new ArrayList<>();
    }

    /**
     * Empty constructor required for marshalling
     */
    public XmlSerializableActivityManager() {}

    /**
     * Conversion
     */
    public XmlSerializableActivityManager(ReadOnlyActivityManager src) {
        activities.addAll(src.getListActivity().stream().map(XmlAdaptedActivity::new).collect(Collectors.toList()));
    }

    @Override
    public ActivityList getActivityList() {
        ActivityList lists = new ActivityList();
        for (XmlAdaptedActivity p : activities) {
            lists.add(p.toModelType());
        }
        return lists;
    }

    @Override
    public List<Activity> getListActivity() {
        return activities.stream().map(p -> {
            return p.toModelType();
        }).collect(Collectors.toCollection(ArrayList::new));
    }
}
