package seedu.manager.logic.parser;

import static seedu.manager.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.manager.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.manager.commons.core.Messages.MESSAGE_RECUR_OUT_OF_RANGE;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.manager.commons.exceptions.IllegalValueException;
import seedu.manager.commons.util.StringUtil;
import seedu.manager.logic.commands.*;
import seedu.manager.model.activity.ActivityType;

/**
 * Parses user input.
 */
public class AMParser {
    //@@author A0135730M
    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private static final Pattern ACTIVITY_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>\\S+)(?<arguments>.*)");
    
    private static final Pattern KEYWORDS_ARGS_FORMAT =
            Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one or more keywords separated by whitespace
    
    private static final Pattern EVENT_RECURRING_ARGS_FORMAT =
            Pattern.compile("^(?<name>.+) ((\"?)(from)\\3) (?<date>.+) ((\"?)(to)\\7) (?<endDate>.+) ((\"?)for\\11) (?<num>\\d+) (?<unit>(day|week|month|year))(s?)$",
                    Pattern.CASE_INSENSITIVE);
    
    private static final Pattern ADD_EVENT_ARGS_FORMAT =
            Pattern.compile("^(?<name>.+) ((\"?)(from)\\3) (?<date>.+) ((\"?)(to)\\7) (?<endDate>.+)$",
                    Pattern.CASE_INSENSITIVE);
    
    private static final Pattern UPDATE_EVENT_ARGS_FORMAT =
            Pattern.compile("^(?<name>.+ )?((\"?)(from)\\3) (?<date>.+) ((\"?)(to)\\7) (?<endDate>.+)$",
                    Pattern.CASE_INSENSITIVE);

    private static final Pattern DEADLINE_RECURRING_ARGS_FORMAT =
            Pattern.compile("^(?<name>.+) ((\"?)(on|by)\\3) (?<date>.+) ((\"?)for\\7) (?<num>\\d+) (?<unit>(day|week|month|year))(s?)$",
                    Pattern.CASE_INSENSITIVE);
    
    private static final Pattern ADD_DEADLINE_ARGS_FORMAT =
            Pattern.compile("^(?<name>.+) ((\"?)(on|by)\\3) (?<date>.+)$", 
                    Pattern.CASE_INSENSITIVE);
    
    private static final Pattern UPDATE_DEADLINE_ARGS_FORMAT =
            Pattern.compile("^(?<name>.+ )?((\"?)(on|by)\\3) (?<date>.+)$", 
                    Pattern.CASE_INSENSITIVE);
    
    private static final Pattern FLOATING_ARGS_FORMAT = // '/' forward slashes are reserved for delimiter prefixes
            Pattern.compile("^(?<name>[^/]+)$"); // variable number of tags
    
    //@@author A0144881Y
    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord").toLowerCase();
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return prepareAdd(arguments);

        case DeleteCommand.COMMAND_WORD:
            return prepareDelete(arguments);
            
        case UpdateCommand.COMMAND_WORD:
            return prepareUpdate(arguments);
            
        case UndoCommand.COMMAND_WORD:
            return prepareUndo(arguments);
            
        case RedoCommand.COMMAND_WORD:
        	return prepareRedo(arguments);
            
        case MarkCommand.COMMAND_WORD:
            return prepareMark(arguments);
            
        case UnmarkCommand.COMMAND_WORD:
            return prepareUnmark(arguments);

        case StoreCommand.COMMAND_WORD:
            return prepareStore(arguments);
        	
        case SearchCommand.COMMAND_WORD:
            return prepareSearch(arguments);
            
        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    //@@author A0135730M
    /**
     * Parses arguments in the context of the add activity command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareAdd(String args){
        
        final Pattern[] addCommandPatterns = { EVENT_RECURRING_ARGS_FORMAT, ADD_EVENT_ARGS_FORMAT, 
                DEADLINE_RECURRING_ARGS_FORMAT, ADD_DEADLINE_ARGS_FORMAT, FLOATING_ARGS_FORMAT };
        try {
            // find the right matcher and parse args accordingly
            for (Pattern pattern : addCommandPatterns) {
                final Matcher matcher = pattern.matcher(args.trim());
                if (matcher.matches()) {
                    return prepareAddByGroupingMatcher(matcher);
                }
            }
            // unable to find matcher which matches add command, throw exception
            throw new IllegalValueException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }

    }

    /**
     * Builds Command based on args in matcher
     * 
     * @param matcher which matches the command format
     * @return commandToReturn with correct properties
     * @throws IllegalValueException if date or recurNum is not valid
     */
    private Command prepareAddByGroupingMatcher(Matcher matcher) throws IllegalValueException {
        String regex = matcher.pattern().pattern();
        final String name = matcher.group("name").trim();
        AddCommand commandToReturn = new AddCommand(name);
        
        // add event properties
        if (regex.contains("date") && regex.contains("endDate")) {
            final String date = matcher.group("date").trim();
            final String endDate = matcher.group("endDate").trim();
            
            StringUtil.validateAMDate(date, endDate);
            
            commandToReturn.setType(ActivityType.EVENT);
            commandToReturn.setDateTime(date);
            commandToReturn.setEndDateTime(endDate);
        // add deadline properties    
        } else if (regex.contains("date")) {
            final String date = matcher.group("date").trim();
            
            StringUtil.validateAMDate(date);
            
            commandToReturn.setType(ActivityType.DEADLINE);
            commandToReturn.setDateTime(date);
        }
        
        // add recurring properties (if any)
        if (regex.contains("num") && regex.contains("unit")) {
            final int recurNum = Integer.parseInt(matcher.group("num").trim());
            final String recurUnit = matcher.group("unit").trim();
            
            validateRecurNumber(recurNum);
            
            commandToReturn.setRecurring(recurNum, recurUnit);
        }
        
        return commandToReturn;
    }

    /**
     * Validates recur number is between 1 and 30
     * 
     * @param num recurring number
     * @throws IllegalValueException if out of range
     */
    private void validateRecurNumber(int num) throws IllegalValueException {
        if (num < 1 || num > 30) {
            throw new IllegalValueException(MESSAGE_RECUR_OUT_OF_RANGE);
        }
    }

    //@@author A0144881Y
    /**
     * Parses arguments in the context of the delete activity command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareDelete(String args) {

        String[] argsSeperated = args.trim().split(" ");
        ArrayList<Integer> argIndexes = new ArrayList<Integer>();
        for (int i = 0; i < argsSeperated.length; i++) {
            Optional<Integer> index = parseIndex(argsSeperated[i]);
            if(!index.isPresent()){
                return new IncorrectCommand(
                	String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            } else {
                argIndexes.add(index.get());
            }
        }
        
        return new DeleteCommand(argIndexes);
    }
    
    //@@author A0135730M
    /**
     * Parses arguments in the context of the update activity command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareUpdate(String args) {
        final Matcher matcher = ACTIVITY_INDEX_ARGS_FORMAT.matcher(args.trim());
        // Validate arg string format
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UpdateCommand.MESSAGE_USAGE));
        }
        
        // Validate index format
        Optional<Integer> index = parseIndex(matcher.group("targetIndex"));
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UpdateCommand.MESSAGE_USAGE));
        }
        final Integer targetIndex = index.get();
        
        // compare with different activity types format and return UpdateCommand accordingly
        String arguments = matcher.group("arguments").trim();
        final Matcher eventMatcher = UPDATE_EVENT_ARGS_FORMAT.matcher(arguments.trim());
        final Matcher deadlineMatcher = UPDATE_DEADLINE_ARGS_FORMAT.matcher(arguments.trim());
        final Matcher floatingMatcher = FLOATING_ARGS_FORMAT.matcher(arguments.trim());
        
        try {
            if (eventMatcher.matches()) {
                final String eventName = (eventMatcher.group("name") == null) ? null : eventMatcher.group("name").trim();
                final String eventDate = eventMatcher.group("date").trim();
                final String eventEndDate = eventMatcher.group("endDate").trim();
                
                StringUtil.validateAMDate(eventDate, eventEndDate);
                
                return new UpdateCommand(targetIndex, eventName, eventDate, eventEndDate);
            } else if (deadlineMatcher.matches()) {
                final String deadlineName = (deadlineMatcher.group("name") == null) ? null : deadlineMatcher.group("name").trim();
                final String deadlineDate = deadlineMatcher.group("date").trim();
                
                StringUtil.validateAMDate(deadlineDate);
                
                return new UpdateCommand(targetIndex, deadlineName, deadlineDate);
            } else if (floatingMatcher.matches()) {
                final String floatingName = floatingMatcher.group("name").trim();
                
                return new UpdateCommand(targetIndex, floatingName);
            } else {
                throw new IllegalValueException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UpdateCommand.MESSAGE_USAGE));
            }
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

    //@@author A0144704L
    /**
     * Parses arguments in the context of the mark activity command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareMark(String args) {
        // Validate index format
        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
				String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
        }

        return new MarkCommand(index.get());
    }
    
    /**
     * Parses arguments in the context of the unmark activity command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareUnmark(String args) {
        // Validate index format
        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
				String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));
        }

        return new UnmarkCommand(index.get());
    }
    
    //@@author A0139797E
    /**
     * Parses arguments in the context of the undo command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareUndo(String args) {
        // Validate index format
        Optional<Integer> index = parseIndex(args);
        if(index.isPresent()){
            return new UndoCommand(index.get());
        } else {
            return new UndoCommand();
        }
    }
    
    //@@author A0144881Y
    /**
     * Parses arguments in the context of the redo command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareRedo(String args) {
        // Validate index format
        Optional<Integer> index = parseIndex(args);
        if(index.isPresent()){
            return new RedoCommand(index.get());
        } else {
            return new RedoCommand();
        }
    }
    
    /**
     * Returns the specified index in the {@code command} IF a positive unsigned integer is given as the index.
     *   Returns an {@code Optional.empty()} otherwise.
     */
    private Optional<Integer> parseIndex(String command) {
        final Matcher matcher = ACTIVITY_INDEX_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String index = matcher.group("targetIndex");
        if(!StringUtil.isUnsignedInteger(index)){
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(index));

    }

    //@@author A0135730M
    /**
     * Parses arguments in the context of the search command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareSearch(String args) {
        args = args.trim();
        // search by keywords if args is wrapped with quotation marks
        if (StringUtil.hasQuotationMarks(args)) {
            return prepareSearchByKeywords(StringUtil.trimQuotationMarks(args));
        // search by status if args contains status
        } else if (StringUtil.hasStatus(args)) {
            return prepareSearchByStatus(StringUtil.getStatus(args));
        // search by dates if args contains dates    
        } else if (StringUtil.hasAMDates(args)) {
            return prepareSearchByDate(StringUtil.getDateRange(args));
        } else {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
        }
    }
    
    /**
     * Parses arguments in the context of the search by keywords command.
     *
     * @param keywordsArg the keywords portion of args
     * @return the prepared command
     */
    private Command prepareSearchByKeywords(String keywordsArg) {
        // keywords delimited by whitespace
        final String[] keywords = keywordsArg.split("\\s+");
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        return new SearchCommand(keywordSet);
    }
    
    /**
     * Parses arguments in the context of the search by status command.
     *
     * @param status either "pending" or "completed"
     * @return the prepared command
     */
    private Command prepareSearchByStatus(String status) {
        return new SearchCommand(status);
    }
    
    /**
     * Parses arguments in the context of the search by date command.
     *
     * @param range a list of dates in epoch time
     * @return the prepared command
     */
    private Command prepareSearchByDate(List<Long> range) {
        return new SearchCommand(range);
    }
    
    //@@author A0144704L
    /**
     * Parses arguments in the context of the store command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareStore(String args) {
    	assert args != null;
    	if (!"".equals(args.trim()) && args.endsWith(".xml")) {
			return new StoreCommand(args.trim());
    	}
    	return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, StoreCommand.MESSAGE_USAGE));
    }
}
