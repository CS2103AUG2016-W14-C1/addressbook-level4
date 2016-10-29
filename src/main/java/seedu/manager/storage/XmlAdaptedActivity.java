package seedu.manager.storage;

import javax.xml.bind.annotation.XmlElement;

import seedu.manager.commons.exceptions.IllegalValueException;
import seedu.manager.model.activity.*;
import seedu.manager.model.tag.Tag;

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
        // TODO: implement other required fields if necessary
//        phone = source.getPhone().value;
//        email = source.getEmail().value;
//        address = source.getAddress().value;
//        tagged = new ArrayList<>();
//        for (Tag tag : source.getTags()) {
//            tagged.add(new XmlAdaptedTag(tag));
//        }
    }

    /**
     * Converts this jaxb-friendly adapted activity object into the model's Activity object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted activity
     */
    //@@author A0135730M
    public Activity toModelType() {
//        final List<Tag> activityTags = new ArrayList<>();
//        for (XmlAdaptedTag tag : tagged) {
//            activityTags.add(tag.toModelType());
//        }
        // TODO: implement for other fields if necessary
//        final Name name = new Name(this.name);
//        final Phone phone = new Phone(this.phone);
//        final Email email = new Email(this.email);
//        final Address address = new Address(this.address);
//        final UniqueTagList tags = new UniqueTagList(personTags);
        
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
