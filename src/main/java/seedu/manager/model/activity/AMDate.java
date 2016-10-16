package seedu.manager.model.activity;

import java.util.Date;
import java.util.List;

import com.joestelmach.natty.*;

public class AMDate {
    
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    
    public static final String[] DAYS = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    
    private Date dateTime;
    
    private Parser dateTimeParser = new Parser();
    
    /**
     * Default constructor for Activity Manager date
     */
    AMDate(String newDateTime) {
        List<DateGroup> dateGroups = dateTimeParser.parse(newDateTime);
        this.dateTime = dateGroups.get(0).getDates().get(0); 
    }
    
    /**
     * Reconstruct from epoch time to load from storage
     * 
     * @param newDateTime
     */
    AMDate(Long newEpochTime) {
        this.dateTime = new Date(newEpochTime);
    }
    
    public void setAMDate(String newDateTime) {
        List<DateGroup> dateGroups = dateTimeParser.parse(newDateTime);
        this.dateTime = dateGroups.get(0).getDates().get(0); 
    }
    
    public Long getTime() {
        return dateTime.getTime();
    }
    
    public String getMonth() {
        assert dateTime != null;
        return MONTHS[dateTime.getMonth()];
    }
    
    public String getDay() {
        assert dateTime != null;
        return Integer.toString(dateTime.getDate());
    }
    
    public String getDayOfWeek() {
        assert dateTime != null;
        return DAYS[dateTime.getDay()];
    }
    
    public String getHour() {
        assert dateTime != null;
        return Integer.toString(dateTime.getHours());
    }
    
    public String getMinutes() {
        assert dateTime != null;
        if (dateTime.getMinutes() < 10) {
            return "0" + Integer.toString(dateTime.getMinutes());
        } else {
            return Integer.toString(dateTime.getMinutes());
        }
    }
    
    public String getSeconds() {
        assert dateTime != null;
        return Integer.toString(dateTime.getSeconds());
    }
    
}
