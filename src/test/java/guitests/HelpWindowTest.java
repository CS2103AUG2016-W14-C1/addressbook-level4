package guitests;

import guitests.guihandles.HelpWindowHandle;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

//@@author A0139797E
public class HelpWindowTest extends ActivityManagerGuiTest {

    @Test
    public void openHelpWindow() {

        activityListPanel.clickOnListView();

        assertHelpWindowOpen(commandBox.runHelpCommand());

    }

    private void assertHelpWindowOpen(HelpWindowHandle helpWindowHandle) {
        assertTrue(helpWindowHandle.isWindowOpen());
        helpWindowHandle.closeWindow();
    } 
}
