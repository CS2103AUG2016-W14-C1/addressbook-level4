package guitests.guihandles;

import guitests.GuiRobot;
import javafx.scene.Node;
import javafx.stage.Stage;
import seedu.manager.model.activity.ReadOnlyActivity;

/**
 * Provides a handle to a activity card in the activity list panel.
 */
public class ActivityCardHandle extends GuiHandle {
    private static final String NAME_FIELD_ID = "#name";
    private static final String DATE_TIME_FIELD_ID = "#dateTime";
    private static final String END_DATE_TIME_FIELD_ID = "#endDateTime";
    private static final String STATUS_FIELD_ID = "#status";

    private Node node;

    public ActivityCardHandle(GuiRobot guiRobot, Stage primaryStage, Node node){
        super(guiRobot, primaryStage, null);
        this.node = node;
    }

    protected String getTextFromLabel(String fieldId) {
        return getTextFromLabel(fieldId, node);
    }

    public String getName() {
        return getTextFromLabel(NAME_FIELD_ID);
    }
    
    public String getDateTime() {
        return getTextFromLabel(DATE_TIME_FIELD_ID);
    }
    
    public String getEndDateTime() {
        return getTextFromLabel(END_DATE_TIME_FIELD_ID);
    }
    
    public String getStatus() {
        return getTextFromLabel(STATUS_FIELD_ID);
    }

    public boolean isSameActivity(ReadOnlyActivity activity){
        return getName().equals(activity.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ActivityCardHandle) {
            ActivityCardHandle handle = (ActivityCardHandle) obj;
            return getName().equals(handle.getName())
                    && getDateTime().equals(handle.getDateTime())
                    && getEndDateTime().equals(handle.getEndDateTime())
                    && getStatus().equals(getStatus());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return getName();
    }
}
