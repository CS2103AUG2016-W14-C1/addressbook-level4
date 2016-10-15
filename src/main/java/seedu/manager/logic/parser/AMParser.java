package seedu.manager.logic.parser;

import static seedu.manager.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.manager.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

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
    private static final int ADD_DEADLINE_TOKEN_COUNT = 2;
    
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

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case SearchCommand.COMMAND_WORD:
            return prepareSearch(arguments);

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
     * Parses arguments in the context of the add person command.
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
        String[] deadline_tokens = args.trim().split("on");
        String[] events_tokens = args.trim().split("from");
        
        try {
            if (deadline_tokens.length == ADD_DEADLINE_TOKEN_COUNT) {
                return new AddCommand(deadline_tokens[0], deadline_tokens[1]);
            }
            return new AddCommand(matcher.group("name"));
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
     * Parses arguments in the context of the delete person command.
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
        
        return new UpdateCommand(index.get(), matcher.group("arguments").trim());
    }

    /**
     * Parses arguments in the context of the mark activity command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    
    private Command prepareMark(String args) {
    	final Matcher matcher = ACTIVITY_INDEX_ARGS_FORMAT.matcher(args.trim());
    	
        // Validate arg string format
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
        }
        
        // Validate index format
        Optional<Integer> index = parseIndex(matcher.group("targetIndex"));
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
        }
        
        String[] splitArgs = (matcher.group("arguments").trim()).split("as");
        String wordStatus;
        if (splitArgs.length == 1) {
        	wordStatus = (splitArgs[0]).trim();
        } else {
        	wordStatus = (splitArgs[1]).trim();
        }
        
        String lowerCaseArgs = wordStatus.toLowerCase();
        boolean status;
        if (lowerCaseArgs.equals("pending")) {
        	status = false;
        } else if (lowerCaseArgs.equals("completed")) {
        	status = true;
        } else {
        	return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
        }
        
        return new MarkCommand(index.get(), status);
    }
    

    /**
     * Parses arguments in the context of the select person command.
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
     * Parses arguments in the context of the find person command.
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
        return new SearchCommand(keywordSet);
    }

}