package guitests;

import org.junit.Test;

import seedu.manager.commons.core.Messages;
import seedu.manager.testutil.TestActivity;

import static org.junit.Assert.assertTrue;

public class FindCommandTest extends ActivityManagerGuiTest {

    @Test
    public void find_nonEmptyList() {
        assertFindResult("search None"); //no results
        assertFindResult("search buy", ta.groceries); //multiple results

        //find after deleting one result
        commandBox.runCommand("delete 1");
        assertFindResult("search buy");
    }

    @Test
    public void find_emptyList(){
        commandBox.runCommand("clear");
        assertFindResult("search None"); //no results
    }

    @Test
    public void find_invalidCommand_fail() {
        commandBox.runCommand("searchactivity");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void assertFindResult(String command, TestActivity... expectedHits ) {
        commandBox.runCommand(command);
        assertListSize(floatingActivityListPanel.getNumberOfActivities(), expectedHits.length);
        assertResultMessage(expectedHits.length + " activities listed!");
        assertTrue(floatingActivityListPanel.isListMatching(expectedHits));
    }
}
