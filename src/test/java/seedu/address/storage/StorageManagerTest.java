package seedu.address.storage;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import seedu.address.model.ActivityManager;
import seedu.address.model.ReadOnlyActivityManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.TypicalTestActivities;

public class StorageManagerTest {

    private StorageManager storageManager;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();


    @Before
    public void setup() {
        storageManager = new StorageManager(getTempFilePath("am"), getTempFilePath("prefs"));
    }


    private String getTempFilePath(String fileName) {
        return testFolder.getRoot().getPath() + fileName;
    }


    /*
     * Note: This is an integration test that verifies the StorageManager is properly wired to the
     * {@link JsonUserPrefStorage} class.
     * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefStorageTest} class.
     */

    @Test
    public void prefsReadSave() throws Exception {
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void activityManagerReadSave() throws Exception {
        ActivityManager original = new TypicalTestActivities().getTypicalActivityManager();
        storageManager.saveActivityManager(original);
        ReadOnlyActivityManager retrieved = storageManager.readActivityManager().get();
        assertTrue(original.toString().equals((new ActivityManager(retrieved)).toString()));
        //More extensive testing of ActivityManager saving/reading is done in XmlAddressBookStorageTest
    }


}
