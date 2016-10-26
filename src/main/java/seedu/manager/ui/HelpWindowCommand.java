//@@author A0139797E
package seedu.manager.ui;

import javafx.beans.property.SimpleStringProperty;

public class HelpWindowCommand {
    private final SimpleStringProperty command;
    private final SimpleStringProperty usage;
    private final SimpleStringProperty examples;
    
    HelpWindowCommand(String command, String usage, String examples) {
        this.command = new SimpleStringProperty(command);
        this.usage = new SimpleStringProperty(usage);
        this.examples = new SimpleStringProperty(examples);
        
    }
    
    public String getCommand() {
        return command.get();
    }
    
    public String getUsage() {
        return usage.get();
    }
    
    public String getExamples() {
        return examples.get();
    }
}
