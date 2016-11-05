package guitests;

import guitests.guihandles.HelpWindowHandle;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;

//@@author A0139797E
public class HelpWindowTest extends ActivityManagerGuiTest {

    @Rule
    public ExpectedException thrown= ExpectedException.none();
    
    @Test
    public void openHelpWindow() {

        activityListPanel.clickOnListView();
        
        assertHelpWindowOpen(mainMenu.openHelpWindowUsingAccelerator());
        
        assertHelpWindowOpen(commandBox.runHelpCommand());
    }

    private void assertHelpWindowOpen(HelpWindowHandle helpWindowHandle) {
        assertTrue(helpWindowHandle.isWindowOpen());
        helpWindowHandle.closeWindow();
    } 
}
