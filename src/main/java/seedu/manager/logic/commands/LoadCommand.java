package seedu.manager.logic.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import seedu.manager.commons.core.Config;
import seedu.manager.commons.util.ConfigUtil;
import seedu.manager.commons.util.FileUtil;
import seedu.manager.model.ReadOnlyActivityManager;
import seedu.manager.storage.XmlFileStorage;
import seedu.manager.storage.XmlSerializableActivityManager;

//@@author A0139797E
public class LoadCommand extends Command {

    public static final String COMMAND_WORD = "load";
    public static final String USAGE = "load STORAGE_FILE_LOCATION";
    public static final String EXAMPLES = "load Users/Documents/Remindaroo.xml";
    public static final String MESSAGE_LOAD_FILE_SUCCESS = "Remindaroo has loaded data from %1$s";
    public static final String MESSAGE_LOAD_FILE_FAIL = "An error has ocurred while loading data";
    public static final String MESSAGE_LOAD_FILE_INVALID = "Specified data file does not exist!";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Loads Remindaroo data from the specified XML storage file path. \n"
             + "Example: " + COMMAND_WORD + " Users/Documents/Remindaroo.xml";

    private final String dataFileLocation;
    
    public LoadCommand(String dataFileLocation) {
        assert dataFileLocation != null;
        this.dataFileLocation = dataFileLocation;
    }


    @Override
    public CommandResult execute() {
        assert model != null;
        ReadOnlyActivityManager AM = model.getActivityManager();
        try {
            File newDataFile = new File(dataFileLocation);
            
            Config currentConfig = ConfigUtil.readConfig(Config.DEFAULT_CONFIG_FILE).get();
            File currentDataFile = new File(currentConfig.getActivityManagerFilePath());
            FileUtil.createIfMissing(currentDataFile);
            
            FileChannel source = new FileInputStream(newDataFile).getChannel();
            FileChannel dest = new FileOutputStream(currentDataFile).getChannel();
            dest.transferFrom(source, 0, source.size());
            
            model.resetData(XmlFileStorage.loadDataFromSaveFile(currentDataFile));
        } catch (FileNotFoundException fnfe) {
            return new CommandResult(MESSAGE_LOAD_FILE_INVALID);
        } catch (Exception e) {
            return new CommandResult(MESSAGE_LOAD_FILE_FAIL);
        }
        
        model.indicateActivityListPanelUpdate();    
        return new CommandResult(String.format(MESSAGE_LOAD_FILE_SUCCESS, dataFileLocation.trim()));
    }
}
