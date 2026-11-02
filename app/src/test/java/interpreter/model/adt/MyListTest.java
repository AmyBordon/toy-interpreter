package interpreter.model.adt;

import static org.junit.jupiter.api.Assertions.*;
import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.Test;

import java.util.List;

class MyListTest {
    private final IList<Integer> list = new MyList<>();

    @Test
    void addAppendsAtTheEnd() throws ADTException {
        list.add(10);
        list.add(20);
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
    }

    @Test
    void sizeCountsElements() {
        assertEquals(0, list.size());
        list.add(1);
        list.add(2);
        assertEquals(2, list.size());
    }

    @Test
    void getWithInvalidPositionThrows() {
        list.add(1);
        assertThrows(ADTException.class, () -> list.get(1));
        assertThrows(ADTException.class, () -> list.get(-1));
    }

    @Test
    void toListKeepsInsertionOrder() throws ADTException {
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(List.of(1, 2, 3), list.toList());
    }

    @Test
    void toListIsNotAffectedByLaterChanges() {
        list.add(1);
        List<Integer> copy = list.toList();
        list.add(2);
        assertEquals(1, copy.size());
    }
}