package guitests;

import guitests.guihandles.ActivityCardHandle;
import org.junit.Test;

import seedu.manager.commons.core.Messages;
import seedu.manager.model.activity.ActivityType;
import seedu.manager.testutil.TestActivity;
import seedu.manager.testutil.TestUtil;

import static org.junit.Assert.assertTrue;

public class AddCommandTest extends ActivityManagerGuiTest {

    @Test
    //@@author A0139797E
    public void add() {
        //add one activity
        TestActivity[] currentList = ta.getTypicalFloatingActivities();
        TestActivity activityToAdd = ta.groceries;
//        assertAddSuccess(activityToAdd, currentList);
        currentList = TestUtil.addActivitiesToList(currentList, activityToAdd);

        //add another activity
        activityToAdd = ta.reading;
 //       assertAddSuccess(activityToAdd, currentList);
        currentList = TestUtil.addActivitiesToList(currentList, activityToAdd);
        
      //add another activity
        TestActivity[] currentSchedule = ta.getTypicalScheduleActivities();
        activityToAdd = ta.assignment;
        assertAddSuccess(activityToAdd, currentSchedule);
        currentSchedule = TestUtil.addActivitiesToList(currentSchedule, activityToAdd);
        
        activityToAdd = ta.talk;
        assertAddSuccess(activityToAdd, currentSchedule);
        currentSchedule = TestUtil.addActivitiesToList(currentSchedule, activityToAdd);

        //add to empty list
        commandBox.runCommand("clear");
        assertAddSuccess(ta.groceries);
        
        //invalid command
        commandBox.runCommand("adds Johnny");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void assertAddSuccess(TestActivity activityToAdd, TestActivity... currentList) {
        commandBox.runCommand(activityToAdd.getAddCommand());
        
        if (activityToAdd.getType().equals(ActivityType.FLOATING)) {
		    //confirm the new card contains the right data
		    ActivityCardHandle addedCard = floatingActivityListPanel.navigateToActivity(activityToAdd.getName());
		    assertMatching(activityToAdd, addedCard);
		
		    //confirm the list now contains all previous activities plus the new activities
		    TestActivity[] expectedList = TestUtil.addActivitiesToSchedule(currentList, activityToAdd);
		    assertTrue(floatingActivityListPanel.isListMatching(expectedList));
        } else {
	    	//confirm the new card contains the right data
		    ActivityCardHandle addedCard = activityListPanel.navigateToActivity(activityToAdd.getName());
		    assertMatching(activityToAdd, addedCard);
		
		    //confirm the list now contains all previous activities plus the new activities
		    TestActivity[] expectedList = TestUtil.addActivitiesToSchedule(currentList, activityToAdd);
		    assertTrue(activityListPanel.isListMatching(expectedList));
        }
    }
}
