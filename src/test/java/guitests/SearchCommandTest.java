package guitests;

import org.junit.Test;

import seedu.manager.commons.core.Messages;
import seedu.manager.testutil.TestActivity;

import static org.junit.Assert.assertTrue;

public class SearchCommandTest extends ActivityManagerGuiTest {

    @Test
    public void search_nonEmptyList() {
        assertSearchResult("search None"); //no results
        assertSearchResult("search buy", ta.groceries); //multiple results

        //find after deleting one result
        commandBox.runCommand("delete 1");
        assertSearchResult("search buy");
    }

    @Test
    public void search_emptyList(){
        commandBox.runCommand("clear");
        assertSearchResult("search None"); //no results
    }

    @Test
    public void search_invalidCommand_fail() {
        commandBox.runCommand("searchactivity");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void assertSearchResult(String command, TestActivity... expectedHits ) {
        commandBox.runCommand(command);
        assertListSize(floatingActivityListPanel.getNumberOfActivities(), expectedHits.length);
        assertResultMessage(expectedHits.length + " activities listed!");
        assertTrue(floatingActivityListPanel.isListMatching(expectedHits));
    }
}
