package interpreter.model.adt;

import static org.junit.jupiter.api.Assertions.*;
import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.Test;

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
}