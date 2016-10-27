package seedu.manager.ui;

import java.util.Calendar;
import java.util.Date;

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
    @FXML
    private Label status;
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
        String statusText = activity.getStatus().toString();
        status.setText(statusText);
        if (activity.getStatus().isCompleted()) { 
        	status.setStyle("-fx-text-fill: darkcyan");
        } else {
        	status.setStyle("-fx-text-fill: maroon");
        }
        
        AMDate checkExpired = null;
        if (activity.getType().equals(ActivityType.DEADLINE)) {
            dateTime.setText(generateDateTimeString(activity.getDateTime()));
            checkExpired = activity.getDateTime();
        } else if (activity.getType().equals(ActivityType.EVENT)) {
            dateTime.setText(generateDateTimeString(activity.getDateTime()));
            endDateTime.setText(generateDateTimeString(activity.getEndDateTime()));
            checkExpired = activity.getEndDateTime();
        }
        if (checkExpired != null) {
	        if (isExpired(checkExpired) && !activity.getStatus().isCompleted()) { 
	        	cardPane.setStyle("-fx-background-color: derive(indianred, 70%); -fx-border-width: 0.5; -fx-border-style: groove; -fx-border-color: grey;"); 
	        }
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
    
    public boolean isExpired(AMDate date) {
    	assert date != null;
    	AMDate today = new AMDate("today");
    	if (today.getTime() > date.getTime())
    		return true;
    	return false;
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
