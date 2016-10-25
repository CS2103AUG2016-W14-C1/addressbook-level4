package seedu.manager.logic;

import com.google.common.eventbus.Subscribe;

import javafx.collections.ObservableList;
import seedu.manager.commons.core.EventsCenter;
import seedu.manager.commons.core.Messages;
import seedu.manager.commons.core.UnmodifiableObservableList;
import seedu.manager.commons.events.model.ActivityManagerChangedEvent;
import seedu.manager.commons.events.ui.JumpToListRequestEvent;
import seedu.manager.commons.events.ui.ShowHelpRequestEvent;
import seedu.manager.logic.Logic;
import seedu.manager.logic.LogicManager;
import seedu.manager.logic.commands.*;
import seedu.manager.model.ActivityManager;
import seedu.manager.model.Model;
import seedu.manager.model.ModelManager;
import seedu.manager.model.ReadOnlyActivityManager;
import seedu.manager.model.activity.*;
import seedu.manager.model.tag.Tag;
import seedu.manager.model.tag.UniqueTagList;
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

    /**
     * See https://github.com/junit-team/junit4/wiki/rules#temporaryfolder-rule
     */
    @Rule
    public TemporaryFolder saveFolder = new TemporaryFolder();

    private Model model;
    private Logic logic;

    //These are for checking the correctness of the events raised
    private ReadOnlyActivityManager latestSavedActivityManager;
    private boolean helpShown;
    private int targetedJumpIndex;

    @Subscribe
    private void handleLocalModelChangedEvent(ActivityManagerChangedEvent abce) {
        latestSavedActivityManager = new ActivityManager(abce.data);
    }

    @Subscribe
    private void handleShowHelpRequestEvent(ShowHelpRequestEvent she) {
        helpShown = true;
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent je) {
        targetedJumpIndex = je.targetIndex;
    }

    @Before
    public void setup() {
        model = new ModelManager();
        String tempActivityManagerFile = saveFolder.getRoot().getPath() + "TempActivityManager.xml";
        String tempPreferencesFile = saveFolder.getRoot().getPath() + "TempPreferences.json";
        logic = new LogicManager(model, new StorageManager(tempActivityManagerFile, tempPreferencesFile));
        EventsCenter.getInstance().registerHandler(this);

        latestSavedActivityManager = new ActivityManager(model.getActivityManager()); // last saved assumed to be up to date before.
        helpShown = false;
        targetedJumpIndex = -1; // non yet
    }

    @After
    public void teardown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_invalid() throws Exception {
        String invalidCommand = "       ";
        assertCommandBehavior(invalidCommand,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }

    /**
     * Executes the command and confirms that the result message is correct.
     * Both the 'activity manager' and the 'last shown list' are expected to be empty.
     * @see #assertCommandBehavior(String, String, ReadOnlyActivityManager, List)
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage) throws Exception {
        assertCommandBehavior(inputCommand, expectedMessage, new ActivityManager(), Collections.emptyList());
    }

    /**
     * Executes the command and confirms that the result message is correct and
     * also confirms that the following three parts of the LogicManager object's state are as expected:<br>
     *      - the internal activity manager data are same as those in the {@code expectedAddressBook} <br>
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
        assertTrue(model.getFilteredActivityList().containsAll(expectedShownList));
        
        //Confirm the state of data (saved and in-memory) is as expected
        assertEquals(expectedActivityManager, model.getActivityManager());
        assertEquals(expectedActivityManager, latestSavedActivityManager);
    }
    
    // TODO: Refactor this "hack" if possible
    /**
     * Overload assertCommandBehavior(..., List<? extends Activity> expectedShownList) to accept both data types
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage,
            ReadOnlyActivityManager expectedActivityManager,
            ActivityList expectedShownList) throws Exception {
        assertCommandBehavior(inputCommand, expectedMessage, expectedActivityManager, (List<? extends Activity>)expectedShownList.getInternalList());
    }


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
        TestDataHelper helper = new TestDataHelper();
        model.addActivity(helper.generateActivity(1));
        model.addActivity(helper.generateActivity(2));
        model.addActivity(helper.generateActivity(3));

        assertCommandBehavior("clear", ClearCommand.MESSAGE_SUCCESS, new ActivityManager(), Collections.emptyList());
    }

    @Test
    public void execute_add_Activity_endDateEarlierThanStartDate() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        assertCommandBehavior("add invalid event from " + helper.getReferenceDateString()
                              + " to day before " + helper.getReferenceDateString(),
                Activity.MESSAGE_DATE_CONSTRAINTS);
    }
    
    @Test
    public void execute_add_invalidDate() throws Exception {
        assertCommandBehavior("add event from abc to def", 
                String.format(Messages.MESSAGE_CANNOT_PARSE_TO_DATE, "abc"));
        assertCommandBehavior("add event from today to def", 
                String.format(Messages.MESSAGE_CANNOT_PARSE_TO_DATE, "def"));
        assertCommandBehavior("add deadline by ghi", 
                String.format(Messages.MESSAGE_CANNOT_PARSE_TO_DATE, "ghi"));
    }
    
    @Test
    public void execute_add_parseKeywordsCorrectly() throws Exception {
        // able to add floating activity
        // note that incomplete format of deadline / event will result in activity being considered as floating
        TestDataHelper helper = new TestDataHelper();
        Activity toBeAdded = new Activity("Girl from next door");
        ActivityManager expectedAM = new ActivityManager();
        expectedAM.addActivity(toBeAdded);
        assertCommandBehavior("add Girl from next door",
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded.getName()),
                expectedAM,
                expectedAM.getActivityList());
        
        // able to add deadline activity with keywords (on/by) (without spaces)
        toBeAdded = new Activity("Presentation Ruby", helper.getReferenceDateString());
        expectedAM.addActivity(toBeAdded);
        assertCommandBehavior("add Presentation Ruby on " + helper.getReferenceDateString(),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded.getName()),
                expectedAM,
                expectedAM.getActivityList());
        
        
        // able to add deadline activity with keywords (on/by) (with spaces)
        toBeAdded = new Activity("read Village by the Sea", helper.getReferenceDateString());
        expectedAM.addActivity(toBeAdded);
        assertCommandBehavior("add read Village by the Sea \"on\" " + helper.getReferenceDateString(),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded.getName()),
                expectedAM,
                expectedAM.getActivityList());
        
        
        // able to add deadline activity with keywords (on/by) (with spaces)
        toBeAdded = new Activity("learn Ruby on Rails", helper.getReferenceDateString());
        expectedAM.addActivity(toBeAdded);
        assertCommandBehavior("add learn Ruby on Rails \"by\" " + helper.getReferenceDateString(),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded.getName()),
                expectedAM,
                expectedAM.getActivityList());
        
        
        // able to add event activity with keywords (from/to) (without spaces)
        toBeAdded = new Activity("The fromance of tom and jerry", helper.getReferenceDateString(), helper.getReferenceDateString());
        expectedAM.addActivity(toBeAdded);
        assertCommandBehavior("add The fromance of tom and jerry from " + helper.getReferenceDateString() 
                              + " to " + helper.getReferenceDateString(),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded.getName()),
                expectedAM,
                expectedAM.getActivityList());
        
        
        // able to add event activity with keywords (from/to) (with spaces)
        toBeAdded = new Activity("Love from Paris", helper.getReferenceDateString(), helper.getReferenceDateString());
        expectedAM.addActivity(toBeAdded);
        assertCommandBehavior("add Love from Paris \"from\" " + helper.getReferenceDateString() 
                              + " to " + helper.getReferenceDateString(),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded.getName()),
                expectedAM,
                expectedAM.getActivityList());
        
        
        // able to add event activity with keywords (from/to) (with spaces)
        toBeAdded = new Activity("Train to Busan", helper.getReferenceDateString(), helper.getReferenceDateString());
        expectedAM.addActivity(toBeAdded);
        assertCommandBehavior("add Train to Busan from " + helper.getReferenceDateString() 
                              + " \"to\" " + helper.getReferenceDateString(),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded.getName()),
                expectedAM,
                expectedAM.getActivityList());
        
        
        // able to add event activity with keywords (from/to) (with spaces)
        toBeAdded = new Activity("Movie: from Jupiter to Mars", helper.getReferenceDateString(), helper.getReferenceDateString());
        expectedAM.addActivity(toBeAdded);
        assertCommandBehavior("add Movie: from Jupiter to Mars \"from\" " + helper.getReferenceDateString() 
                              + " \"to\" " + helper.getReferenceDateString(),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded.getName()),
                expectedAM,
                expectedAM.getActivityList());
    }

    @Test
    public void execute_list_showsAllActivities() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        ActivityManager expectedAM = helper.generateActivityManager(2);
        List<? extends Activity> expectedList = (List<? extends Activity>)expectedAM.getActivityList().getInternalList();
        // prepare activity manager state
        helper.addToModel(model, 2);

        assertCommandBehavior("list",
                ListCommand.MESSAGE_SUCCESS,
                expectedAM,
                expectedList);
    }


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
    private void assertIndexNotFoundBehaviorForCommand(String commandWord) throws Exception {
        String expectedMessage = MESSAGE_INVALID_ACTIVITY_DISPLAYED_INDEX;
        TestDataHelper helper = new TestDataHelper();
        List<Activity> activityList = helper.generateActivityList(2);

        // set AB state to 2 activities
        model.resetData(new ActivityManager());
        for (Activity p : activityList) {
            model.addActivity(p);
        }

        assertCommandBehavior(commandWord + " 3", expectedMessage, model.getActivityManager(), activityList);
    }

    @Test
    public void execute_selectInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("select", expectedMessage);
    }

    @Test
    public void execute_selectIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("select");
    }

    /* TODO: remove select command tests if unnecessary 
    @Test
    public void execute_select_jumpsToCorrectActivity() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Activity> threeActivities = helper.generateActivityList(3);

        ActivityManager expectedAM = helper.generateActivityManager(threeActivities);
        helper.addToModel(model, threeActivities);

        assertCommandBehavior("select 2",
                String.format(SelectCommand.MESSAGE_SELECT_ACTIVITY_SUCCESS, 2),
                expectedAM,
                expectedAM.getActivityList());
        assertEquals(1, targetedJumpIndex);
        assertEquals(model.getFilteredActivityList().get(1), threeActivities.get(1));
    } */


    @Test
    public void execute_deleteInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("delete", expectedMessage);
    }

    @Test
    public void execute_deleteIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("delete");
    }

    @Test
    public void execute_delete_removesCorrectActivity() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Activity> threeActivities = helper.generateActivityList(3);

        ActivityManager expectedAM = helper.generateActivityManager(threeActivities);
        expectedAM.removeActivity(threeActivities.get(1));
        helper.addToModel(model, threeActivities);

        assertCommandBehavior("delete 2",
                String.format(DeleteCommand.MESSAGE_DELETE_ACTIVITY_SUCCESS, threeActivities.get(1).getName()),
                expectedAM,
                expectedAM.getActivityList());
    }
    
    @Test
    public void execute_updateInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UpdateCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("update", expectedMessage);
    }
    
    @Test
    public void execute_update_Activity_endDateEarlierThanStartDate() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        ActivityManager expectedAM = new ActivityManager();
        
        Activity dummyEvent = new Activity("Dummy", helper.getReferenceDateString(), helper.getReferenceDateString());
        model.addActivity(dummyEvent);
        expectedAM.addActivity(dummyEvent);
        
        assertCommandBehavior("update 1 newName from " + helper.getReferenceDateString()
                              + " to day before " + helper.getReferenceDateString(),
                Activity.MESSAGE_DATE_CONSTRAINTS,
                expectedAM,
                expectedAM.getActivityList());
    }
    
    @Test
    public void execute_updateIndexNotFound_errorMessageShown() throws Exception {
        String expectedMessage = MESSAGE_INVALID_ACTIVITY_DISPLAYED_INDEX;
        TestDataHelper helper = new TestDataHelper();
        List<Activity> activityList = helper.generateActivityList(2);

        // set AB state to 2 activities
        model.resetData(new ActivityManager());
        for (Activity p : activityList) {
            model.addActivity(p);
        }

        assertCommandBehavior("update 3 blind mice", 
                expectedMessage, 
                model.getActivityManager(), 
                activityList);
    }
    
    @Test
    public void execute_update_invalidDate() throws Exception {
        assertCommandBehavior("update 1 new from abc to def", 
                String.format(Messages.MESSAGE_CANNOT_PARSE_TO_DATE, "abc"));
        assertCommandBehavior("update 1 new from today to def", 
                String.format(Messages.MESSAGE_CANNOT_PARSE_TO_DATE, "def"));
        assertCommandBehavior("update 1 new by ghi", 
                String.format(Messages.MESSAGE_CANNOT_PARSE_TO_DATE, "ghi"));
    }
    
    
    @Test
    public void execute_update_trimArguments() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        ActivityManager expectedAM = new ActivityManager();
        ActivityManager emptyAM = new ActivityManager();
        
        model.resetData(emptyAM);
        expectedAM.resetData(emptyAM);
        Activity existingActivity = new Activity("bla bla bla");
        model.addActivity(existingActivity);
        
        Activity newActivity = new Activity("bla");
        expectedAM.addActivity(newActivity);

        assertCommandBehavior("update 1     bla",
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, newActivity.getName()),
                expectedAM,
                expectedAM.getActivityList());
    
        // setup expectations for deadline activity
        expectedAM.resetData(emptyAM);
        model.resetData(emptyAM);
        Activity existingDeadline = new Activity("deadline", helper.getReferenceDateString());
        model.addActivity(existingDeadline);
        
        Activity newDeadline = new Activity("new deadline", helper.getReferenceDateString());
        expectedAM.addActivity(newDeadline);
      
        assertCommandBehavior("update 1    new deadline    by    " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, newDeadline.getName()),
                expectedAM,
                expectedAM.getActivityList()); 
        
     // setup expectations for event activity
        expectedAM.resetData(emptyAM);
        model.resetData(emptyAM);
        Activity existingEvent = new Activity("event", helper.getReferenceDateString(), helper.getReferenceDateString());
        model.addActivity(existingEvent);
        
        Activity newEvent = new Activity("new event", helper.getReferenceDateString(), helper.getReferenceDateString());
        expectedAM.addActivity(newEvent);
      
        assertCommandBehavior("update 1    new event   from   " + helper.getReferenceDateString() + "   to   " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, newEvent.getName()),
                expectedAM,
                expectedAM.getActivityList());
    }
    
    @Test
    public void execute_update_parseKeywordsCorrectly() throws Exception {
    	TestDataHelper helper = new TestDataHelper();
    	ActivityManager expectedAM = new ActivityManager();
    	ActivityManager emptyAM = new ActivityManager();
    	
        // able to update deadline activity with keywords (on/by) (without spaces)
    	model.resetData(emptyAM);
    	expectedAM.resetData(emptyAM);
    	Activity existingDeadline = new Activity("Presentation", helper.getReferenceDateString());
    	model.addActivity(existingDeadline);
        
        Activity newDeadline = new Activity("Presentation Ruby", helper.getReferenceDateString());
        expectedAM.addActivity(newDeadline);
        
        assertCommandBehavior("update 1 Presentation Ruby on " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, newDeadline.getName()),
                expectedAM,
                expectedAM.getActivityList());
        
        // able to update deadline activity with keywords (on/by) (with spaces)
        expectedAM.resetData(emptyAM);
        newDeadline = new Activity("read Village by the Sea", helper.getReferenceDateString());
        expectedAM.addActivity(newDeadline);
        assertCommandBehavior("update 1 read Village by the Sea \"on\" " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, newDeadline.getName()),
                expectedAM,
                expectedAM.getActivityList());
        
        // able to update event activity with keywords (from/to) (without spaces)
        model.resetData(emptyAM);
        expectedAM.resetData(emptyAM);
        Activity existingEvent = new Activity("Tom and jerry", helper.getReferenceDateString(), helper.getReferenceDateString());
        model.addActivity(existingEvent);
        
        Activity newEvent = new Activity("The fromance of tom and jerry", helper.getReferenceDateString(), helper.getReferenceDateString());
        expectedAM.addActivity(newEvent);
       
        assertCommandBehavior("update 1 The fromance of tom and jerry from " + helper.getReferenceDateString() 
                              + " to " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, newEvent.getName()),
                expectedAM,
                expectedAM.getActivityList());
        
        // able to update event activity with keywords (from/to) (with spaces)
        expectedAM.resetData(emptyAM);
        newEvent = new Activity("Love from Paris", helper.getReferenceDateString(), helper.getReferenceDateString());
        expectedAM.addActivity(newEvent);
        
        assertCommandBehavior("update 1 Love from Paris \"from\" " + helper.getReferenceDateString() 
                              + " to " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, newEvent.getName()),
                expectedAM,
                expectedAM.getActivityList());
        
        // able to update event activity with keywords (from/to) (with spaces)
        expectedAM.resetData(emptyAM);
        newEvent = new Activity("Train to Busan", helper.getReferenceDateString(), helper.getReferenceDateString());
        expectedAM.addActivity(newEvent);
        
        assertCommandBehavior("update 1 Train to Busan from " + helper.getReferenceDateString() 
                              + " \"to\" " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, newEvent.getName()),
                expectedAM,
                expectedAM.getActivityList());
        
        // able to update event activity with keywords (from/to) (with spaces)
        expectedAM.resetData(emptyAM);
        newEvent = new Activity("Movie: from Jupiter to Mars", helper.getReferenceDateString(), helper.getReferenceDateString());
        expectedAM.addActivity(newEvent);
        
        assertCommandBehavior("update 1 Movie: from Jupiter to Mars \"from\" " + helper.getReferenceDateString() 
                              + " \"to\" " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, newEvent.getName()),
                expectedAM,
                expectedAM.getActivityList());
    }
    
    @Test
    public void execute_update_changeActivityTypeCorrectly() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        ActivityManager expectedAM = new ActivityManager();
        ActivityManager emptyAM = new ActivityManager();
        Activity floating = new Activity("Floating");
        Activity deadline = new Activity("Deadline", helper.getReferenceDateString());
        Activity event = new Activity("Event", helper.getReferenceDateString(), helper.getReferenceDateString());
        
        // able to update from floating to deadline
        model.addActivity(floating);
        expectedAM.addActivity(deadline);
        assertCommandBehavior("update 1 Deadline on " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, deadline.getName()),
                expectedAM,
                expectedAM.getActivityList());
        
        // able to update from deadline to event
        expectedAM.resetData(emptyAM);
        expectedAM.addActivity(event);
        assertCommandBehavior("update 1 Event from " + helper.getReferenceDateString() + " to " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, event.getName()),
                expectedAM,
                expectedAM.getActivityList());
        
        
        // able to update from floating to event
        model.resetData(emptyAM);
        expectedAM.resetData(emptyAM);
        model.addActivity(floating);
        expectedAM.addActivity(event);
        assertCommandBehavior("update 1 Event from " + helper.getReferenceDateString() + " to " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, event.getName()),
                expectedAM,
                expectedAM.getActivityList());
        
        // able to update from event to deadline
        expectedAM.resetData(emptyAM);
        expectedAM.addActivity(deadline);
        assertCommandBehavior("update 1 Deadline on " + helper.getReferenceDateString(),
                String.format(UpdateCommand.MESSAGE_UPDATE_ACTIVITY_SUCCESS, deadline.getName()),
                expectedAM,
                expectedAM.getActivityList());
    }

    @Test
    public void execute_search_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE);
        assertCommandBehavior("search ", expectedMessage);
    }

    @Test
    public void execute_search_matchNameSuccess() throws Exception {
        // should match if only full search name is found in part of activity name, case insensitive
        TestDataHelper helper = new TestDataHelper();
        Activity pTarget1 = new Activity("bla bla KEY bla");
        Activity pTarget2 = new Activity("bla KEY bla bceofeia", helper.getReferenceDateString());
        Activity pTarget3 = new Activity("bla key bla", helper.getReferenceDateString(), helper.getReferenceDateString());
        Activity p1 = new Activity("KE Y");
        Activity p2 = new Activity("KEYKEYKEY sduauo", helper.getReferenceDateString());
        Activity p3 = new Activity("KEXY", helper.getReferenceDateString(), helper.getReferenceDateString());

        List<Activity> sixActivities = helper.generateActivityList(p1, pTarget1, p2, pTarget2, p3, pTarget3);
        ActivityManager expectedAB = helper.generateActivityManager(sixActivities);
        List<Activity> expectedList = helper.generateActivityList(pTarget1, pTarget2, pTarget3);
        helper.addToModel(model, sixActivities);

        assertCommandBehavior("search KEY",
                Command.getMessageForActivityListShownSummary(expectedList.size()),
                expectedAB,
                expectedList);
    }
    
    @Test
    public void execute_search_matchesIfWithinDateSuccess() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Activity pTarget1 = new Activity("deadline", "today");
        Activity pTarget2 = new Activity("event", "yesterday", "tomorow");
        Activity p1 = new Activity("no match");
        Activity p2 = new Activity("deadline", "next week");
        Activity p3 = new Activity("event", "next week", "2 week later");
        
        List<Activity> activities = helper.generateActivityList(pTarget1, p1, pTarget2, p2, p3);
        ActivityManager expectedAB = helper.generateActivityManager(activities);
        List<Activity> expectedList = helper.generateActivityList(pTarget1, pTarget2);
        helper.addToModel(model, activities);
        
        assertCommandBehavior("search today",
                Command.getMessageForActivityListShownSummary(expectedList.size()),
                expectedAB,
                expectedList);
        
        assertCommandBehavior("search today to tomorrow",
                Command.getMessageForActivityListShownSummary(expectedList.size()),
                expectedAB,
                expectedList);
    }
    
    @Test
    public void execute_search_matchesStatusSuccess() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Activity> threeActivities = helper.generateActivityList(3);

        ActivityManager expectedAM = helper.generateActivityManager(threeActivities);
        expectedAM.markActivity(threeActivities.get(0));
        helper.addToModel(model, threeActivities);
        List<Activity> expectedMarkList = helper.generateActivityList(threeActivities.get(0));
        List<Activity> expectedPendingList = helper.generateActivityList(threeActivities.get(1), threeActivities.get(2));
       
        assertCommandBehavior("search pending",
                Command.getMessageForActivityListShownSummary(expectedPendingList.size()),
                expectedAM,
                expectedPendingList);
        
        assertCommandBehavior("search completed",
                Command.getMessageForActivityListShownSummary(expectedMarkList.size()),
                expectedAM,
                expectedMarkList);
    }
    
    @Test
    public void execute_markInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("mark", expectedMessage);
    }

    @Test
    public void execute_markIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("mark");
    }

    @Test
    public void execute_mark_marksCorrectActivity() throws Exception {
        TestDataHelper helper = new TestDataHelper();
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
        assertIndexNotFoundBehaviorForCommand("unmark");
    }

    @Test
    public void execute_unmark_unmarksCorrectActivity() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Activity> threeActivities = helper.generateActivityList(3);

        ActivityManager expectedAM = helper.generateActivityManager(threeActivities);
        expectedAM.unmarkActivity(threeActivities.get(1));
        helper.addToModel(model, threeActivities);
       
        assertCommandBehavior("unmark 2",
                String.format(UnmarkCommand.MESSAGE_UNMARK_ACTIVITY_SUCCESS, threeActivities.get(1).getName()),
                expectedAM,
                expectedAM.getActivityList());
    }
    
    @Test
    public void execute_store_storeToCorrectLocation () throws Exception {
    	String testDataFileLocation = "/data/RemindarooTest.xml";
    	assertCommandBehavior("store " + testDataFileLocation, String.format(StoreCommand.MESSAGE_STORE_FILE_SUCCESS, testDataFileLocation));
    }

    @Test
    public void execute_store_incorrectExtension () throws Exception {
    	String testDataFileLocation = "/data/RemindarooTest.txt";
    	assertCommandBehavior("store " + testDataFileLocation, String.format(MESSAGE_INVALID_COMMAND_FORMAT, StoreCommand.MESSAGE_USAGE));
    }
    
    @Test
    public void execute_store_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, StoreCommand.MESSAGE_USAGE);
        assertCommandBehavior("store", expectedMessage);
    }


    /**
     * A utility class to generate test data.
     */
    class TestDataHelper {
        
        String getReferenceDateString() {
            return "28 Feb 2016 00:00:00";
        }

        /**
         * Generates a valid activity using the given seed.
         * Running this function with the same parameter values guarantees the returned activity will have the same state.
         * Each unique seed will generate a unique Activity object.
         *
         * @param seed used to generate the activity data field values
         */
        Activity generateActivity(int seed) throws Exception {
            return new Activity("Activity " + seed);
        }

        /** Generates the correct add command based on the activity given */
//        String generateAddCommand(Activity activity) {
//            StringBuffer cmd = new StringBuffer();
//
//            cmd.append("add ");
//
//            cmd.append(activity.getName().toString());
//
////            UniqueTagList tags = p.getTags();
////            for(Tag t: tags){
////                cmd.append(" t/").append(t.tagName);
////            }
//
//            return cmd.toString();
//        }

        /**
         * Generates an ActivityManager with auto-generated activities.
         */
        ActivityManager generateActivityManager(int numGenerated) throws Exception{
            ActivityManager activityManager = new ActivityManager();
            addToActivityManager(activityManager, numGenerated);
            return activityManager;
        }

        /**
         * Generates an ActivityManager based on the list of activities given.
         */
        ActivityManager generateActivityManager(List<Activity> activities) throws Exception{
            ActivityManager activityManager = new ActivityManager();
            addToActivityManager(activityManager, activities);
            return activityManager;
        }

        /**
         * Adds auto-generated Activity objects to the given ActivityManager
         * @param activityManager The ActivityManager to which the activities will be added
         */
        void addToActivityManager(ActivityManager activityManager, int numGenerated) throws Exception{
            addToActivityManager(activityManager, generateActivityList(numGenerated));
        }

        /**
         * Adds the given list of activities to the given ActivityManager
         */
        void addToActivityManager(ActivityManager activityManager, List<Activity> activitiesToAdd) throws Exception{
            for(Activity p: activitiesToAdd){
                activityManager.addActivity(p);
            }
        }

        /**
         * Adds auto-generated Activity objects to the given model
         * @param model The model to which the activities will be added
         */
        void addToModel(Model model, int numGenerated) throws Exception{
            addToModel(model, generateActivityList(numGenerated));
        }

        /**
         * Adds the given list of activities to the given model
         */
        void addToModel(Model model, List<Activity> activtiesToAdd) throws Exception{
            for(Activity p: activtiesToAdd){
                model.addActivity(p);
            }
        }

        /**
         * Generates a list of activities based on the flags.
         */
        List<Activity> generateActivityList(int numGenerated) throws Exception{
            List<Activity> activity = new ArrayList<>();
            for(int i = 1; i <= numGenerated; i++){
                activity.add(generateActivity(i));
            }
            return activity;
        }

        List<Activity> generateActivityList(Activity... activities) {
            return Arrays.asList(activities);
        }
    }
}
