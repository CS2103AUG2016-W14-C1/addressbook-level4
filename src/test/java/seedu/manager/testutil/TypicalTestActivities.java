package seedu.manager.testutil;

import seedu.manager.commons.exceptions.IllegalValueException;
import seedu.manager.model.ActivityManager;
import seedu.manager.model.activity.*;

/**
 *
 */
public class TypicalTestActivities {

    public TestActivity groceries, reading, guitar, tidy, paint, movie, dog, plane, hotel, assignment;

    public TypicalTestActivities() {
        try {
            // Automated activities
            groceries =  new ActivityBuilder().withName("Buy groceries").build();
            reading = new ActivityBuilder().withName("Read favourite book").build();
            guitar = new ActivityBuilder().withName("Practice playing guitar").build();
            paint = new ActivityBuilder().withName("Paint room wall (blue)").build();
            movie =  new ActivityBuilder().withNameandStatus("Watch Lord of the Rings", false).build();
            // TODO: set to true to test for mark when GUI is more stable
            dog =  new ActivityBuilder().withNameandStatus("Walk the dog", false).build();
            // Deadline task example
            assignment = new ActivityBuilder().withNameandTime("essay assignment", "1 Dec 2016 23:59:59").build();
           
            
            // Manual activities
            tidy = new ActivityBuilder().withName("Tidy study desk").build();
            plane = new ActivityBuilder().withNameandStatus("Buy plane ticket to Paris", false).build(); 
            hotel = new ActivityBuilder().withNameandStatus("Book hotel in Paris", true).build();
            
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
    }

    public TestActivity[] getTypicalActivities() {
        return new TestActivity[]{groceries, reading, guitar, paint, movie, dog, assignment};
    }
    
    public TestActivity[] getTypicalFloatingActivities() {
        return new TestActivity[]{groceries, reading, guitar, paint, movie, dog};
    }
    
    public TestActivity[] getTypicalDeadlineActivities() {
        return new TestActivity[]{assignment};
    }

    public ActivityManager getTypicalActivityManager(){
        ActivityManager am = new ActivityManager();
        loadActivityManagerWithSampleData(am);
        return am;
    }
}
