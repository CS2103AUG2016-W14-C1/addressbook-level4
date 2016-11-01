package seedu.manager.logic.commands;

import java.util.List;
import java.util.Set;

import seedu.manager.commons.events.ui.ActivityListPanelUpdateEvent;
import seedu.manager.model.activity.AMDate;

/**
 * Finds and lists all activities in acitvity manager whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
//@@author A0135730M
public class SearchCommand extends Command {

    private enum SearchType { KEYWORDS, STATUS, DATE };
    
    public static final String COMMAND_WORD = "search";

    public static final String USAGE = "search \"KEYWORDS\"\n" + "search DATE_TIME\n" + "search STATUS";

    public static final String EXAMPLES = "search \"buy\"\n" + "search 21 Oct\n" + "search completed";   
    
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Searches all activities whose names contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " \"alice bob charlie\"";

    private SearchType type;
    private Set<String> keywords;
    private AMDate dateTime;
    private AMDate endDateTime;
    private String status;

    /**
     * Constructor to search by keywords
     */
    public SearchCommand(Set<String> keywords) {
        this.type = SearchType.KEYWORDS;
        this.keywords = keywords;
    }
    
    /**
     * Constructor to search by status
     * @param status either "completed" or "pending"
     */
    public SearchCommand(String status) {
        this.type = SearchType.STATUS;
        this.status = status;
    }
    
    /**
     * Constructor to search by date ranges
     * @param ranges contains exactly a start and end date
     */
    public SearchCommand(List<Long> ranges) {
        assert ranges.size() == 2;
        this.type = SearchType.DATE;
        this.dateTime = new AMDate(ranges.get(0));
        this.endDateTime = new AMDate(ranges.get(1));
        
        // expand range from start of dateTime to end of endDateTime
        this.dateTime.toStartOfDay();
        this.endDateTime.toEndOfDay();
    }
    
    @Override
    public CommandResult execute() {
        switch (this.type) {
        case KEYWORDS:
            assert keywords != null;
            model.updateFilteredActivityList(keywords);
            break;
        case STATUS:
            assert status != null;
            boolean isCompleted = "completed".equals(status);
            model.updateFilteredActivityList(isCompleted);
            break;
        case DATE:
            assert dateTime != null;
            assert endDateTime != null;
            model.updateFilteredActivityList(dateTime, endDateTime);
            break;
        }
        model.indicateActivityListPanelUpdate();	
        return new CommandResult(getMessageForActivityListShownSummary(model.getFilteredActivityList().size()));
    }

}
