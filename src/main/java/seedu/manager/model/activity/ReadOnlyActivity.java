package seedu.manager.model.activity;

public interface ReadOnlyActivity {
    
    String getName();
    
    AMDate getDate();
    AMDate getEndDate();
    
    void setName(String newName);
    void setStatus(boolean completed);
    String getStatus();
    
    void setDateTime(String newDateTime);
    void setEndDateTime(String newEndDateTime);
}
