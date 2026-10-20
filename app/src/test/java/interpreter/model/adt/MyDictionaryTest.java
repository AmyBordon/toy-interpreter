package interpreter.model.adt;
import static org.junit.jupiter.api.Assertions.*;
import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.Test;

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
}