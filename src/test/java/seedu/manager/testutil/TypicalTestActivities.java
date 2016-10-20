package seedu.manager.testutil;

import seedu.manager.commons.exceptions.IllegalValueException;
import seedu.manager.model.ActivityManager;
import seedu.manager.model.activity.*;

/**
 *
 */
public class TypicalTestActivities {

    public static TestActivity groceries, reading, guitar, tidy, paint, movie, dog, plane, hotel;

    public TypicalTestActivities() {
        try {
            // Automated activities
            groceries =  new ActivityBuilder().withName("Buy groceries").build();
            reading = new ActivityBuilder().withName("Read favourite book").build();
            guitar = new ActivityBuilder().withName("Practice playing guitar").build();
            paint = new ActivityBuilder().withName("Paint room wall (blue)").build();
            movie =  new ActivityBuilder().withNameandStatus("Watch Lord of the Rings", false).build();
            dog =  new ActivityBuilder().withNameandStatus("Walk the dog", true).build();
           
            
            // Manual activities
            tidy = new ActivityBuilder().withName("Tidy study desk").build();
            plane = new ActivityBuilder().withNameandStatus("Buy plane ticket to Paris", false).build(); 
            hotel = new ActivityBuilder().withNameandStatus("Book hotel in Paris", true).build();
            
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadActivityManagerWithSampleData(ActivityManager am) {

//        try {
            am.addActivity(new FloatingActivity(groceries));
            am.addActivity(new FloatingActivity(reading));
            am.addActivity(new FloatingActivity(guitar));
            am.addActivity(new FloatingActivity(paint));
            am.addActivity(new FloatingActivity(movie));
            am.addActivity(new FloatingActivity(dog));
//        } catch (UniquePersonList.DuplicatePersonException e) {
//            assert false : "not possible";
//        }
    }

    public TestActivity[] getTypicalActivities() {
        return new TestActivity[]{groceries, reading, guitar, paint, movie, dog};
    }

    public ActivityManager getTypicalActivityManager(){
        ActivityManager am = new ActivityManager();
        loadActivityManagerWithSampleData(am);
        return am;
    }
}
