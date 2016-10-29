package seedu.manager.storage;

import javax.xml.bind.annotation.XmlElement;

import seedu.manager.commons.exceptions.IllegalValueException;
import seedu.manager.model.activity.*;

import java.util.ArrayList;
import java.util.List;

/**
 * JAXB-friendly version of an Activity.
 */
public class XmlAdaptedActivity {

    @XmlElement(required = true)
    private ActivityType type;
    
    @XmlElement(required = true)
    private String name;

    @XmlElement(required = false)
    private Long epochDateTime;
    
    @XmlElement(required = false)
    private Long epochEndDateTime;
    
    @XmlElement(required = true)
    private boolean isCompleted;
    
    // @XmlElement
    // private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * No-arg constructor for JAXB use.
     */
    public XmlAdaptedActivity() {}


    /**
     * Converts a given Activity into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedActivity
     */
    //@@author A0135730M
    public XmlAdaptedActivity(Activity source) {
        type = source.getType();
        name = source.getName();
        isCompleted = source.getStatus().isCompleted();
        
        if (type.equals(ActivityType.DEADLINE)) {
            epochDateTime = source.getDateTime().getTime();
        } if (type.equals(ActivityType.EVENT)) {
            epochDateTime = source.getDateTime().getTime();
            epochEndDateTime = source.getEndDateTime().getTime();
        }
    }

    /**
     * Converts this jaxb-friendly adapted activity object into the model's Activity object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted activity
     */
    //@@author A0135730M
    public Activity toModelType() {
        Activity newActivity;
        if (type.equals(ActivityType.EVENT)) {
            newActivity = new Activity(this.name, epochDateTime, epochEndDateTime);
        } else if(type.equals(ActivityType.DEADLINE)) {
            newActivity = new Activity(this.name, epochDateTime);    
        } else {
            newActivity = new Activity(this.name);
        }
    	newActivity.setStatus(this.isCompleted);
    	
        return newActivity;
    }
}
