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
    //@@author A0139797E
    public void initialize() {
        name.setText(activity.getName());
        id.setText(displayedIndex + ". ");
        dateTime.setText(""); // default
        endDateTime.setText(""); // default
        if (activity.getType().equals(ActivityType.DEADLINE)) {
            dateTime.setText(generateDateTimeString(activity.getDateTime()));
        } else if (activity.getType().equals(ActivityType.EVENT)) {
            dateTime.setText(generateDateTimeString(activity.getDateTime()));
            endDateTime.setText(generateDateTimeString(activity.getEndDateTime()));
        }
    }
    
    //@@author A0135730M
    private String generateDateTimeString(AMDate dateTime) {
        assert dateTime == null;
        
        return dateTime.getDayOfWeek() + DATE_DELIMITER +
               dateTime.getDay() + DATE_DELIMITER +
               dateTime.getMonth() + DATETIME_DELIMITER + 
               dateTime.getHour() + TIME_DELIMITER +
               dateTime.getMinutes();
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
