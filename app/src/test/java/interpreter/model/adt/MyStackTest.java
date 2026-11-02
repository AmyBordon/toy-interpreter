package interpreter.model.adt;
import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.Test;

import java.util.List;

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

    @Test
    void toListHasTheTopOfTheStackFirst(){
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(List.of(3, 2, 1), stack.toList());
    }

    @Test
    void toListIsNotAffectedByLaterChanges() {
        stack.push(1);
        List<Integer> copy = stack.toList();
        stack.push(2);
        assertEquals(1, copy.size());
    }

    @Test
    void toListDoesNotRemoveElementsFromTheStack() throws ADTException {
        stack.push(1);
        stack.push(2);
        stack.toList();
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
    }
}
