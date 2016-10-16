package seedu.manager.model.activity;

/**
 * Represents a floating activity in Remindaroo.
 * Guarantees: details are present and not null, field values are validated.
 */

public class FloatingActivity extends Activity {
	public FloatingActivity(String name) {
		super(name);
	}
	
	/**
     * Copy constructor
     */
    public FloatingActivity(ReadOnlyActivity source) {
        this(source.getName());
    }
}
