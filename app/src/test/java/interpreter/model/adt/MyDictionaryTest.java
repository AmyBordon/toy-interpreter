package interpreter.model.adt;
import static org.junit.jupiter.api.Assertions.*;
import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.Test;

import java.util.Map;

class MyDictionaryTest {
    private final IDictionary<String, Integer> dict = new MyDictionary<>();

    @Test
    void putThenLookupReturnsValue() throws ADTException {
        dict.put("x", 5);
        assertEquals(5, dict.lookup("x"));
    }

    @Test
    void lookupOfUndefinedKeyThrows() {
        assertThrows(ADTException.class, () -> dict.lookup("y"));
    }

    @Test
    void updateReplacesExistingValue() throws ADTException {
        dict.put("x", 5);
        dict.update("x", 10);
        assertEquals(10, dict.lookup("x"));
    }

    @Test
    void updateOfUndefinedKeyThrows() {
        assertThrows(ADTException.class, () -> dict.update("z", 1));
    }

    @Test
    void toMapIsNotAffectedByLaterChanges() {
        dict.put("x", 1);
        Map<String, Integer> copy = dict.toMap();
        dict.put("y", 2);
        assertEquals(1, copy.size());
    }

    @Test
    void toMapCannotBeModified() {
        dict.put("x", 1);
        Map<String, Integer> copy = dict.toMap();
        assertThrows(UnsupportedOperationException.class, () -> copy.put("z", 3));
    }

    @Test
    void removeDeletesExistingValue() throws ADTException {
        dict.put("x", 5);
        dict.remove("x");
        assertFalse(dict.isDefined("x"));
    }

    @Test
    void removeOfUndefinedKeyThrows() {
        assertThrows(ADTException.class, () -> dict.remove("z"));
    }

}