package seedu.manager.model.activity;

import java.util.List;
import java.util.Objects;
import com.joestelmach.natty.*;

public class Activity implements ReadOnlyActivity {
	public String name;
	public Status status;
	Parser parser = new Parser();
	
	
	public Activity(String name) {
		this.name = name;
		this.status = new Status();
	}

	
    public Activity(String name, String date_time) {
        this(name);
        List<DateGroup> dateGroups = parser.parse(date_time);
        for (DateGroup dateGroup : dateGroups) {
            System.out.println(dateGroup.getLine());
        }
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
	public void setName(String newName) {
		this.name = newName;
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
