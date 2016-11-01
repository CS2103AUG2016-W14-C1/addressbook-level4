package guitests;

import org.junit.Test;

import seedu.manager.testutil.TestActivity;
import seedu.manager.testutil.TestUtil;

import static org.junit.Assert.assertTrue;
import static seedu.manager.logic.commands.DeleteCommand.MESSAGE_DELETE_ACTIVITY_SUCCESS;

public class DeleteCommandTest extends ActivityManagerGuiTest {

    @Test
    //@@author A0139797E
    public void delete() {

        //delete the first in the list
        TestActivity[] currentList = ta.getTypicalFloatingActivities();
        int targetIndex = 2;
        assertDeleteSuccess(targetIndex, currentList);

        //delete the last in the list
        currentList = TestUtil.removeActivityFromList(currentList, getFloatingIndex(targetIndex));
        targetIndex = currentList.length + ta.getTypicalDeadlineActivities().length;
        assertDeleteSuccess(targetIndex, currentList);

        //delete from the middle of the list
        currentList = TestUtil.removeActivityFromList(currentList, getFloatingIndex(targetIndex));
        targetIndex = currentList.length/2;
        assertDeleteSuccess(targetIndex, currentList);

        //invalid index
        commandBox.runCommand("delete " + (currentList.length + ta.getTypicalDeadlineActivities().length));
        assertResultMessage("The activity index provided is invalid");

    }

    //@@author A0139797E
    private int getFloatingIndex(int index) {
        return index - ta.getTypicalDeadlineActivities().length;
    }
    
    /**
     * Runs the delete command to delete the activity at specified index and confirms the result is correct.
     * @param targetIndexOneIndexed e.g. to delete the first person in the list, 1 should be given as the target index.
     * @param currentList A copy of the current list of persons (before deletion).
     */
    public void assertDeleteSuccess(int targetIndexOneIndexed, final TestActivity[] currentList) {
        int floatingIndex = getFloatingIndex(targetIndexOneIndexed);
        TestActivity activityToDelete = currentList[floatingIndex-1]; //-1 because array uses zero indexing
        TestActivity[] expectedRemainder = TestUtil.removeActivityFromList(currentList, floatingIndex);
        
        commandBox.runCommand("delete " + targetIndexOneIndexed);

        //confirm the list now contains all previous activities except the deleted activity
        assertTrue(floatingActivityListPanel.isListMatching(expectedRemainder));

        //confirm the result message is correct
        assertResultMessage(String.format(MESSAGE_DELETE_ACTIVITY_SUCCESS, activityToDelete));
    }

}
