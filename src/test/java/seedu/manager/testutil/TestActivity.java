package seedu.manager.testutil;

import seedu.manager.model.activity.*;
import seedu.manager.model.tag.UniqueTagList;

/**
 * A mutable person object. For testing only.
 */
public class TestActivity implements ReadOnlyActivity {

    private String name;
    private AMDate dateTime;
    private AMDate endDateTime;
    private UniqueTagList tags;

    public TestActivity() {
        tags = new UniqueTagList();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public UniqueTagList getTags() {
        return tags;
    }
    
    @Override
    public String toString() {
        return this.getName();
    }

    public String getAddCommand() {
        StringBuilder sb = new StringBuilder();
        sb.append("add " + this.getName());
        // TODO: Re-implement tags when possible
        // this.getTags().getInternalList().stream().forEach(s -> sb.append("t/" + s.tagName + " "));
        return sb.toString();
    }

    @Override
    public void setStatus(boolean completed) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getStatus() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AMDate getDate() {
        return dateTime;
    }
    
    @Override
    public AMDate getEndDate() {
        return endDateTime;
    }

    @Override
    public void setDateTime(String newDateTime) {
        this.dateTime.setAMDate(newDateTime);
    }

    @Override
    public void setEndDateTime(String newEndDateTime) {
        this.dateTime.setAMDate(newEndDateTime);    
    }
}
