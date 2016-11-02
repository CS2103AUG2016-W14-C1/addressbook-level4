package guitests.guihandles;

import guitests.GuiRobot;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import seedu.manager.TestApp;

import java.util.Arrays;

/**
 * Provides a handle to the main menu of the app.
 */
public class MainMenuHandle extends GuiHandle {
    public MainMenuHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);
    }

    public HelpWindowHandle openHelpWindowUsingAccelerator() {
        useF1Accelerator();
        return new HelpWindowHandle(guiRobot, primaryStage);
    }
    
    public void exitMainWindowUsingAccelerator() {
        useExitAccelerator();
    }
    
    private void useExitAccelerator() {
       guiRobot.push(new KeyCodeCombination(KeyCode.ESCAPE, KeyCombination.CONTROL_DOWN));
       guiRobot.sleep(500);
    }
    
    private void useF1Accelerator() {
        guiRobot.push(KeyCode.F1);
        guiRobot.sleep(500);
    }
}
