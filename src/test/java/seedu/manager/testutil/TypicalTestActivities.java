package seedu.manager.testutil;

import seedu.manager.commons.exceptions.IllegalValueException;
import seedu.manager.model.ActivityManager;
import seedu.manager.model.activity.*;

//@@author A0144704L
/**
 * Utility class to get test activities
 */
public class TypicalTestActivities {

    public TestActivity groceries, reading, guitar, tidy, paint, movie, dog, plane, hotel, assignment, talk;

    public TypicalTestActivities() {
        try {
            // Automated activities
            groceries =  new ActivityBuilder().withName("Buy groceries").build();
            reading = new ActivityBuilder().withName("Read favourite book").build();
            guitar = new ActivityBuilder().withName("Practice playing guitar").build();
            paint = new ActivityBuilder().withName("Paint room wall (blue)").build();
            movie =  new ActivityBuilder().withNameAndStatus("Watch Lord of the Rings", false).build();
            // TODO: set to true to test for mark when GUI is more stable
            dog =  new ActivityBuilder().withNameAndStatus("Walk the dog", false).build();
            // Deadline task example
            assignment = new ActivityBuilder().withNameAndTime("essay assignment", "1 Dec 2016 23:59:59").build();
           // Event task example
            talk = new ActivityBuilder().withNameAndStartEndTime("HTML5 talk", "2 Dec 2016 10:00:00", "2 Dec 2016, 12:00:00").build();
            
            // Manual activities
            tidy = new ActivityBuilder().withName("Tidy study desk").build();
            plane = new ActivityBuilder().withNameAndStatus("Buy plane ticket to Paris", false).build(); 
            hotel = new ActivityBuilder().withNameAndStatus("Book hotel in Paris", true).build();
            
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public void loadActivityManagerWithSampleData(ActivityManager am) {
        am.addActivity(new Activity(this.groceries));
        am.addActivity(new Activity(this.reading));
        am.addActivity(new Activity(this.guitar));
        am.addActivity(new Activity(this.paint));
        am.addActivity(new Activity(this.movie));
        am.addActivity(new Activity(this.dog));
        am.addActivity(new Activity(this.assignment));
        am.addActivity(new Activity(this.talk));
    }
    
    public TestActivity[] getTypicalFloatingActivities() {
        return new TestActivity[]{groceries, reading, guitar, paint, movie, dog};
    }
    
    public TestActivity[] getTypicalScheduleActivities() {
        return new TestActivity[]{assignment, talk};
    }

    public ActivityManager getTypicalActivityManager(){
        ActivityManager am = new ActivityManager();
        loadActivityManagerWithSampleData(am);
        return am;
    }
}
