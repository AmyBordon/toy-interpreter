package interpreter.model.type;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class RefTypeTest {
    @Test
    void defaultValueIsNull() {
        assertEquals(new RefValue(0, new IntType()),new RefType(new IntType()).defaultValue());
    }

    @Test
    void twoRefTypesAreEqual() {
        assertEquals(new RefType(new BoolType()), new RefType(new BoolType()));
    }

    @Test
    void equalRefTypesHaveTheSameHashCode() {
        assertEquals(new RefType(new BoolType()).hashCode(), new RefType(new BoolType()).hashCode());
    }

    @Test
    void refTypeIsNotEqualToBoolType() {
        assertNotEquals(new RefType(new IntType()), new BoolType());
    }

    @Test
    void toStringShowsTheInnerType() {

        assertEquals("Ref(string)", new RefType(new StringType()).toString());
    }

    @Test
    void doubleRefEqualDoubleRef(){
        assertEquals(new RefType(new RefType(new IntType())), new RefType(new RefType(new IntType())));
    }

    @Test
    void doubleRefNotEqualSingleRef(){
        assertNotEquals(new RefType(new IntType()), new RefType(new RefType(new IntType())));
    }

    @Test
    void intRefNotEqualBoolRef(){
        assertNotEquals(new RefType(new IntType()), new RefType(new BoolType()));
    }

    @Test
    void defaultValueDoubleRef() {
        assertEquals(new RefValue(0, new RefType(new IntType())),
                new RefType(new RefType(new IntType())).defaultValue());
    }

}
