package interpreter.model.value;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.model.type.BoolType;
import org.junit.jupiter.api.Test;

class BoolValueTest {

    @Test
    void typeIsBool() {
        assertEquals(new BoolType(), new BoolValue(true).getType());
    }

    @Test
    void valuesWithTheSameBooleanAreEqual() {
        assertEquals(new BoolValue(true), new BoolValue(true));
        assertEquals(new BoolValue(false), new BoolValue(false));
    }

    @Test
    void trueAndFalseAreNotEqual() {
        assertNotEquals(new BoolValue(true), new BoolValue(false));
    }

    @Test
    void equalValuesHaveTheSameHashCode() {
        assertEquals(new BoolValue(true).hashCode(), new BoolValue(true).hashCode());
    }

    @Test
    void boolValueIsNotEqualToIntValue() {
        assertNotEquals(new BoolValue(true), new IntValue(1));
    }

    @Test
    void toStringPrintsTrueOrFalse() {
        assertEquals("true", new BoolValue(true).toString());
        assertEquals("false", new BoolValue(false).toString());
    }
}