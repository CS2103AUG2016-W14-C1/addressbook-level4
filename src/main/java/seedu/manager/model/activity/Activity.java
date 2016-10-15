package seedu.manager.model.activity;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import com.joestelmach.natty.*;

public class Activity implements ReadOnlyActivity {
	public String name;
	public Status status;
	public Date dateTime;
	public Date endDateTime;
	Parser parser = new Parser();
	
	public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
	public static final String[] DAYS = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    
	public Activity(String name) {
	    this.name = name;
		this.status = new Status();
	}

	public Activity(String name, String newStartDateTime, String newEndDateTime) {
        this(name);
        List<DateGroup> startDateGroups = parser.parse(newStartDateTime);
        this.dateTime = startDateGroups.get(0).getDates().get(0);
        List<DateGroup> endDateGroups = parser.parse(newStartDateTime);
        this.dateTime = endDateGroups.get(0).getDates().get(0);
    }
	
    public Activity(String name, String newDateTime) {
        this(name);
        List<DateGroup> dateGroups = parser.parse(newDateTime);
        this.dateTime = dateGroups.get(0).getDates().get(0);
    }
	
	/**
	 * Copy constructor
	 */
	public Activity(ReadOnlyActivity source) {
	    this(source.getName());
	}
	
	@Override
	public String getName() {
	    return name;
	}
	
	@Override
    public Date getDate() {
        return dateTime;
    }
	
	@Override
    public String getMonth() {
        if (dateTime == null) {
            return "";
        } else {
            return MONTHS[dateTime.getMonth()];
        }
    }
	
	@Override
	public String getDay() {
        if (dateTime == null) {
            return "";
        } else {
            return Integer.toString(dateTime.getDate());
        }
    }
	
	@Override
    public String getDayOfWeek() {
        if (dateTime == null) {
            return "";
        } else {
            return DAYS[dateTime.getDay()];
        }
    }
	
	@Override
    public String getHour() {
        if (dateTime == null) {
            return "";
        } else {
            return Integer.toString(dateTime.getHours());
        }
    }
    
	@Override
    public String getMinutes() {
        if (dateTime == null) {
            return "";
        } else {
            return Integer.toString(dateTime.getMinutes());
        }
    }
	
	@Override
    public String getSeconds() {
        if (dateTime == null) {
            return "";
        } else {
            return Integer.toString(dateTime.getSeconds());
        }
    }
    
    
	
	
	@Override
	public void setName(String newName) {
		this.name = newName;
	}
	
	@Override
    public void setDateTime(String newDateTime) {
	    List<DateGroup> dateGroups = parser.parse(newDateTime);
        this.dateTime = dateGroups.get(0).getDates().get(0);
    }
	
	@Override
	public void setStatus(boolean completed) {
		if (completed) {
			(this.status).setCompleted();
		} else {
			(this.status).setPending();
		}
	}
	
	@Override
	public String getStatus() {
		return (this.status).toString();
	}
	
	// TODO: Re-implement equality if necessary when more details are added
	@Override
	public boolean equals(Object o) {
	    // Check for name equality
	    return o == this ||
	           (o instanceof Activity &&
	            this.name.equals(((Activity)o).name));
	}
}
