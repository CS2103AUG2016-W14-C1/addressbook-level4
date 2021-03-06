package seedu.manager.model.activity;

/**
 * A read-only immutable interface for an Activity in Remindaroo.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */

public interface ReadOnlyActivity {
    
    ActivityType getType();
    String getName();
    Status getStatus();
    AMDate getDateTime();
    AMDate getEndDateTime();
    boolean getSelected();
}
