package seedu.manager.model.activity;

import java.util.Date;
import java.util.List;

import com.joestelmach.natty.*;

@SuppressWarnings("deprecation")
public class AMDate {
    
    private enum TimeUnit { DAY, WEEK, MONTH, YEAR }
    
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    
    public static final String[] DAYS = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    
    private Date dateTime;
    
    private Parser dateTimeParser = new Parser();
    
    /**
     * Default constructor for Activity Manager date
     */
    public AMDate(String newDateTime) {
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
    
    /** dateTime manipulation methods **/
    
    public void toStartOfDay() {
        this.dateTime.setHours(0);
        this.dateTime.setMinutes(0);
        this.dateTime.setSeconds(0);
    }
    
    public void toEndOfDay() {
        this.dateTime.setHours(23);
        this.dateTime.setMinutes(59);
        this.dateTime.setSeconds(59);
    }
    
    /**
     * Adds offset number of time unit to dateTime
     */
    public void addOffset(int offset, String unit) {
        TimeUnit timeUnit = TimeUnit.valueOf(unit.toUpperCase());
        switch (timeUnit) {
        
        case DAY:
            this.dateTime.setDate(this.dateTime.getDate() + offset);
            return;
        case WEEK:
            this.dateTime.setDate(this.dateTime.getDate() + (offset * 7));
            return;
        case MONTH:
            this.dateTime.setMonth(this.dateTime.getMonth() + offset);
            return;
        case YEAR:
            this.dateTime.setYear(this.dateTime.getYear() + offset);
            return;
        }
    }
    
    /** dateTime accessors **/
    
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
    
    @Override
    public boolean equals(Object o) {
        return o == this
               || (o instanceof AMDate 
                  && this.getTime().equals(((AMDate)o).getTime()));
    }
    
    @Override
    public String toString() {
        return dateTime.toString();
    }
    
}
