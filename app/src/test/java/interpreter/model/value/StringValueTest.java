package interpreter.model.value;
import interpreter.model.type.StringType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class StringValueTest {
    @Test
    void typeIsString() {
        assertEquals(new StringType(), new StringValue("a").getType());
    }

    @Test
    void valuesWithTheSameTextAreEqual() {
        assertEquals(new StringValue("a"), new StringValue("a"));
    }

    @Test
    void valuesWithDifferentTextAreNotEqual() {
        assertNotEquals(new StringValue("ab"), new StringValue("bc"));
    }

    @Test
    void equalSringsHaveTheSameHashCode() {
        assertEquals(new StringValue("a").hashCode(), new StringValue("a").hashCode());
    }

    @Test
    void StringValueIsNotEqualToBoolValue() {
        assertNotEquals(new StringValue("a"), new BoolValue(true));
    }

    @Test
    void toStringPutsTheTextBetweenQuotes() {
        assertEquals("\"ab\"", new StringValue("ab").toString());
        assertEquals("\"\"", new StringValue("").toString());   // empty string
    }
}
