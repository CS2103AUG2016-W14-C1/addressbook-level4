package seedu.manager.model.activity;

import java.util.Date;
import java.util.List;

import com.joestelmach.natty.*;

@SuppressWarnings("deprecation")
//@@author A0139797E
public class AMDate {
    
    private enum TimeUnit { DAY, WEEK, MONTH, YEAR }
    
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    
    public static final String[] DAYS = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    
    public static final String[] FULLDAYS = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    
    public static final String[] FULLMONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    
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
     * Reconstruct from epoch time to load from storage and for search
     * 
     * @param newDateTime
     */
    public AMDate(Long newEpochTime) {
        this.dateTime = new Date(newEpochTime);
    }
    
    public void setAMDate(String newDateTime) {
        List<DateGroup> dateGroups = dateTimeParser.parse(newDateTime);
        this.dateTime = dateGroups.get(0).getDates().get(0); 
    }
    
    //@@author A0135730M
    
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
            default:
                this.dateTime.setDate(this.dateTime.getDate());
                break;
        }
    }
    
    //@@author A0139797E
    
    /** dateTime accessors **/
    
    public Long getTime() {
        return dateTime.getTime();
    }
    
    public String getMonth() {
        assert dateTime != null;
        return MONTHS[dateTime.getMonth()];
    }
    
    public String getMonthFull() {
        assert dateTime != null;
        return FULLMONTHS[dateTime.getMonth()];
    }
    
    public String getDay() {
        assert dateTime != null;
        return Integer.toString(dateTime.getDate());
    }
    
    public String getDayWithExtension() {
        assert dateTime != null;
        String day = Integer.toString(dateTime.getDate());
        if (day.endsWith("1")) {
        	return day + "st";
        } else if (day.endsWith("2")) {
        	return day + "nd";
        } else if (day.endsWith("3")) {
        	return day + "rd";
        } else {
        	return day + "th";
        }
    }
    
    public String getDayOfWeek() {
        assert dateTime != null;
        return DAYS[dateTime.getDay()];
    }
    
    public String getDayOfWeekFull() {
        assert dateTime != null;
        return FULLDAYS[dateTime.getDay()];
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
