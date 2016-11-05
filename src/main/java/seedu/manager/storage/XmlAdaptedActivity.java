package seedu.manager.storage;

import javax.xml.bind.annotation.XmlElement;
import seedu.manager.model.activity.*;

//@@author A0135730M
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

    /**
     * No-arg constructor for JAXB use.
     */
    public XmlAdaptedActivity() {}

    /**
     * Converts a given Activity into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedActivity
     */
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
     * @return model's Activity object converted from jaxb
     */
    public Activity toModelType() {
        Activity newActivity = new Activity(this.name);
        if (type.equals(ActivityType.EVENT)) {
            newActivity.setDateTime(this.epochDateTime);
            newActivity.setEndDateTime(this.epochEndDateTime);
        } else if (type.equals(ActivityType.DEADLINE)) {
            newActivity.setDateTime(this.epochDateTime); 
        } 
    	newActivity.setStatus(this.isCompleted);
    	
        return newActivity;
    }
}
