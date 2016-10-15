package seedu.manager.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.manager.model.activity.Activity;

public class ActivityCard extends UiPart{

    private static final String FXML = "ActivityListCard.fxml";
    private static final String DATETIME_DELIMITER = " | ";
    private static final String DATE_DELIMITER = " ";
    private static final String TIME_DELIMITER = ":";
    
    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label dateTime;
// TODO: re-instate tags or equivalent when implementation is complete    
//    @FXML
//    private Label tags;

    private Activity activity;
    private int displayedIndex;

    public ActivityCard(){

    }

    public static ActivityCard load(Activity activity, int displayedIndex){
        ActivityCard card = new ActivityCard();
        card.activity = activity;
        card.displayedIndex = displayedIndex;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
        name.setText(activity.name);
        id.setText(displayedIndex + ". ");
        dateTime.setText(generateDateTimeString());
    }
    
    private String generateDateTimeString() {
        if (activity.getDate() == null) {
            return "";
        } else {
            return activity.getDayOfWeek() + DATE_DELIMITER +
                   activity.getDay() + DATE_DELIMITER +
                   activity.getMonth() + DATETIME_DELIMITER + 
                   activity.getHour() + TIME_DELIMITER +
                   activity.getMinutes();
        }
    }

    public HBox getLayout() {
        return cardPane;
    }

    @Override
    public void setNode(Node node) {
        cardPane = (HBox)node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}
