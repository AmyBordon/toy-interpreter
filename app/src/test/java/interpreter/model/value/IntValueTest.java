package interpreter.model.value;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.model.type.IntType;
import org.junit.jupiter.api.Test;

class IntValueTest {

    @Test
    void typeIsInt() {
        assertEquals(new IntType(), new IntValue(5).getType());
    }

    @Test
    void valuesWithTheSameNumberAreEqual() {
        assertEquals(new IntValue(5), new IntValue(5));
    }

    @Test
    void valuesWithDifferentNumbersAreNotEqual() {
        assertNotEquals(new IntValue(5), new IntValue(6));
    }

    @Test
    void equalValuesHaveTheSameHashCode() {
        assertEquals(new IntValue(5).hashCode(), new IntValue(5).hashCode());
    }

    @Test
    void intValueIsNotEqualToBoolValue() {
        assertNotEquals(new IntValue(1), new BoolValue(true));
    }

    @Test
    void toStringPrintsTheNumber() {
        assertEquals("5", new IntValue(5).toString());
        assertEquals("-3", new IntValue(-3).toString());
    }
}