package seedu.manager.model.activity;

public interface ReadOnlyActivity {
    String getName();
    void setName(String newName);
    void setStatus(boolean completed);
    String getStatus();
}
