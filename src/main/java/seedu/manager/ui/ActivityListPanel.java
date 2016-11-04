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
import seedu.manager.model.activity.Activity;

import java.util.logging.Logger;

/**
 * Panel containing the list of activities.
 */
public class ActivityListPanel extends UiPart {
    private final Logger logger = LogsCenter.getLogger(ActivityListPanel.class);
    private static final String FXML = "ActivityListPanel.fxml";
    private VBox panel;
    private AnchorPane placeHolderPane;

    @FXML
    private ListView<Activity> activityListView;

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

    public static ActivityListPanel load(Stage primaryStage, AnchorPane activityListPlaceholder,
                                       ObservableList<Activity> observableList, int indexOffset) {
        ActivityListPanel activityListPanel =
                UiPartLoader.loadUiPart(primaryStage, activityListPlaceholder, new ActivityListPanel());
        activityListPanel.configure(observableList, indexOffset);
        return activityListPanel;
    }

    private void configure(ObservableList<Activity> observableList, int indexOffset) {
        setConnections(observableList, indexOffset);
        addToPlaceholder();
    }

    private void setConnections(ObservableList<Activity> observableList, int indexOffset) {
        activityListView.setItems(observableList);
        activityListView.setCellFactory(listView -> new ActivityListViewCell(indexOffset));
    }

    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(panel);
    }

    //@@author A0139797E
    public void updateActivityListPanel(ObservableList<Activity> observableList, int indexOffset, int scrollIndex) {
        this.setConnections(observableList, indexOffset);
        this.scrollTo(scrollIndex);
    }
    
    public void scrollTo(int index) {
        Platform.runLater(() -> {
            activityListView.scrollTo(index);
            activityListView.getSelectionModel().clearAndSelect(index);
        });
    }
    //

    class ActivityListViewCell extends ListCell<Activity> {
    	private int indexOffset;

        public ActivityListViewCell(int indexOffset) {
        	this.indexOffset = indexOffset;
        }

        //@@author A0144881Y
        @Override
        protected void updateItem(Activity activity, boolean empty) {
            super.updateItem(activity, empty);

            if (empty || activity == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(ActivityCard.load(activity, getIndex() + 1 + indexOffset).getLayout());
            }
        }
        //
    }

}
