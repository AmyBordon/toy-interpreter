package interpreter.model.adt;
import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MyStackTest {
    private final IStack<Integer> stack = new MyStack<>();

    @Test
    void pushAppendsAtTheTop() throws ADTException {
        stack.push(1);
        stack.push(2);
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
    }

    @Test
    void popOnEmptyStackThrows() {
        assertThrows(ADTException.class, () -> stack.pop());
    }

    @Test
    void newStackIsEmpty() {
        assertTrue(stack.isEmpty());
    }

    @Test
    void stackWithElementsIsNotEmpty() {
        stack.push(1);
        assertFalse(stack.isEmpty());
    }

    @Test
    void stackIsEmptyAgainAfterPoppingEverything() throws ADTException {
        stack.push(1);
        stack.pop();
        assertTrue(stack.isEmpty());
    }
}
