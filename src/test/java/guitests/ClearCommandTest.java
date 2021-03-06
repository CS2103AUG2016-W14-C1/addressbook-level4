package guitests;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

//@@author A0139797E
public class ClearCommandTest extends ActivityManagerGuiTest {

    @Test
    public void clear() {

        //verify a non-empty list can be cleared
        assertTrue(floatingActivityListPanel.isListMatching(ta.getTypicalFloatingActivities()));
        assertClearCommandSuccess();

        //verify other commands can work after a clear command
        commandBox.runCommand(ta.groceries.getAddCommand());
        assertTrue(floatingActivityListPanel.isListMatching(ta.groceries));
        commandBox.runCommand("delete 1");
        assertListSize(floatingActivityListPanel.getNumberOfActivities(), 0);
        //verify clear command works when the list is empty
        assertClearCommandSuccess();
    }

    private void assertClearCommandSuccess() {
        commandBox.runCommand("clear");
        assertListSize(floatingActivityListPanel.getNumberOfActivities(), 0);
        assertResultMessage("Your activities have been removed!");
    }
}
