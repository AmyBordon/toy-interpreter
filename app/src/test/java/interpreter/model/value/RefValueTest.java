package interpreter.model.value;

import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.type.StringType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class RefValueTest {

    @Test
    void typeIsRef() {
        assertEquals(new RefType(new IntType()), new RefValue(1, new IntType()).getType());
    }

    @Test
    void typeisDoubleRef(){
        assertEquals(new RefType(new RefType(new IntType())), new RefValue(2, new RefType(new IntType())).getType());
    }

    @Test
    void valuesWithTheSameNumberAreEqual() {

        assertEquals(new RefValue(1, new IntType()), new RefValue(1, new IntType()));
    }

    @Test
    void valuesWithDifferentAddressAreNotEqual() {

        assertNotEquals(new RefValue(5, new IntType()), new RefValue(6, new IntType()));
    }

    @Test
    void valuesWithDifferentTypesAreNotEqual() {

        assertNotEquals(new RefValue(5, new IntType()), new RefValue(5, new BoolType()));
    }

    @Test
    void equalValuesHaveTheSameHashCode() {

        assertEquals(new RefValue(5, new IntType()).hashCode(), new RefValue(5, new IntType()).hashCode());
    }

    @Test
    void refValueIsNotEqualToBoolValue() {
        assertNotEquals(new RefValue(1, new BoolType()), new BoolValue(true));
    }

    @Test
    void toStringPrintsThePair() {
        assertEquals("(1, int)", new RefValue(1, new IntType()).toString());
        assertEquals("(2, bool)", new RefValue(2, new BoolType()).toString());
        assertEquals("(3, string)", new RefValue(3, new StringType()).toString());
        assertEquals("(4, Ref(int))", new RefValue(4, new RefType(new IntType())).toString());
    }

}
