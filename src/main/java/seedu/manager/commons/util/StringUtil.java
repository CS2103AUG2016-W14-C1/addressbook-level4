package seedu.manager.commons.util;

import static seedu.manager.commons.core.Messages.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.joestelmach.natty.*;

import seedu.manager.commons.exceptions.IllegalValueException;

/**
 * Helper functions for handling strings.
 */
public class StringUtil {
    
    public static boolean containsIgnoreCase(String source, String query) {
        String[] split = source.toLowerCase().split("\\s+");
        List<String> strings = Arrays.asList(split);
        return strings.stream().filter(s -> s.equals(query.toLowerCase())).count() > 0;
    }

    /**
     * Returns a detailed message of the t, including the stack trace.
     */
    public static String getDetails(Throwable t){
        assert t != null;
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    /**
     * Returns true if s represents an unsigned integer e.g. 1, 2, 3, ... <br>
     *   Will return false for null, empty string, "-1", "0", "+1", and " 2 " (untrimmed) "3 0" (contains whitespace).
     * @param s Should be trimmed.
     */
    public static boolean isUnsignedInteger(String s){
        return s != null && s.matches("^0*[1-9]\\d*$");
    }
    
    //@@author A0135730M
    /**
     * Attempts to validate an AMDate type
     * @param dateTime input string to be validated
     * 
     * @throws IllegalValueException if cannot parse to date
     */
    public static void validateAMDate(String dateTime) throws IllegalValueException {
        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse(dateTime);
        if (groups.size() <= 0) {
            throw new IllegalValueException(String.format(MESSAGE_CANNOT_PARSE_TO_DATE, dateTime));
        }
    }
    
    /**
     * Attempts to validate two AMDate objects
     * @param dateTime input string to be validated
     * @param endDateTime input string to be validated
     * 
     * @throws IllegalValueException if cannot parse to date, or end earlier than start
     */
    public static void validateAMDate(String dateTime, String endDateTime) throws IllegalValueException {
        Parser parser = new Parser();
        
        // check that both inputs can be parsed to Date
        List<DateGroup> groups = parser.parse(dateTime);
        if (groups.size() <= 0) {
            throw new IllegalValueException(String.format(MESSAGE_CANNOT_PARSE_TO_DATE, dateTime));
        }
        List<DateGroup> endGroups = parser.parse(endDateTime);
        if (endGroups.size() <= 0) {
            throw new IllegalValueException(String.format(MESSAGE_CANNOT_PARSE_TO_DATE, endDateTime));
        }
        
        // check that end cannot be earlier than start
        final long endTime = endGroups.get(0).getDates().get(0).getTime();
        final long startTime = groups.get(0).getDates().get(0).getTime(); 
        if (endTime < startTime) {
            throw new IllegalValueException(MESSAGE_EVENT_DATE_CONSTRAINTS);
        }
    }

    /**
     * Returns true if s has quotation marks at the start and end, with string inside
     * @param s String to check
     */
    public static boolean hasQuotationMarks(String s) {
        return s.length() > 2
               && ((s.charAt(0) == '"' && s.charAt(s.length()-1) == '"')
                   || (s.charAt(0) == '\'' && s.charAt(s.length()-1) == '\''));
    }

    /**
     * Returns string with begin and end quotation marks trimmed
     * @param s String to trim, assumed to have quotation marks at the start and end
     */
    public static String trimQuotationMarks(String s) {
        return s.substring(1, s.length()-1);
    }

    /**
     * Returns true if s contains status
     * @param s String to check
     */
    public static boolean hasStatus(String s) {
        return "pending".equals(s.toLowerCase()) 
                || "completed".equals(s.toLowerCase());
    }

    /**
     * Returns either status based on s's content
     * @param s String which contains a status
     */
    public static String getStatus(String s) {
        if ("pending".equals(s.toLowerCase())) {
            return "pending";
        } else {
            return "completed";
        }
    }
    
    /**
     * Returns true if s can be parsed as an AMDate type
     * @param s Should be trimmed
     */
    public static boolean hasAMDates(String s) {
        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse(s);
        return groups.size() > 0;
    }

    /**
     * Extracts the start and end date from s
     * @param s String which contains date(s)
     * @return list with a start and end date
     */
    public static List<Long> getDateRange(String s) {
        Parser parser = new Parser();
        DateGroup group = parser.parse(s).get(0);
        List<Date> dates = group.getDates();
        List<Long> ranges = new ArrayList<Long>();
        
        // get the first and last date and use it as the range
        // if there is only one date, first date == last date
        ranges.add(dates.get(0).getTime());
        ranges.add(dates.get(dates.size() - 1).getTime());
        // sort to ensure start always earlier than end
        Collections.sort(ranges);
        return ranges;
    }
}
