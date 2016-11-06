package seedu.manager.commons.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility methods related to Collections
 */
public class CollectionUtil {

    /**
     * Returns true if any of the given items are null.
     */
    private static boolean isAnyNull(Object... items) {
        for (Object item : items) {
            if (item == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Throws an assertion error if the collection or any item in it is null.
     */
    public static void assertNoNullElements(Collection<?> items) {
        assert items != null;
        assert !isAnyNull(items);
    }
}
