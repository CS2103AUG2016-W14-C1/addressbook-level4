package seedu.manager.storage;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.manager.commons.exceptions.DataConversionException;
import seedu.manager.commons.util.FileUtil;
import seedu.manager.model.ActivityManager;
import seedu.manager.model.ReadOnlyActivityManager;
import seedu.manager.model.activity.*;
import seedu.manager.storage.XmlActivityManagerStorage;
import seedu.manager.testutil.TypicalTestActivities;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class XmlActivityManagerStorageTest {
    private static String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlActivityManagerStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readActivityManager_nullFilePath_assertionFailure() throws Exception {
        thrown.expect(AssertionError.class);
        readActivityManager(null);
    }

    private java.util.Optional<ReadOnlyActivityManager> readActivityManager(String filePath) throws Exception {
        return new XmlActivityManagerStorage(filePath).readActivityManager(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readActivityManager("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readActivityManager("NotXmlFormatActivityManager.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    //@@author A0144881Y
    @Test
    public void readAndSaveActivityManager_allInOrder_success() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempActivityManager.xml";
        TypicalTestActivities ta = new TypicalTestActivities();
        ActivityManager original = ta.getTypicalActivityManager();
        XmlActivityManagerStorage xmlActivityManagerStorage = new XmlActivityManagerStorage(filePath);

        //Save in new file and read back
        xmlActivityManagerStorage.saveActivityManager(original, filePath);
        ReadOnlyActivityManager readBack = xmlActivityManagerStorage.readActivityManager(filePath).get();
        assertEquals(original, new ActivityManager(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addActivity(new Activity(ta.tidy));
        original.addActivity(new Activity(ta.plane));
        original.addActivity(new Activity(ta.hotel));
        original.removeActivity(new Activity(ta.tidy));
        original.addActivity(new Activity(ta.plane));
        original.addActivity(new Activity(ta.hotel));
        original.updateActivity(new Activity(ta.groceries), "Buy Bread", null, null);
        xmlActivityManagerStorage.saveActivityManager(original, filePath);
        readBack = xmlActivityManagerStorage.readActivityManager(filePath).get();
        assertEquals(original, new ActivityManager(readBack));

    }

    @Test
    public void saveActivityManager_nullActivityManager_assertionFailure() throws IOException {
        thrown.expect(AssertionError.class);
        saveActivityManager(null, "SomeFile.xml");
    }
    
    private void saveActivityManager(ReadOnlyActivityManager activityManager, String filePath) throws IOException {
        new XmlActivityManagerStorage(filePath).saveActivityManager(activityManager, addToTestDataPathIfNotNull(filePath));
    }

    @Test
    public void saveActivityManager_nullFilePath_assertionFailure() throws IOException {
        thrown.expect(AssertionError.class);
        saveActivityManager(new ActivityManager(), null);
    }


}
