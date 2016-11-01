//@@author A0139797E
package seedu.manager.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import seedu.manager.commons.core.LogsCenter;
import seedu.manager.logic.commands.*;

import java.util.logging.Logger;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart {

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String ICON = "/images/help_icon.png";
    private static final String FXML = "HelpWindow.fxml";
    private static final String TITLE = "Help";
    
    private AnchorPane mainPane;

    private Stage dialogStage;

    /** Table-related properties */
    private static final int WINDOW_HEIGHT = 500;
    private static final int WINDOW_WIDTH = 800;
    private static final int COMMAND_WIDTH = 150;
    private static final int USAGE_WIDTH = 350;
    private static final int EXAMPLES_WIDTH = 300;
    
    private TableView<HelpWindowCommand> table = new TableView<HelpWindowCommand>();
    
    
    public static HelpWindow load(Stage primaryStage) {
        logger.fine("Showing help page about the application.");
        HelpWindow helpWindow = UiPartLoader.loadUiPart(primaryStage, new HelpWindow());
        helpWindow.configure();
        return helpWindow;
    }

    @Override
    public void setNode(Node node) {
        mainPane = (AnchorPane) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    private void configure(){
        Scene scene = new Scene(mainPane);
        //Null passed as the parent stage to make it non-modal.
        dialogStage = createDialogStage(TITLE, null, scene);
        dialogStage.setHeight(WINDOW_HEIGHT);
        dialogStage.setWidth(WINDOW_WIDTH);
        setIcon(dialogStage, ICON);

        table.setMinWidth(WINDOW_WIDTH);
        table.setMinHeight(WINDOW_HEIGHT);
        
        TableColumn<HelpWindowCommand, String>  command = new TableColumn<HelpWindowCommand, String>("Command");
        command.setMinWidth(COMMAND_WIDTH);
        command.setCellValueFactory(new PropertyValueFactory<HelpWindowCommand,String>("command"));
        
        TableColumn<HelpWindowCommand, String> usage = new TableColumn<HelpWindowCommand, String>("Usage");
        usage.setMinWidth(USAGE_WIDTH);
        usage.setCellValueFactory(new PropertyValueFactory<HelpWindowCommand,String>("usage"));
        
        TableColumn<HelpWindowCommand, String> examples = new TableColumn<HelpWindowCommand, String>("Examples");
        examples.setMinWidth(EXAMPLES_WIDTH);
        examples.setCellValueFactory(new PropertyValueFactory<HelpWindowCommand,String>("examples"));
            
        
        final ObservableList<HelpWindowCommand> helpWindowCommands = FXCollections.observableArrayList();
        helpWindowCommands.addAll(
                new HelpWindowCommand(AddCommand.COMMAND_WORD, AddCommand.USAGE, AddCommand.EXAMPLES),
                new HelpWindowCommand(ClearCommand.COMMAND_WORD, ClearCommand.USAGE, ClearCommand.EXAMPLES),
                new HelpWindowCommand(DeleteCommand.COMMAND_WORD, DeleteCommand.USAGE, DeleteCommand.EXAMPLES),
                new HelpWindowCommand(ExitCommand.COMMAND_WORD, ExitCommand.USAGE, ExitCommand.EXAMPLES),
                new HelpWindowCommand(HelpCommand.COMMAND_WORD, HelpCommand.USAGE, HelpCommand.EXAMPLES),
                new HelpWindowCommand(ListCommand.COMMAND_WORD, ListCommand.USAGE, ListCommand.EXAMPLES),
                new HelpWindowCommand(MarkCommand.COMMAND_WORD, MarkCommand.USAGE, MarkCommand.EXAMPLES),
                new HelpWindowCommand(SearchCommand.COMMAND_WORD, SearchCommand.USAGE, SearchCommand.EXAMPLES),
                new HelpWindowCommand(StoreCommand.COMMAND_WORD, StoreCommand.USAGE, StoreCommand.EXAMPLES),
                new HelpWindowCommand(UndoCommand.COMMAND_WORD, UndoCommand.USAGE, UndoCommand.EXAMPLES),
                new HelpWindowCommand(UnmarkCommand.COMMAND_WORD, UnmarkCommand.USAGE, UnmarkCommand.EXAMPLES),
                new HelpWindowCommand(UpdateCommand.COMMAND_WORD, UpdateCommand.USAGE, UpdateCommand.EXAMPLES)
                );
        
        table.getColumns().addAll(command, usage, examples);
        table.setItems(helpWindowCommands);
        
        mainPane.getChildren().add(table);
    }

    public void show() {
        dialogStage.showAndWait();
    }
}
