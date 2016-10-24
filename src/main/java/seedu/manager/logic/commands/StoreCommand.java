package seedu.manager.logic.commands;

import static seedu.manager.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.io.File;
import java.io.IOException;

import seedu.manager.commons.core.Config;
import seedu.manager.commons.exceptions.DataConversionException;
import seedu.manager.commons.exceptions.IllegalValueException;
import seedu.manager.commons.util.ConfigUtil;
import seedu.manager.commons.util.FileUtil;
import seedu.manager.model.ReadOnlyActivityManager;
import seedu.manager.storage.XmlActivityManagerStorage;
import seedu.manager.storage.XmlFileStorage;
import seedu.manager.storage.XmlSerializableActivityManager;

public class StoreCommand extends Command {

    public static final String COMMAND_WORD = "store";
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
        } catch (IOException e) {
        	return new CommandResult(MESSAGE_STORE_FILE_FAIL);
        }
        
        Config currentConfig;
        try {
        	currentConfig = ConfigUtil.readConfig(Config.DEFAULT_CONFIG_FILE).get();
        	currentConfig.setActivityManagerFilePath(dataFileLocation);
        	ConfigUtil.saveConfig(currentConfig, Config.DEFAULT_CONFIG_FILE);	
        } catch (Exception e) {
			return new CommandResult(MESSAGE_STORE_FILE_FAIL);
		}
      
        return new CommandResult(String.format(MESSAGE_STORE_FILE_SUCCESS, dataFileLocation.trim()));
    }
}
