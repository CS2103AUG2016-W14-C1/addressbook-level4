package seedu.manager.logic.commands;

import java.util.Set;
import seedu.manager.model.activity.AMDate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class SearchCommand extends Command {

    public static final String COMMAND_WORD = "search";

    public static final String USAGE = "search KEYWORDS\n" + "search DATE_TIME\n" + "search STATUS";

    public static final String EXAMPLES = "search buy\n" + "search 21 Oct\n" + "search completed";   
    
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Searches all activities whose names contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final Set<String> keywords;
    private AMDate dateTime;
    private AMDate endDateTime;
    private String status;

    public SearchCommand(Set<String> keywords) {
        this.keywords = keywords;
        this.dateTime = null;
        this.endDateTime = null;
        this.status = null;
    }
    
    /**
     * Add the start/end dateTime range for search, use default end (end of the same day)
     * 
     * @param searchDateTime specified by user
     */
    public void addDateTimeRange(String searchDateTime) {
        addDateTimeRange(searchDateTime, searchDateTime);
    }
    
    /**
     * Add the start/end dateTime range for search
     * 
     * @param searchDateTime, searchEndDateTime specified by user
     */
    public void addDateTimeRange(String searchDateTime, String searchEndDateTime) {
        this.dateTime = new AMDate(searchDateTime);
        this.endDateTime = new AMDate(searchEndDateTime);
        this.dateTime.toStartOfDay();
        this.endDateTime.toEndOfDay();
    }
    
    /**
     * Add the status for search
     * 
     * @param status specified by user
     */
    public void addStatus(String status) {
    	this.status = status.toLowerCase();
    }
    
    @Override
    public CommandResult execute() {
        model.updateFilteredActivityList(keywords);
        if (this.dateTime != null && this.endDateTime != null) {
            model.updateFilteredActivityList(dateTime, endDateTime);
        }
        
        if (this.status != null) {
        	boolean isCompleted;
        	if ((this.status).equals("pending")) {
        		isCompleted = false;
        	} else {
        		isCompleted = true;
        	}    
        	model.updateFilteredActivityList(isCompleted);
        }
        	
        return new CommandResult(getMessageForActivityListShownSummary(model.getFilteredActivityList().size()));
    }

}
