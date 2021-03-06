package seedu.manager.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.manager.commons.core.Config;
import seedu.manager.commons.core.GuiSettings;
import seedu.manager.commons.events.ui.ExitAppRequestEvent;
import seedu.manager.logic.Logic;
import seedu.manager.model.UserPrefs;
import seedu.manager.model.activity.AMDate;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart {

    private static final String ICON = "/images/logo.png";
    private static final String FXML = "MainWindow.fxml";
    public static final int MIN_HEIGHT = 768;
    public static final int MIN_WIDTH = 1024;

    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private ActivityListPanel activityListPanel;
    private FloatingListPanel floatingActivityListPanel;
    private ResultDisplay resultDisplay;
    private StatusBarFooter statusBarFooter;
    private CommandBox commandBox;
    private Config config;
    private UserPrefs userPrefs;

    // Handles to elements of this Ui container
    private VBox rootLayout;
    private Scene scene;

    private String activityManagerName;
    
    @FXML
    private Label currentTime;
    
    @FXML
    private AnchorPane commandBoxPlaceholder;
    
    @FXML
    private SplitPane splitPane;
    
    @FXML
    private MenuItem exitMenuItem;
    
    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private AnchorPane activityListPanelPlaceholder;
    
    @FXML
    private AnchorPane floatingActivityListPanelPlaceholder;

    @FXML
    private AnchorPane resultDisplayPlaceholder;

    @FXML
    private AnchorPane statusbarPlaceholder;


    public MainWindow() {
        super();
    }

    @Override
    public void setNode(Node node) {
        rootLayout = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    public static MainWindow load(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {

        MainWindow mainWindow = UiPartLoader.loadUiPart(primaryStage, new MainWindow());
        mainWindow.configure(config.getAppTitle(), config.getActivityManagerName(), config, prefs, logic);
        return mainWindow;
    }

    private void configure(String appTitle, String activityManagerName, Config config, UserPrefs prefs,
                           Logic logic) {

        //Set dependencies
        this.logic = logic;
        this.activityManagerName = activityManagerName;
        this.config = config;
        this.userPrefs = prefs;

        //Configure the UI
        setTitle(appTitle);
        setIcon(ICON);
        setWindowMinSize();
        setWindowDefaultSize(prefs);
        scene = new Scene(rootLayout);
        primaryStage.setScene(scene);

        setAccelerators();
    }

    private void setAccelerators() {
        helpMenuItem.setAccelerator(KeyCombination.valueOf("F1"));
        exitMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.ESCAPE, KeyCombination.CONTROL_DOWN));
    }

    //@@author A0144881Y
    public void fillInnerParts() {
    	splitPane.setDividerPosition(1, 0.5);
    	AMDate today = new AMDate("today");
    	currentTime.setText(today.getDayWithExtension() +" "+ today.getMonthFull() + ", " + today.getDayOfWeekFull());
        activityListPanel = ActivityListPanel.load(primaryStage, getActivityListPlaceholder(), logic.getFilteredDeadlineAndEventList(), 0);
        floatingActivityListPanel = FloatingListPanel.load(primaryStage, getFloatingActivityListPlaceholder(), logic.getFilteredFloatingActivityList(), logic.getFilteredDeadlineAndEventList().size());
        resultDisplay = ResultDisplay.load(primaryStage, getResultDisplayPlaceholder());
        statusBarFooter = StatusBarFooter.load(primaryStage, getStatusbarPlaceholder(), config.getActivityManagerFilePath());
        commandBox = CommandBox.load(primaryStage, getCommandBoxPlaceholder(), resultDisplay, logic);
    }

    private AnchorPane getCommandBoxPlaceholder() {
        return commandBoxPlaceholder;
    }

    private AnchorPane getStatusbarPlaceholder() {
        return statusbarPlaceholder;
    }

    private AnchorPane getResultDisplayPlaceholder() {
        return resultDisplayPlaceholder;
    }

    public AnchorPane getActivityListPlaceholder() {
        return activityListPanelPlaceholder;
    }
    
    //@@author A0144704L
    public AnchorPane getFloatingActivityListPlaceholder() {
    	return floatingActivityListPanelPlaceholder;
    }
    
    public void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the default size based on user preferences.
     */
    protected void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    private void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    public GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    @FXML
    public void handleHelp() {
        HelpWindow helpWindow = HelpWindow.load(primaryStage);
        helpWindow.show();
    }

    public void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    public ActivityListPanel getActivityListPanel() {
        return this.activityListPanel;
    }
    
    public FloatingListPanel getFloatingActivityListPanel() {
    	return this.floatingActivityListPanel;
    }
}
