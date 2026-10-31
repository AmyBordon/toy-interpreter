package interpreter.model.type;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.model.value.BoolValue;
import org.junit.jupiter.api.Test;

class BoolTypeTest {

    @Test
    void defaultValueIsFalse() {
        assertEquals(new BoolValue(false), new BoolType().defaultValue());
    }

    @Test
    void twoBoolTypesAreEqual() {
        assertEquals(new BoolType(), new BoolType());
    }

    @Test
    void equalBoolTypesHaveTheSameHashCode() {
        assertEquals(new BoolType().hashCode(), new BoolType().hashCode());
    }

    @Test
    void boolTypeIsNotEqualToIntType() {
        assertNotEquals(new BoolType(), new IntType());
    }

    @Test
    void toStringIsBool() {
        assertEquals("bool", new BoolType().toString());
    }
}