package seedu.manager.model;

import javafx.collections.FXCollections;
import seedu.manager.commons.core.UnmodifiableObservableList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static seedu.manager.testutil.TestUtil.assertThrows;

public class UnmodifiableObservableListTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public List<Integer> backing;
    public UnmodifiableObservableList<Integer> list;
    public UnmodifiableObservableList<Integer> list2;

    @Before
    public void setup() {
        backing = new ArrayList<>();
        backing.add(10);
        list = new UnmodifiableObservableList<>(FXCollections.observableList(backing));
        list2 = new UnmodifiableObservableList<>(FXCollections.observableList(backing));
    }

    @Test
    public void transformationListGenerators_correctBackingList() {
        assertSame(list.sorted().getSource(), list);
        assertSame(list.filtered(i -> true).getSource(), list);
    }

    @Test
    public void mutatingMethods_disabled() {

        final Class<UnsupportedOperationException> ex = UnsupportedOperationException.class;

        assertThrows(ex, () -> list.add(0, 2));
        assertThrows(ex, () -> list.add(3));

        assertThrows(ex, () -> list.addAll(2, 1));
        assertThrows(ex, () -> list.addAll(backing));
        assertThrows(ex, () -> list.addAll(0, backing));

        assertThrows(ex, () -> list.set(0, 2));

        assertThrows(ex, () -> list.setAll(new ArrayList<Number>()));
        assertThrows(ex, () -> list.setAll(1, 2));

        assertThrows(ex, () -> list.remove(0, 1));
        assertThrows(ex, () -> list.remove(null));
        assertThrows(ex, () -> list.remove(0));

        assertThrows(ex, () -> list.removeAll(backing));
        assertThrows(ex, () -> list.removeAll(1, 2));

        assertThrows(ex, () -> list.retainAll(backing));
        assertThrows(ex, () -> list.retainAll(1, 2));

        assertThrows(ex, () -> list.replaceAll(i -> 1));

        assertThrows(ex, () -> list.sort(Comparator.naturalOrder()));

        assertThrows(ex, () -> list.clear());

        final Iterator<Integer> iter = list.iterator();
        iter.next();
        assertThrows(ex, iter::remove);
        
        final ListIterator<Integer> liter = list.listIterator();
        
        liter.next();
        assertThrows(ex, liter::remove);
        assertThrows(ex, () -> liter.add(5));
        assertThrows(ex, () -> liter.set(3));
        assertThrows(ex, () -> list.removeIf(i -> true));
    }
    
    //@@author A0139797E
    @Test
    public void listIterator_NextAndPrevious() {
        final ListIterator<Integer> liter = list.listIterator();
        
        while(liter.hasNext()) {
            liter.nextIndex();
            liter.next();
        }
        
        while(liter.hasPrevious()) {
            liter.previousIndex();
            liter.previous();
        }
    }
    
    @Test
    public void unmodifiableList_Equals() {
        assertEquals(list, list2);
        assertEquals(list.hashCode(), list2.hashCode());
    }
    
    @Test
    public void unmodifiableList_indexAccess() {
        assertEquals(0, list.indexOf(10));
        assertEquals(0, list.lastIndexOf(10));
    }
    
    @Test
    public void unmodifiableList_streamConversion() {
        Optional<Integer> firstIndex = list.stream().map(e -> 2 * e).findFirst();
        assertTrue(firstIndex.isPresent());
        assertSame(20, firstIndex.get());
    }
    
    @Test
    public void unmodifiableList_arrayTest() {
        Object[] objArray = list.toArray();
        assertSame(10, objArray[0]);
    }
    
    @Test
    public void unmodifiableList_emptyTest() {
        assertFalse(list.isEmpty());
    }
    
    @Test
    public void unmodifiableList_containTest() {
        assertTrue(list.contains(10));
    }
}
