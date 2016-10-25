//@@author A0144704L
package seedu.manager.ui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.manager.commons.core.LogsCenter;
import seedu.manager.commons.events.ui.ActivityPanelSelectionChangedEvent;
import seedu.manager.commons.events.ui.ActivityPanelUpdateEvent;
import seedu.manager.model.activity.Activity;
import seedu.manager.ui.ActivityListPanel.ActivityListViewCell;

import java.util.logging.Logger;

/**
 * Panel containing the list of persons.
 */
public class FloatingListPanel extends UiPart{
    private final Logger logger = LogsCenter.getLogger(FloatingListPanel.class);
    private static final String FXML = "FloatingListPanel.fxml";
    private VBox panel;
    private AnchorPane placeHolderPane;

    @FXML
    private ListView<Activity> floatingListView;

    public FloatingListPanel() {
        super();
    }
    
    @Override
    public void setNode(Node node) {
        panel = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }
    
    public static FloatingListPanel load(Stage primaryStage, AnchorPane activityListPlaceholder,
    									ObservableList<Activity> observableList, int indexOffset) {
		FloatingListPanel activityListPanel =
				UiPartLoader.loadUiPart(primaryStage, activityListPlaceholder, new FloatingListPanel());
		activityListPanel.configure(observableList, indexOffset);
		return activityListPanel;
	}
    
    private void configure(ObservableList<Activity> observableList, int indexOffset) {
        setConnections(observableList, indexOffset);
        addToPlaceholder();
    }
    
    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(panel);
    }

    private void setConnections(ObservableList<Activity> observableList, int indexOffset) {
        floatingListView.setItems(observableList);
        floatingListView.setCellFactory(listView -> new ActivityListViewCell(indexOffset));
        setEventHandlerForSelectionChangeEvent();
        setEventHandlerForUpdateEvent();
    }
    
    private void setEventHandlerForSelectionChangeEvent() {
    	floatingListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                logger.fine("Selection in person list panel changed to : '" + newValue + "'");
                raise(new ActivityPanelSelectionChangedEvent(newValue));
            }
        });
    }

    private void setEventHandlerForUpdateEvent() {
    	floatingListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                logger.fine("Activity has been updated to : '" + newValue + "'");
                raise(new ActivityPanelUpdateEvent(newValue));
            }
        });
    }
    
    //@@author A0144704L
    public void updateActivityListPanel(ObservableList<Activity> observableList, int indexOffset) {
        floatingListView.setItems(observableList);
    	floatingListView.setCellFactory(listView -> new ActivityListViewCell(indexOffset));
    }
    
    //@@author A0144704L
    public void updateActivityCard(Activity newActivity, int indexOffset) {
        // Refresh activity card cells to update GUI
    	floatingListView.setCellFactory(listView -> new ActivityListViewCell(indexOffset));
    }
    
    public void scrollTo(int index) {
        Platform.runLater(() -> {
        	floatingListView.scrollTo(index);
        	floatingListView.getSelectionModel().clearAndSelect(index);
        });
    }
    
    class ActivityListViewCell extends ListCell<Activity> {
    	private int indexOffset;

        public ActivityListViewCell(int indexOffset) {
        	this.indexOffset = indexOffset;
        }

        @Override
        //@@author A0144881Y
        protected void updateItem(Activity activity, boolean empty) {
            super.updateItem(activity, empty);

            if (empty || activity == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(ActivityCard.load(activity, getIndex() + 1 + indexOffset).getLayout());
            }
        }
    }
}

