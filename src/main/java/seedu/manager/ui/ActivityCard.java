package seedu.manager.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.manager.model.activity.*;

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
    @FXML
    private Label endDateTime;
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
        name.setText(activity.getName());
        id.setText(displayedIndex + ". ");
        if (activity.getClass().equals(DeadlineActivity.class)) {
            dateTime.setText(generateDateTimeString(((DeadlineActivity)activity).getDateTime()));
        } else if (activity.getClass().equals(EventActivity.class)) {
            dateTime.setText(generateDateTimeString(((EventActivity)activity).getDateTime()));
            endDateTime.setText(generateDateTimeString(((EventActivity)activity).getEndDateTime()));
        }
    }
    
    private String generateDateTimeString(AMDate dateTime) {
        if (dateTime == null) {
            return "";
        } else {
            return dateTime.getDayOfWeek() + DATE_DELIMITER +
                   dateTime.getDay() + DATE_DELIMITER +
                   dateTime.getMonth() + DATETIME_DELIMITER + 
                   dateTime.getHour() + TIME_DELIMITER +
                   dateTime.getMinutes();
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
