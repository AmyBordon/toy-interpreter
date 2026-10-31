package interpreter.model.type;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

class IntTypeTest {

    @Test
    void defaultValueIsZero() {
        assertEquals(new IntValue(0), new IntType().defaultValue());
    }

    @Test
    void twoIntTypesAreEqual() {
        assertEquals(new IntType(), new IntType());
    }

    @Test
    void equalIntTypesHaveTheSameHashCode() {
        assertEquals(new IntType().hashCode(), new IntType().hashCode());
    }

    @Test
    void intTypeIsNotEqualToBoolType() {
        assertNotEquals(new IntType(), new BoolType());
    }

    @Test
    void toStringIsInt() {
        assertEquals("int", new IntType().toString());
    }
}