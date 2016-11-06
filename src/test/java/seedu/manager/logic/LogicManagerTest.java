package seedu.manager.logic;

import com.google.common.eventbus.Subscribe;

import seedu.manager.commons.core.Config;
import seedu.manager.commons.core.EventsCenter;
import seedu.manager.commons.events.model.ActivityManagerChangedEvent;
import seedu.manager.commons.events.ui.ShowHelpRequestEvent;
import seedu.manager.commons.util.ConfigUtil;
import seedu.manager.commons.util.XmlUtil;
import seedu.manager.logic.Logic;
import seedu.manager.logic.LogicManager;
import seedu.manager.logic.commands.*;
import seedu.manager.model.ActivityManager;
import seedu.manager.model.Model;
import seedu.manager.model.ModelManager;
import seedu.manager.model.ReadOnlyActivityManager;
import seedu.manager.model.activity.*;
import seedu.manager.storage.StorageManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.manager.commons.core.Messages.*;

public class LogicManagerTest {

    private Model model;
    private Logic logic;
    private TestDataHelper helper;

    //These are for checking the correctness of the events raised
    private ReadOnlyActivityManager latestSavedActivityManager;
    private boolean helpShown;
    
    @Subscribe
    private void handleLocalModelChangedEvent(ActivityManagerChangedEvent amce) {
        latestSavedActivityManager = new ActivityManager(amce.data);
    }

    @Subscribe
    private void handleShowHelpRequestEvent(ShowHelpRequestEvent she) {
        helpShown = true;
    }
    
    @Before
    public void setup() {
        model = new ModelManager();
        logic = new LogicManager(model);
        EventsCenter.getInstance().registerHandler(this);
        helper = new TestDataHelper();

        latestSavedActivityManager = new ActivityManager(model.getActivityManager()); // last saved assumed to be up to date before.
        helpShown = false;
    }

    @After
    public void teardown() {
        EventsCenter.clearSubscribers();
    }

    /**
     * Executes the command and confirms that the result message is correct.
     * Both the 'activity manager' and the 'last shown list' are expected to be empty.
     * @see #assertCommandBehavior(String, String, ReadOnlyActivityManager, List)
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage) throws Exception {
        assertCommandBehavior(inputCommand, expectedMessage, new ActivityManager(), Collections.emptyList());
    }

    //@@author A0135730M
    /**
     * Executes the command and confirms that the result message is correct and
     * also confirms that the following three parts of the LogicManager object's state are as expected:<br>
     *      - the internal activity manager data are same as those in the {@code expectedActivityManager} <br>
     *      - the backing list shown by UI matches the {@code shownList} <br>
     *      - {@code expectedActivityManager} was saved to the storage file. <br>
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage,
                                       ReadOnlyActivityManager expectedActivityManager,
                                       List<? extends Activity> expectedShownList) throws Exception {

        //Execute the command
        CommandResult result = logic.execute(inputCommand);

        //Confirm the ui display elements should contain the right data
        assertEquals(expectedMessage, result.feedbackToUser);
        //Confirm the shown list is subset and in order with filtered activity list
        assertTrue(Collections.indexOfSubList(model.getFilteredActivityList(), expectedShownList) != -1);
        
        //Confirm the state of data (saved and in-memory) is as expected
        assertEquals(expectedActivityManager, model.getActivityManager());
        assertEquals(expectedActivityManager, latestSavedActivityManager);
    }

    //@@author A0144881Y
    @Test
    public void execute_unknownCommandWord() throws Exception {
        String unknownCommand = "uicfhmowqewca";
        assertCommandBehavior(unknownCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_help() throws Exception {
        assertCommandBehavior("help", HelpCommand.SHOWING_HELP_MESSAGE);
        assertTrue(helpShown);
    }

    @Test
    public void execute_exit() throws Exception {
        assertCommandBehavior("exit", ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT);
    }

    @Test
    public void execute_clear() throws Exception {
        model.addActivity(helper.generateActivity(1), true);
        model.addActivity(helper.generateActivity(2), true);
        model.addActivity(helper.generateActivity(3), true);

        assertCommandBehavior("clear", ClearCommand.MESSAGE_SUCCESS, new ActivityManager(), Collections.emptyList());
    }

    //@@author A0135730M
    
    private void assertEndDateEarlierThanStartDateForCommand(String commandWord) throws Exception {
        ActivityManager expectedAM = new ActivityManager();
        Activity dummy = new Activity("Dummy");
        model.addActivity(dummy, true);
        expectedAM.addActivity(dummy);
        
        assertCommandBehavior(commandWord + " invalid event from " + helper.getReferenceDateString()
                              + " to day before " + helper.getReferenceDateString(),
                Activity.MESSAGE_DATE_CONSTRAINTS,
                expectedAM,
                expectedAM.getActivityList().getInternalList());
    }
    
    private void assertInvalidDateForCommand(String commandWord) throws Exception {
        assertCommandBehavior(commandWord + " event from abc to def", 
                String.format(MESSAGE_CANNOT_PARSE_TO_DATE, "abc"));
        assertCommandBehavior(commandWord + " event from today to def", 
                String.format(MESSAGE_CANNOT_PARSE_TO_DATE, "def"));
        assertCommandBehavior(commandWord + " deadline by ghi", 
                String.format(MESSAGE_CANNOT_PARSE_TO_DATE, "ghi"));
    }
    
    @Test
    public void execute_addEndDateEarlierThanStartDate_errorMessageShown() throws Exception {
        assertEndDateEarlierThanStartDateForCommand("add");
    }
    
    @Test
    public void execute_addInvalidDate_errorMessageShown() throws Exception {
        assertInvalidDateForCommand("add");
    }
    
    @Test
    public void execute_add_parseKeywordsCorrectly() throws Exception {
        // able to add floating activity
        // note that incomplete format of deadline / event will result in activity being considered as floating
        Activity toBeAdded = new Activity("Girl from next door");
        ActivityManager expectedAM = new ActivityManager();
        expectedAM.addActivity(toBeAdded);
        
        assertCommandBehavior("add Girl from next door",
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded.getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());       
        
        // able to add deadline activity with keywords (on/by) (with spaces)
        toBeAdded = new Activity("read Village by the Sea");
        toBeAdded.setType(ActivityType.DEADLINE);
        toBeAdded.setDateTime(helper.getReferenceDateString());
        expectedAM.addActivity(toBeAdded);
        
        assertCommandBehavior("add read Village by the Sea \"BY\" " + helper.getReferenceDateString(),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded.getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());
        
        
        // able to add deadline activity with keywords (on/by) (with spaces)
        toBeAdded = new Activity("learn Ruby on Rails");
        toBeAdded.setType(ActivityType.DEADLINE);
        toBeAdded.setDateTime(helper.getReferenceDateString());
        expectedAM.addActivity(toBeAdded);
        
        assertCommandBehavior("add learn Ruby on Rails \"on\" " + helper.getReferenceDateString(),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded.getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());
        
        // able to add event activity with keywords (from/to) (with spaces)
        toBeAdded = new Activity("Love from Paris");
        toBeAdded.setType(ActivityType.EVENT);
        toBeAdded.setDateTime(helper.getReferenceDateString());
        toBeAdded.setEndDateTime(helper.getReferenceDateString());
        expectedAM.addActivity(toBeAdded);
        
        assertCommandBehavior("add Love from Paris \"from\" " + helper.getReferenceDateString() 
                              + " to " + helper.getReferenceDateString(),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded.getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());
        
        // able to add event activity with keywords (from and to) (with spaces)
        toBeAdded = new Activity("Movie: from Jupiter to Mars");
        toBeAdded.setType(ActivityType.EVENT);
        toBeAdded.setDateTime(helper.getReferenceDateString());
        toBeAdded.setEndDateTime(helper.getReferenceDateString());
        expectedAM.addActivity(toBeAdded);
        
        assertCommandBehavior("add Movie: from Jupiter to Mars \"from\" " + helper.getReferenceDateString() 
                              + " \"to\" " + helper.getReferenceDateString(),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded.getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());
    }
    
    @Test
    public void execute_addRecurIfOutOfRange_errorMessageShown() throws Exception {
        assertCommandBehavior("add zero no count on today for 0 year", MESSAGE_RECUR_OUT_OF_RANGE);
        assertCommandBehavior("add thirty-one golden rings from today to tomorrow for 31 days", MESSAGE_RECUR_OUT_OF_RANGE);
    }
    
    @Test
    public void execute_add_recurCorrectly() throws Exception {
        ActivityManager expectedAM = new ActivityManager();
        Activity toBeAdded = null;
        int repeat;
        
        // able to add recurring deadline by day
        for (repeat = 0; repeat < 3; repeat++) {
            toBeAdded = new Activity("Every day I'm shuffling");
            toBeAdded.setType(ActivityType.DEADLINE);
            toBeAdded.setDateTime(helper.getReferenceDateString());
            toBeAdded.setOffset(repeat, "day");
            expectedAM.addActivity(toBeAdded);
        }
        
        assertCommandBehavior("add Every day I'm shuffling by " + helper.getReferenceDateString() + " for 3 days",
                String.format(AddCommand.MESSAGE_RECUR_SUCCESS, toBeAdded.getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());
        
        // able to add recurring deadline by week
        for (repeat = 0; repeat < 2; repeat++) {
            toBeAdded = new Activity("Clubbing on the dance floor");
            toBeAdded.setType(ActivityType.DEADLINE);
            toBeAdded.setDateTime(helper.getReferenceDateString());
            toBeAdded.setOffset(repeat, "Week");
            expectedAM.addActivity(toBeAdded);
        }
        
        assertCommandBehavior("add Clubbing on the dance floor \"ON\" " + helper.getReferenceDateString() + " for 2 WEEK",
                String.format(AddCommand.MESSAGE_RECUR_SUCCESS, toBeAdded.getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());
        
        // able to add recurring event by month
        for (repeat = 0; repeat < 12; repeat++) {
            toBeAdded = new Activity("Monthly general meeting from boss");
            toBeAdded.setType(ActivityType.EVENT);
            toBeAdded.setDateTime(helper.getReferenceDateString());
            toBeAdded.setEndDateTime(helper.getReferenceDateString());
            toBeAdded.setOffset(repeat, "MONTH");
            expectedAM.addActivity(toBeAdded);
        }
        
        assertCommandBehavior("add Monthly general meeting from boss \"from\" " + helper.getReferenceDateString() 
                              + " to " + helper.getReferenceDateString() + " For 12 months",
                String.format(AddCommand.MESSAGE_RECUR_SUCCESS, toBeAdded.getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());
        
        // able to add recurring event by year
        for (repeat = 0; repeat < 1; repeat++) {
            toBeAdded = new Activity("Birthday party");
            toBeAdded.setType(ActivityType.EVENT);
            toBeAdded.setDateTime(helper.getReferenceDateString());
            toBeAdded.setEndDateTime("day after " + helper.getReferenceDateString());
            toBeAdded.setOffset(repeat, "year");
            expectedAM.addActivity(toBeAdded);
        }
        
        assertCommandBehavior("add Birthday party from " + helper.getReferenceDateString() 
                              + " \"to\" day after " + helper.getReferenceDateString() + " foR 1 yeaR",
                String.format(AddCommand.MESSAGE_RECUR_SUCCESS, toBeAdded.getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());
    }
    
    //@@author A0135730M
    @Test
    public void execute_list_sortActivitiesCorrectly() throws Exception {
        Activity a1 = new Activity("deadline");
        a1.setType(ActivityType.DEADLINE);
        a1.setDateTime("yesterday");
        Activity a2 = new Activity("event");
        a2.setType(ActivityType.EVENT);
        a2.setDateTime("today");
        a2.setEndDateTime("tomorrow");
        Activity a3 = new Activity("completed deadline");
        a3.setType(ActivityType.DEADLINE);
        a3.setDateTime("two days ago");
        a3.setStatus(true);
        Activity a4 = new Activity("floating bottom");
        
        List<Activity> unorderedList = helper.generateActivityList(a2, a4, a1, a3);
        ActivityManager expectedAM = helper.generateActivityManager(unorderedList);
        helper.addToModel(model, unorderedList);
        
        List<Activity> orderedList = helper.generateActivityList(a1, a2, a3, a4);
        assertCommandBehavior("list",
                ListCommand.MESSAGE_SUCCESS,
                expectedAM,
                orderedList);
    }

    //@@author A0144881Y
    /**
     * Confirms the 'invalid argument index number behaviour' for the given command
     * targeting a single activity in the shown list, using visible index.
     * @param commandWord to test assuming it targets a single activity in the last shown list based on visible index.
     */
    private void assertIncorrectIndexFormatBehaviorForCommand(String commandWord, String expectedMessage) throws Exception {
        assertCommandBehavior(commandWord , expectedMessage); //index missing
        assertCommandBehavior(commandWord + " +1", expectedMessage); //index should be unsigned
        assertCommandBehavior(commandWord + " -1", expectedMessage); //index should be unsigned
        assertCommandBehavior(commandWord + " 0", expectedMessage); //index cannot be 0
        assertCommandBehavior(commandWord + " not_a_number", expectedMessage);
    }

    /**
     * Confirms the 'invalid argument index number behaviour' for the given command
     * targeting a single activity in the shown list, using visible index.
     * @param commandWord to test assuming it targets a single activity in the last shown list based on visible index.
     */
    private void assertIndexNotFoundBehaviorForCommand(String commandWord, String optArgs) throws Exception {
        ActivityManager expectedAM = helper.generateActivityManager(2);
        List<Activity> activityList = helper.generateActivityList(2);
        model.resetData(expectedAM);

        assertCommandBehavior(commandWord + " 3 " + optArgs, 
                MESSAGE_INVALID_ACTIVITY_DISPLAYED_INDEX, 
                expectedAM, 
                activityList);
    }

    @Test
    public void execute_deleteInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("delete", expectedMessage);
    }

    @Test
    public void execute_deleteIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("delete", "");
    }

    @Test
    public void execute_delete_removesCorrectActivity() throws Exception {
        
        List<Activity> threeActivities = helper.generateActivityList(3);

        ActivityManager expectedAM = helper.generateActivityManager(threeActivities);
        expectedAM.removeActivity(threeActivities.get(1));
        helper.addToModel(model, threeActivities);

        assertCommandBehavior("delete 2",
                String.format(DeleteCommand.MESSAGE_DELETE_ACTIVITY_SUCCESS, 
                              DeleteCommand.ACTIVITY_SEPERATOR +threeActivities.get(1).getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());
    }
    
    @Test
    public void execute_updateInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UpdateCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("update", expectedMessage);
    }
    
    //@@author A0135730M
    @Test
    public void execute_updateEndDateEarlierThanStartDate_errorMessageShown() throws Exception {
        assertEndDateEarlierThanStartDateForCommand("update 1");
    }
    
    @Test
    public void execute_updateIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("update", "blind mice");
    }
    
    @Test
    public void execute_updateInvalidDate_errorMessageShown() throws Exception {
        assertInvalidDateForCommand("update 1");
    }
    
    
    @Test
    public void execute_update_trimArguments() throws Exception {        
        Activity existingActivity = new Activity("bla bla bla");
        model.addActivity(existingActivity, true);
        
        ActivityManager expectedAM = new ActivityManager();
        Activity newActivity = new Activity("bla");
        expectedAM.addActivity(newActivity);

        assertCommandBehavior("update 1     bla",
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, newActivity.getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());
    }
    
    @Test
    public void execute_update_parseKeywordsCorrectly() throws Exception {
    	ActivityManager expectedAM = new ActivityManager();
    	ActivityManager emptyAM = new ActivityManager();
    	
        Activity existingDeadline = new Activity("Presentation");
        existingDeadline.setType(ActivityType.DEADLINE);
        existingDeadline.setDateTime(helper.getReferenceDateString());
        model.addActivity(existingDeadline, true);
        
        // able to update deadline without name
        expectedAM.resetData(emptyAM);
        Activity newDeadline = new Activity("Presentation");
        newDeadline.setType(ActivityType.DEADLINE);
        newDeadline.setDateTime("day after " + helper.getReferenceDateString());
        expectedAM.addActivity(newDeadline);
        
        assertCommandBehavior("update 1 on day after " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, newDeadline.getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());
        
        // able to update deadline activity with keywords (on/by) (with spaces)
        expectedAM.resetData(emptyAM);
        newDeadline.setName("read Village by the Sea");
        newDeadline.setDateTime(helper.getReferenceDateString());
        expectedAM.addActivity(newDeadline);
        
        assertCommandBehavior("update 1 read Village by the Sea \"by\" " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, newDeadline.getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());
        
        // able to update event without name
        expectedAM.resetData(emptyAM);
        Activity newEvent = new Activity("read Village by the Sea");
        newEvent.setType(ActivityType.EVENT);
        newEvent.setDateTime("day after " + helper.getReferenceDateString());
        newEvent.setEndDateTime("day after " + helper.getReferenceDateString());
        expectedAM.addActivity(newEvent);
        
        assertCommandBehavior("update 1 from day after " + helper.getReferenceDateString() + " to day after " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, newEvent.getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());
        
        // able to update event activity with keywords (from/to) (with spaces)
        expectedAM.resetData(emptyAM);
        newEvent.setName("Love from Paris");
        newEvent.setDateTime(helper.getReferenceDateString());
        newEvent.setEndDateTime(helper.getReferenceDateString());
        expectedAM.addActivity(newEvent);
        
        assertCommandBehavior("update 1 Love from Paris \"froM\" " + helper.getReferenceDateString() 
                              + " to " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, newEvent.getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());
        
        // able to update event activity with keywords (from and to) (with spaces)
        expectedAM.resetData(emptyAM);
        newEvent.setName("Movie: from Jupiter to Mars");
        newEvent.setDateTime(helper.getReferenceDateString());
        newEvent.setDateTime(helper.getReferenceDateString());
        expectedAM.addActivity(newEvent);
        
        assertCommandBehavior("update 1 Movie: from Jupiter to Mars \"from\" " + helper.getReferenceDateString() 
                              + " \"to\" " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, newEvent.getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());
    }
    
    @Test
    public void execute_update_changeActivityTypeCorrectly() throws Exception {
        ActivityManager expectedAM = new ActivityManager();
        ActivityManager emptyAM = new ActivityManager();
        Activity floating = new Activity("Floating");
        Activity deadline = new Activity("Deadline");
        deadline.setType(ActivityType.DEADLINE);
        deadline.setDateTime(helper.getReferenceDateString());
        Activity event = new Activity("Event");
        event.setType(ActivityType.EVENT);
        event.setDateTime(helper.getReferenceDateString());
        event.setEndDateTime(helper.getReferenceDateString());
        
        // able to update from floating to deadline
        model.addActivity(floating, true);
        expectedAM.addActivity(deadline);
        assertCommandBehavior("update 1 Deadline on " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, deadline.getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());
        
        // able to update from deadline to event
        expectedAM.resetData(emptyAM);
        expectedAM.addActivity(event);
        assertCommandBehavior("update 1 Event from " + helper.getReferenceDateString() + " to " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, event.getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());
        
        
        // able to update from floating to event
        model.resetData(emptyAM);
        expectedAM.resetData(emptyAM);
        model.addActivity(floating, true);
        expectedAM.addActivity(event);
        assertCommandBehavior("update 1 Event from " + helper.getReferenceDateString() + " to " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, event.getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());
        
        // able to update from event to deadline
        expectedAM.resetData(emptyAM);
        expectedAM.addActivity(deadline);
        assertCommandBehavior("update 1 Deadline on " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, deadline.getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());
    }

    @Test
    public void execute_search_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE);
        assertCommandBehavior("search ", expectedMessage);
    }

    @Test
    public void execute_search_matchNameSuccess() throws Exception {
        // should match if only full search name is found in part of activity name, case insensitive
        Activity aTarget1 = new Activity("bla bla KEY bla");
        Activity aTarget2 = new Activity("bla KEY bla bceofeia");
        Activity aTarget3 = new Activity("bla key bla");
        Activity a1 = new Activity("KE Y");
        Activity a2 = new Activity("KEYKEYKEY sduauo");
        Activity a3 = new Activity("KEXY");

        List<Activity> sixActivities = helper.generateActivityList(a1, aTarget1, a2, aTarget2, a3, aTarget3);
        ActivityManager expectedAM = helper.generateActivityManager(sixActivities);
        List<Activity> expectedList = helper.generateActivityList(aTarget1, aTarget2, aTarget3);
        helper.addToModel(model, sixActivities);

        assertCommandBehavior("search \"KEY\"",
                Command.getMessageForActivityListShownSummary(expectedList.size()),
                expectedAM,
                expectedList);
    }
    
    @Test
    public void execute_search_multipleKeywords() throws Exception {
        Activity a1 = new Activity("come home");
        Activity a2 = new Activity("come");
        Activity a3 = new Activity("home");
        Activity a4 = new Activity("back");
        
        List<Activity> activities = helper.generateActivityList(a1, a2, a3, a4);
        ActivityManager expectedAM = helper.generateActivityManager(activities);
        List<Activity> expectedList = helper.generateActivityList(a1, a2, a3);
        helper.addToModel(model, activities);

        assertCommandBehavior("search \"come home\"",
                Command.getMessageForActivityListShownSummary(expectedList.size()),
                expectedAM,
                expectedList);
    }
    
    @Test
    public void execute_search_useQuotationMarksForKeywords() throws Exception {
        Activity aTarget1 = new Activity("read TODAY newspaper");
        Activity aTarget2 = new Activity("something");
        aTarget2.setType(ActivityType.DEADLINE);
        aTarget2.setDateTime("today");
        
        List<Activity> activities = helper.generateActivityList(aTarget1, aTarget2);
        ActivityManager expectedAM = helper.generateActivityManager(activities);
        List<Activity> expectedList = helper.generateActivityList(aTarget1);
        helper.addToModel(model, activities);
        
        assertCommandBehavior("search \'today\'",
                Command.getMessageForActivityListShownSummary(expectedList.size()),
                expectedAM,
                expectedList);
        
        expectedList = helper.generateActivityList(aTarget2);
        
        assertCommandBehavior("search today",
                Command.getMessageForActivityListShownSummary(expectedList.size()),
                expectedAM,
                expectedList);
    }
    
    @Test
    public void execute_search_matchesIfWithinDateSuccess() throws Exception {
        Activity aTarget1 = new Activity("event");
        aTarget1.setType(ActivityType.EVENT);
        aTarget1.setDateTime("yesterday");
        aTarget1.setEndDateTime("tomorrow");
        Activity aTarget2 = new Activity("deadline");
        aTarget2.setType(ActivityType.DEADLINE);
        aTarget2.setDateTime("today");
        
        Activity a1 = new Activity("no match");
        Activity a2 = new Activity("deadline");
        a2.setType(ActivityType.DEADLINE);
        a2.setDateTime("next week");
        Activity a3 = new Activity("event");
        a3.setType(ActivityType.EVENT);
        a3.setDateTime("next week");
        a3.setEndDateTime("two weeks later");
        
        List<Activity> activities = helper.generateActivityList(aTarget1, a1, aTarget2, a2, a3);
        ActivityManager expectedAM = helper.generateActivityManager(activities);
        List<Activity> expectedList = helper.generateActivityList(aTarget1, aTarget2);
        helper.addToModel(model, activities);
        
        assertCommandBehavior("search today",
                Command.getMessageForActivityListShownSummary(expectedList.size()),
                expectedAM,
                expectedList);
        
        assertCommandBehavior("search today to tomorrow",
                Command.getMessageForActivityListShownSummary(expectedList.size()),
                expectedAM,
                expectedList);
        
        assertCommandBehavior("search past 2 days",
                Command.getMessageForActivityListShownSummary(expectedList.size()),
                expectedAM,
                expectedList);
    }
    
    @Test
    public void execute_search_matchesStatusSuccess() throws Exception {
        
        List<Activity> threeActivities = helper.generateActivityList(3);

        ActivityManager expectedAM = helper.generateActivityManager(threeActivities);
        expectedAM.markActivity(threeActivities.get(0));
        threeActivities.get(0).setStatus(true);
        helper.addToModel(model, threeActivities);
        List<Activity> expectedMarkList = helper.generateActivityList(threeActivities.get(0));
        List<Activity> expectedPendingList = helper.generateActivityList(threeActivities.get(1), threeActivities.get(2));
       
        assertCommandBehavior("search completed",
                Command.getMessageForActivityListShownSummary(expectedMarkList.size()),
                expectedAM,
                expectedMarkList);
        
        assertCommandBehavior("search pending",
                Command.getMessageForActivityListShownSummary(expectedPendingList.size()),
                expectedAM,
                expectedPendingList);
        
        
    }
    
    //@@author A0144704L
    @Test
    public void execute_markInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("mark", expectedMessage);
    }

    @Test
    public void execute_markIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("mark", "");
    }

    @Test
    public void execute_mark_marksCorrectActivity() throws Exception {
        
        List<Activity> threeActivities = helper.generateActivityList(3);

        ActivityManager expectedAM = helper.generateActivityManager(threeActivities);
        expectedAM.markActivity(threeActivities.get(1));
        helper.addToModel(model, threeActivities);
       
        assertCommandBehavior("mark 2",
                String.format(MarkCommand.MESSAGE_MARK_ACTIVITY_SUCCESS, threeActivities.get(1).getName()),
                expectedAM,
                expectedAM.getPendingActivityList());
    }
    
    
    @Test
    public void execute_unmarkInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("unmark", expectedMessage);
    }

    @Test
    public void execute_unmarkIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("unmark", "");
    }

    @Test
    public void execute_unmark_unmarksCorrectActivity() throws Exception {
        
        List<Activity> threeActivities = helper.generateActivityList(3);

        ActivityManager expectedAM = helper.generateActivityManager(threeActivities);
        expectedAM.unmarkActivity(threeActivities.get(1));
        helper.addToModel(model, threeActivities);
       
        assertCommandBehavior("unmark 2",
                String.format(UnmarkCommand.MESSAGE_UNMARK_ACTIVITY_SUCCESS, threeActivities.get(1).getName()),
                expectedAM,
                expectedAM.getActivityList().getInternalList());
    }
    
    //@@author A0139797E
    @Test
    public void execute_undo_noCommand() throws Exception {
        assertCommandBehavior("undo", UndoCommand.MESSAGE_INDEX_LESS_THAN_ZERO);
    }
    
    @Test
    public void execute_undo_outOfBounds() throws Exception {
        
        helper.addToModel(model, 2);
        ActivityManager expectedAM = helper.generateActivityManager(2);
        List<Activity> expectedList = helper.generateActivityList(2);
        
        // failed undo should not modify AM and list
        assertCommandBehavior("undo 3", 
                UndoCommand.MESSAGE_OFFSET_OUT_OF_BOUNDS,
                expectedAM,
                expectedList);
    }
    
    @Test
    public void execute_undo_normally() throws Exception {
        
        helper.addToModel(model, 4);
        
        // able to undo (no offset means default 1)
        ActivityManager expectedAM = helper.generateActivityManager(3);
        List<Activity> expectedList = helper.generateActivityList(3);
        assertCommandBehavior("undo", 
                String.format(UndoCommand.MESSAGE_SUCCESS, 1), 
                expectedAM, 
                expectedList);
        
        // able to undo multiple times
        expectedAM = helper.generateActivityManager(1);
        expectedList = helper.generateActivityList(1);
        assertCommandBehavior("undo 2", 
                String.format(UndoCommand.MESSAGE_SUCCESS, 2), 
                expectedAM, 
                expectedList);
    }
    
    @Test
    public void execute_redo_noCommand() throws Exception {
        assertCommandBehavior("redo", RedoCommand.MESSAGE_INDEX_LARGER_THAN_MAX);
    }
    
    @Test
    public void execute_redo_outOfBounds() throws Exception {
        
        helper.addToModel(model, 2);
        
        // undo first before redo
        ActivityManager expectedAM = helper.generateActivityManager(1);
        List<Activity> expectedList = helper.generateActivityList(1);
        assertCommandBehavior("undo", 
                String.format(UndoCommand.MESSAGE_SUCCESS, 1), 
                expectedAM, 
                expectedList);
        
        assertCommandBehavior("redo 3", 
                String.format(RedoCommand.MESSAGE_OFFSET_OUT_OF_BOUNDS, 1), 
                expectedAM, 
                expectedList);
    }
    
    @Test
    public void execute_redo_normally() throws Exception {
        helper.addToModel(model, 4);
        
        // undo first before redo
        ActivityManager expectedAM = helper.generateActivityManager(1);
        List<Activity> expectedList = helper.generateActivityList(1);
        assertCommandBehavior("undo 3", 
                String.format(UndoCommand.MESSAGE_SUCCESS, 3), 
                expectedAM, 
                expectedList);
        
        // able to redo (on offset means default 1)
        expectedAM = helper.generateActivityManager(2);
        expectedList = helper.generateActivityList(2);
        assertCommandBehavior("redo", 
                String.format(RedoCommand.MESSAGE_SUCCESS, 1), 
                expectedAM, 
                expectedList);
        
       // able to multiple redo
        expectedAM = helper.generateActivityManager(4);
        expectedList = helper.generateActivityList(4);
        assertCommandBehavior("redo 2", 
                String.format(RedoCommand.MESSAGE_SUCCESS, 2), 
                expectedAM, 
                expectedList);
    }
    
    //@@author A0144704L
    @Test
    public void execute_store_storeToCorrectLocation() throws Exception {
    	String testDataFileLocation = "data/RemindarooTest.xml";
    	assertCommandBehavior("store " + testDataFileLocation, String.format(StoreCommand.MESSAGE_STORE_FILE_SUCCESS, testDataFileLocation));
    }

    @Test
    public void execute_store_incorrectExtension() throws Exception {
    	String testDataFileLocation = "data/RemindarooTest.txt";
    	assertCommandBehavior("store " + testDataFileLocation, String.format(MESSAGE_INVALID_COMMAND_FORMAT, StoreCommand.MESSAGE_USAGE));
    }
    
    @Test
    public void execute_store_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, StoreCommand.MESSAGE_USAGE);
        assertCommandBehavior("store", expectedMessage);
    }
    
    @Test
    public void execute_load_fileLoadedCorrectly() throws Exception {
        String testDataFileLocation = "data/seed.xml";
        String existingDataFileLocation = ConfigUtil.readConfig(Config.DEFAULT_CONFIG_FILE).get()
                                          .getActivityManagerFilePath();
        logic.execute("clear");
        
        CommandResult result = logic.execute("load " + testDataFileLocation);
        
        assertEquals(String.format(LoadCommand.MESSAGE_LOAD_FILE_SUCCESS, testDataFileLocation), result.feedbackToUser);
        XmlUtil.contentEquals(testDataFileLocation, existingDataFileLocation);
    }
    
    @Test
    public void execute_load_invalidFile() throws Exception {
        String testDataFileLocation = "data/ThisShouldNotExist.xml";
        assertCommandBehavior("load " + testDataFileLocation, LoadCommand.MESSAGE_LOAD_FILE_INVALID);
    }
    
    @Test
    public void execute_load_invalidArgsFormat() throws Exception {
        assertCommandBehavior("load", String.format(MESSAGE_INVALID_COMMAND_FORMAT, LoadCommand.MESSAGE_USAGE));
    }
    
    /**
     * A utility class to generate test data.
     */
    class TestDataHelper {
        //@@author A0135730M
        public String getReferenceDateString() {
            return "28 Feb 2016 00:00:00";
        }
        

        /**
         * Generates a valid activity using the given seed.
         * Running this function with the same parameter values guarantees the returned activity will have the same state.
         * Each unique seed will generate a unique Activity object.
         *
         * @param seed used to generate the activity data field values
         */
        public Activity generateActivity(int seed) throws Exception {
            return new Activity("Activity " + seed);
        }

        /**
         * Generates an ActivityManager with auto-generated activities.
         */
        public ActivityManager generateActivityManager(int numGenerated) throws Exception{
            ActivityManager activityManager = new ActivityManager();
            addToActivityManager(activityManager, numGenerated);
            return activityManager;
        }

        /**
         * Generates an ActivityManager based on the list of activities given.
         */
        public ActivityManager generateActivityManager(List<Activity> activities) throws Exception{
            ActivityManager activityManager = new ActivityManager();
            addToActivityManager(activityManager, activities);
            return activityManager;
        }

        /**
         * Adds auto-generated Activity objects to the given ActivityManager
         * @param activityManager The ActivityManager to which the activities will be added
         */
        public void addToActivityManager(ActivityManager activityManager, int numGenerated) throws Exception{
            addToActivityManager(activityManager, generateActivityList(numGenerated));
        }

        /**
         * Adds the given list of activities to the given ActivityManager
         */
        public void addToActivityManager(ActivityManager activityManager, List<Activity> activitiesToAdd) throws Exception{
            for(Activity activity: activitiesToAdd){
                activityManager.addActivity(activity);
            }
        }

        /**
         * Adds auto-generated Activity objects to the given model
         * @param model The model to which the activities will be added
         */
        public void addToModel(Model model, int numGenerated) throws Exception{
            addToModel(model, generateActivityList(numGenerated));
        }

        /**
         * Adds the given list of activities to the given model
         */
        public void addToModel(Model model, List<Activity> activtiesToAdd) throws Exception{
            for(Activity activity: activtiesToAdd){
                model.addActivity(activity, true);
            }
        }

        /**
         * Generates a list of activities based on the flags.
         */
        public List<Activity> generateActivityList(int numGenerated) throws Exception{
            List<Activity> activity = new ArrayList<>();
            for(int i = 1; i <= numGenerated; i++){
                activity.add(generateActivity(i));
            }
            return activity;
        }

        public List<Activity> generateActivityList(Activity... activities) {
            return Arrays.asList(activities);
        }
    }
}
