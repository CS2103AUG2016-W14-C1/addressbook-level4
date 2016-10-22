package seedu.manager.logic.parser;

import static seedu.manager.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.manager.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.manager.commons.core.Messages.MESSAGE_CANNOT_PARSE_TO_DATE;

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

    private static final Pattern ACTIVITY_DATA_ARGS_FORMAT = // '/' forward slashes are reserved for delimiter prefixes
            Pattern.compile("(?<name>[^/]+)"); // variable number of tags

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
        final Matcher matcher = ACTIVITY_DATA_ARGS_FORMAT.matcher(args.trim());
        // Validate arg string format
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        String[] eventTokens = args.trim().split(" from ");
        String[] deadlineTokens = args.trim().split(" on ");
        String[] deadlineAltTokens = args.split(" by ");
        String[] eventStrictTokens = args.trim().split(" \"from\" ");
        String[] deadlineStrictTokens = args.trim().split(" \"on\" ");
        String[] deadlineAltStrictTokens = args.split(" \"by\" ");
        
        try {
            // Perform strict token checking for events before processing normally
            if (eventStrictTokens.length == EVENT_TOKEN_COUNT) {
                final String eventName = eventStrictTokens[0].trim();
                String[] eventVeryStrictTimeTokens = eventStrictTokens[1].split(" \"to\" ");
                String[] eventStrictTimeTokens = eventStrictTokens[1].split(" to ");
                
                // Perform strict token checking for events "to" keyword before processing normally
                if (eventVeryStrictTimeTokens.length == EVENT_TOKEN_COUNT) {
                    final String dateTime = eventVeryStrictTimeTokens[0].trim();
                    final String endDateTime = eventVeryStrictTimeTokens[1].trim();
                    
                    StringUtil.validateAMDate(dateTime);
                    StringUtil.validateAMDate(endDateTime);
                    
                    return new AddCommand(eventName, dateTime, endDateTime);
                } else if (eventStrictTimeTokens.length == EVENT_TOKEN_COUNT) {
                    final String dateTime = eventStrictTimeTokens[0].trim();
                    final String endDateTime = eventStrictTimeTokens[1].trim();
                    
                    StringUtil.validateAMDate(dateTime);
                    StringUtil.validateAMDate(endDateTime);
                    
                    return new AddCommand(eventName, dateTime, endDateTime);
                } else {
                    return new AddCommand(matcher.group("name"));
                }
            } else if (eventTokens.length == EVENT_TOKEN_COUNT) {
                final String eventName = eventTokens[0].trim();
                String[] eventStrictToTimeTokens = eventTokens[1].split(" \"to\" "); 
                String[] eventTimeTokens = eventTokens[1].split(" to "); 
                
                // Perform strict token checking for events "to" keyword before processing normally
                if (eventStrictToTimeTokens.length == EVENT_TOKEN_COUNT) {
                    final String dateTime = eventStrictToTimeTokens[0].trim();
                    final String endDateTime = eventStrictToTimeTokens[1].trim();
                    
                    StringUtil.validateAMDate(dateTime);
                    StringUtil.validateAMDate(endDateTime);
                    
                    return new AddCommand(eventName, dateTime, endDateTime);
                } else if (eventTimeTokens.length == EVENT_TOKEN_COUNT) {
                    final String dateTime = eventTimeTokens[0].trim();
                    final String endDateTime = eventTimeTokens[1].trim();
                    
                    StringUtil.validateAMDate(dateTime);
                    StringUtil.validateAMDate(endDateTime);
                    
                    return new AddCommand(eventName, dateTime, endDateTime);
                } else {
                    return new AddCommand(matcher.group("name"));
                }
             // Perform strict token checking for (alt.) deadlines before processing normally
            } else if (deadlineAltStrictTokens.length == DEADLINE_TOKEN_COUNT) {
                final String deadlineName = deadlineAltStrictTokens[0].trim();
                final String dateTime = deadlineAltStrictTokens[1].trim();
                
                StringUtil.validateAMDate(dateTime);
                
                return new AddCommand(deadlineName, dateTime);
            } else if (deadlineStrictTokens.length == DEADLINE_TOKEN_COUNT) {
                final String deadlineName = deadlineStrictTokens[0].trim();
                final String dateTime = deadlineStrictTokens[1].trim();
                
                StringUtil.validateAMDate(dateTime);
                
                return new AddCommand(deadlineName, dateTime);
            } else if (deadlineAltTokens.length == DEADLINE_TOKEN_COUNT) {
                final String deadlineName = deadlineAltTokens[0].trim();
                final String dateTime = deadlineAltTokens[1].trim();
                
                StringUtil.validateAMDate(dateTime);
                
                return new AddCommand(deadlineName, dateTime);
            } else if (deadlineTokens.length == DEADLINE_TOKEN_COUNT) {
                final String deadlineName = deadlineTokens[0].trim();
                final String dateTime = deadlineTokens[1].trim();
                
                StringUtil.validateAMDate(dateTime);
                
                return new AddCommand(deadlineName, dateTime);
            } else {
                return new AddCommand(matcher.group("name"));
            }
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

    /**
     * Extracts the new person's tags from the add command's tag arguments string.
     * Merges duplicate tag strings.
     */
    private static Set<String> getTagsFromArgs(String tagArguments) throws IllegalValueException {
        // no tags
        if (tagArguments.isEmpty()) {
            return Collections.emptySet();
        }
        // replace first delimiter prefix, then split
        final Collection<String> tagStrings = Arrays.asList(tagArguments.replaceFirst(" t/", "").split(" t/"));
        return new HashSet<>(tagStrings);
    }

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
        
        String arguments = matcher.group("arguments").trim();
        String[] eventTokens = arguments.split(" from ");
        String[] deadlineTokens = arguments.split(" on ");
        String[] deadlineAltTokens = arguments.split(" by ");
        String[] eventStrictTokens = arguments.trim().split(" \"from\" ");
        String[] deadlineAltStrictTokens = arguments.trim().split(" \"by\" ");
        String[] deadlineStrictTokens = arguments.trim().split(" \"on\" ");
        
        try {
            // Perform strict token checking for events before processing normally
            if (eventStrictTokens.length == EVENT_TOKEN_COUNT) {
                final String eventName = eventStrictTokens[0].trim();
                String[] eventVeryStrictTimeTokens = eventStrictTokens[1].split(" \"to\" ");
                String[] eventStrictTimeTokens = eventStrictTokens[1].split(" to ");
                
                // Perform strict token checking for events "to" keyword before processing normally
                if (eventVeryStrictTimeTokens.length == EVENT_TOKEN_COUNT) {
                    final String dateTime = eventVeryStrictTimeTokens[0].trim();
                    final String endDateTime = eventVeryStrictTimeTokens[1].trim();
                    
                    StringUtil.validateAMDate(dateTime);
                    StringUtil.validateAMDate(endDateTime);

                    return new UpdateCommand(targetIndex, eventName, dateTime, endDateTime);
                } else if (eventStrictTimeTokens.length == EVENT_TOKEN_COUNT) {
                    final String dateTime = eventStrictTimeTokens[0].trim();
                    final String endDateTime = eventStrictTimeTokens[1].trim();
                    
                    StringUtil.validateAMDate(dateTime);
                    StringUtil.validateAMDate(endDateTime);
                    
                    return new UpdateCommand(targetIndex, eventName, dateTime, endDateTime);
                } else {
                    return new UpdateCommand(targetIndex, arguments);
                }
            } else if (eventTokens.length == EVENT_TOKEN_COUNT) {
                final String eventName = eventTokens[0].trim();
                String[] eventStrictToTimeTokens = eventTokens[1].split(" \"to\" "); 
                String[] eventTimeTokens = eventTokens[1].split(" to "); 
                
                // Perform strict token checking for events "to" keyword before processing normally
                if (eventStrictToTimeTokens.length == EVENT_TOKEN_COUNT) {
                    final String dateTime = eventStrictToTimeTokens[0].trim();
                    final String endDateTime = eventStrictToTimeTokens[1].trim();
                    
                    StringUtil.validateAMDate(dateTime);
                    StringUtil.validateAMDate(endDateTime);
                    
                    return new UpdateCommand(targetIndex, eventName, dateTime, endDateTime);
                } else if (eventTimeTokens.length == EVENT_TOKEN_COUNT) {
                    final String dateTime = eventTimeTokens[0].trim();
                    final String endDateTime = eventTimeTokens[1].trim();
                    
                    StringUtil.validateAMDate(dateTime);
                    StringUtil.validateAMDate(endDateTime);

                    return new UpdateCommand(targetIndex, eventName, dateTime, endDateTime);
                } else {
                    return new UpdateCommand(targetIndex, arguments);
                }
            // Perform strict token checking for (alt.) deadlines before processing normally
            } else if (deadlineAltStrictTokens.length == DEADLINE_TOKEN_COUNT) {
                final String deadlineName = deadlineAltStrictTokens[0].trim();
                final String dateTime = deadlineAltStrictTokens[1].trim();
                
                StringUtil.validateAMDate(dateTime);
                
                return new UpdateCommand(targetIndex, deadlineName, dateTime);  
            } else if (deadlineStrictTokens.length == DEADLINE_TOKEN_COUNT) {
                final String deadlineName = deadlineStrictTokens[0].trim();
                final String dateTime = deadlineStrictTokens[1].trim();
                
                StringUtil.validateAMDate(dateTime);
                
                return new UpdateCommand(targetIndex, deadlineName, dateTime);
            } else if (deadlineAltTokens.length == DEADLINE_TOKEN_COUNT) {
                final String deadlineName = deadlineAltTokens[0].trim();
                final String dateTime = deadlineAltTokens[1].trim();
                
                StringUtil.validateAMDate(dateTime);
                
                return new UpdateCommand(targetIndex, deadlineName, dateTime);
            } else if (deadlineTokens.length == DEADLINE_TOKEN_COUNT) {
                final String deadlineName = deadlineTokens[0].trim();
                final String dateTime = deadlineTokens[1].trim();
                
                StringUtil.validateAMDate(dateTime);
                
                return new UpdateCommand(targetIndex, deadlineName, dateTime);
            } else {
                return new UpdateCommand(targetIndex, arguments);
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
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    SearchCommand.MESSAGE_USAGE));
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
        
        if ((args.trim().toLowerCase()).equals("pending") || (args.trim().toLowerCase()).equals("completed")) {
        	searchCommand.addStatus(args.trim().toLowerCase());
        }
        
        return searchCommand;
    }
    
    private Command prepareStore(String args) {
    	assert args != null;
    	if (!args.equals("") && args.endsWith(".xml")) {
    		try {
				return new StoreCommand(args);
			} catch (IllegalValueException e) {
				return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, StoreCommand.MESSAGE_USAGE));
			}
    	}
    	return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, StoreCommand.MESSAGE_USAGE));
    }

}