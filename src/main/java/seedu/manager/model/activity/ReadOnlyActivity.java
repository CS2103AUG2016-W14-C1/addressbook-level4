package seedu.manager.model.activity;

import java.util.Date;

public interface ReadOnlyActivity {
    
    String getName();
    Date getDate();
    String getMonth();
    String getDay();
    String getDayOfWeek();
    String getHour();
    String getMinutes();
    String getSeconds();
    String getStatus();
    
    void setName(String newName);
    void setStatus(boolean completed);
    void setDateTime(String newDateTime);
}
