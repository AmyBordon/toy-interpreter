package interpreter.model.type;

import interpreter.model.value.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class StringTypeTest {

    @Test
    void defaultValueIsEmptyString() {
        assertEquals(new StringValue(""), new StringType().defaultValue());
    }

    @Test
    void twoStringTypesAreEqual() {
        assertEquals(new StringType(), new StringType());
    }

    @Test
    void equalStringTypesHaveTheSameHashCode() {
        assertEquals(new StringType().hashCode(), new StringType().hashCode());
    }

    @Test
    void stringTypeIsNotEqualToBoolType() {
        assertNotEquals(new StringType(), new BoolType());
    }

    @Test
    void toStringIsString() {
        assertEquals("string", new StringType().toString());
    }
}
