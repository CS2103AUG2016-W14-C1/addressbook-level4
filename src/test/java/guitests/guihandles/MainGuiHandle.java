package guitests.guihandles;

import guitests.GuiRobot;
import javafx.stage.Stage;
import seedu.manager.TestApp;

/**
 * Provides a handle for the main GUI.
 */
public class MainGuiHandle extends GuiHandle {

    public MainGuiHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);
    }

    public ActivityListPanelHandle getActivityListPanel() {
        return new ActivityListPanelHandle(guiRobot, primaryStage);
    }
    
    //@@author A0139797E
    public FloatingListPanelHandle getFloatingListPanel() {
        return new FloatingListPanelHandle(guiRobot, primaryStage);
    }

    public ResultDisplayHandle getResultDisplay() {
        return new ResultDisplayHandle(guiRobot, primaryStage);
    }

    public CommandBoxHandle getCommandBox() {
        return new CommandBoxHandle(guiRobot, primaryStage, TestApp.APP_TITLE);
    }

    public MainMenuHandle getMainMenu() {
        return new MainMenuHandle(guiRobot, primaryStage);
    }

}
