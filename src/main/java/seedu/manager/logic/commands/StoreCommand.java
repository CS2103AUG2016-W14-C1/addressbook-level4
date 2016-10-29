package seedu.manager.logic.commands;

import java.io.File;

import seedu.manager.commons.core.Config;
import seedu.manager.commons.core.EventsCenter;
import seedu.manager.commons.events.storage.ChangeStorageFileEvent;
import seedu.manager.commons.util.ConfigUtil;
import seedu.manager.commons.util.FileUtil;
import seedu.manager.model.ReadOnlyActivityManager;
import seedu.manager.storage.XmlFileStorage;
import seedu.manager.storage.XmlSerializableActivityManager;

public class StoreCommand extends Command {

    public static final String COMMAND_WORD = "store";
    public static final String USAGE = "store STORAGE_FILE_LOCATION";
    public static final String EXAMPLES = "store Users/Documents/Remindaroo.xml";
    public static final String MESSAGE_STORE_FILE_SUCCESS = "Remindaroo data have been saved to %1$s";
    public static final String MESSAGE_STORE_FILE_FAIL= "An error has ocurred while saving";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Saves Remindaroo data to the specified storage file path. \n"
             + "Example: " + COMMAND_WORD + " Users/Documents/Remindaroo.xml";

    private final String dataFileLocation;
    
    public StoreCommand(String dataFileLocation) {
    	assert dataFileLocation != null;
    	this.dataFileLocation = dataFileLocation;
    }


    @Override
    public CommandResult execute() {
        assert model != null;
        ReadOnlyActivityManager AM = model.getActivityManager();
        try {
        	File newDataFile = new File(dataFileLocation);
        	FileUtil.createIfMissing(newDataFile);
        	XmlSerializableActivityManager xmlAM = new XmlSerializableActivityManager(AM);
        	XmlFileStorage.saveDataToFile(newDataFile, xmlAM);
        
        	Config currentConfig;
        	currentConfig = ConfigUtil.readConfig(Config.DEFAULT_CONFIG_FILE).get();
        	currentConfig.setActivityManagerFilePath(dataFileLocation);
        	ConfigUtil.saveConfig(currentConfig, Config.DEFAULT_CONFIG_FILE);	
        } catch (Exception e) {
			return new CommandResult(MESSAGE_STORE_FILE_FAIL);
		}
        
        EventsCenter.getInstance().post(new ChangeStorageFileEvent(dataFileLocation));
        return new CommandResult(String.format(MESSAGE_STORE_FILE_SUCCESS, dataFileLocation.trim()));
    }
}
