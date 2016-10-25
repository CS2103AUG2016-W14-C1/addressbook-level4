package seedu.manager.logic.parser;

import static seedu.manager.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.manager.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.manager.commons.core.Messages.MESSAGE_RECUR_NOT_POSITIVE;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.manager.commons.exceptions.IllegalValueException;
import seedu.manager.commons.util.StringUtil;
import seedu.manager.logic.commands.*;

/**
 * Parses user input.
 */
public class AMParser {

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

    /**
     * Various token counts
     */
    private static final int DEADLINE_TOKEN_COUNT = 2;
    private static final int EVENT_TOKEN_COUNT = 2;
    private static final int SEARCH_RANGE_TOKEN_COUNT = 2;
    
    public AMParser() {}

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

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return prepareAdd(arguments);

        case SelectCommand.COMMAND_WORD:
            return prepareSelect(arguments);

        case DeleteCommand.COMMAND_WORD:
            return prepareDelete(arguments);
            
        case UpdateCommand.COMMAND_WORD:
            return prepareUpdate(arguments);
            
        case UndoCommand.COMMAND_WORD:
            return prepareUndo(arguments);
            
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

    /**
     * Parses arguments in the context of the add activity command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareAdd(String args){
        // compare with different activity types format and return AddCommand accordingly
        final Matcher eventRecurringMatcher = EVENT_RECURRING_ARGS_FORMAT.matcher(args.trim());
        final Matcher eventMatcher = ADD_EVENT_ARGS_FORMAT.matcher(args.trim());
        final Matcher deadlineRecurringMatcher = DEADLINE_RECURRING_ARGS_FORMAT.matcher(args.trim());
        final Matcher deadlineMatcher = ADD_DEADLINE_ARGS_FORMAT.matcher(args.trim());
        final Matcher floatingMatcher = FLOATING_ARGS_FORMAT.matcher(args.trim());
        
        try {
            if (eventRecurringMatcher.matches()) {
                final String eventName = eventRecurringMatcher.group("name").trim();
                final String eventDate = eventRecurringMatcher.group("date").trim();
                final String eventEndDate = eventRecurringMatcher.group("endDate").trim();
                final int eventRecurNumber = Integer.parseInt(eventRecurringMatcher.group("num").trim());
                final String eventRecurUnit = eventRecurringMatcher.group("unit").trim();
                
                StringUtil.validateAMDate(eventDate, eventEndDate);
                validateRecurNumber(eventRecurNumber);
                
                return new AddCommand(eventName, eventDate, eventEndDate, eventRecurNumber, eventRecurUnit);
            } else if (eventMatcher.matches()) {
                final String eventName = eventMatcher.group("name").trim();
                final String eventDate = eventMatcher.group("date").trim();
                final String eventEndDate = eventMatcher.group("endDate").trim();
                
                StringUtil.validateAMDate(eventDate, eventEndDate);
                
                return new AddCommand(eventName, eventDate, eventEndDate);
            } else if (deadlineRecurringMatcher.matches()) {
                final String deadlineName = deadlineRecurringMatcher.group("name").trim();
                final String deadlineDate = deadlineRecurringMatcher.group("date").trim();
                final int deadlineRecurNumber = Integer.parseInt(deadlineRecurringMatcher.group("num").trim());
                final String deadlineRecurUnit = deadlineRecurringMatcher.group("unit").trim();
                
                StringUtil.validateAMDate(deadlineDate);
                validateRecurNumber(deadlineRecurNumber);
                
                return new AddCommand(deadlineName, deadlineDate, deadlineRecurNumber, deadlineRecurUnit);
            } else if (deadlineMatcher.matches()) {
                final String deadlineName = deadlineMatcher.group("name").trim();
                final String deadlineDate = deadlineMatcher.group("date").trim();
                
                StringUtil.validateAMDate(deadlineDate);
                
                return new AddCommand(deadlineName, deadlineDate);
            } else if (floatingMatcher.matches()) {
                final String floatingName = floatingMatcher.group("name").trim();
                
                return new AddCommand(floatingName);
            } else {
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
            }
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

    private void validateRecurNumber(int num) throws IllegalValueException {
        if (num <= 0) {
            throw new IllegalValueException(MESSAGE_RECUR_NOT_POSITIVE);
        }
    }

    /**
     * Extracts the new person's tags from the add command's tag arguments string.
     * Merges duplicate tag strings.
     */
    // TODO: remove if tags not used in the end
//    private static Set<String> getTagsFromArgs(String tagArguments) throws IllegalValueException {
//        // no tags
//        if (tagArguments.isEmpty()) {
//            return Collections.emptySet();
//        }
//        // replace first delimiter prefix, then split
//        final Collection<String> tagStrings = Arrays.asList(tagArguments.replaceFirst(" t/", "").split(" t/"));
//        return new HashSet<>(tagStrings);
//    }

    /**
     * Parses arguments in the context of the delete activity command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareDelete(String args) {

        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
            	String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        return new DeleteCommand(index.get());
    }
    
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
    
    /**
     * Parses arguments in the context of the select activity command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareSelect(String args) {
        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
				String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));
        }

        return new SelectCommand(index.get());
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

    /**
     * Parses arguments in the context of the search command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareSearch(String args) {
        final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
        }
        
        // keywords delimited by whitespace
        final String[] keywords = matcher.group("keywords").split("\\s+");
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        SearchCommand searchCommand = new SearchCommand(keywordSet);
        
        // add dateTime range if dateTime is indicated in part of search
        if (StringUtil.isAMDate(args.trim())) {
            String[] searchTimeTokens = args.trim().split(" to ");
            if (searchTimeTokens.length == SEARCH_RANGE_TOKEN_COUNT) {
                searchCommand.addDateTimeRange(searchTimeTokens[0].trim(), searchTimeTokens[1].trim());
            } else {
                searchCommand.addDateTimeRange(searchTimeTokens[0].trim());
            }
        }
        
        if ("pending".equals(args.trim().toLowerCase()) || "completed".equals(args.trim().toLowerCase())) {
        	searchCommand.addStatus(args.trim().toLowerCase());
        }
        
        return searchCommand;
    }
    
    private Command prepareStore(String args) {
    	assert args != null;
    	if (!args.equals("") && args.endsWith(".xml")) {
			return new StoreCommand(args);
    	}
    	return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, StoreCommand.MESSAGE_USAGE));
    }
}
