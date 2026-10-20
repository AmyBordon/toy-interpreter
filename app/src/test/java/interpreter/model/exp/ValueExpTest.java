package interpreter.model.exp;

import static org.junit.jupiter.api.Assertions.*;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

class ValueExpTest {
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();

    @Test
    void returnsItsIntValue() throws MyException {
        IExp e = new ValueExp(new IntValue(7));
        assertEquals(new IntValue(7), e.eval(symTable));
    }

    @Test
    void doesNotDependOnTheSymbolTable() throws MyException {
        symTable.put("x", new IntValue(99));
        IExp e = new ValueExp(new IntValue(7));
        assertEquals(new IntValue(7), e.eval(symTable));
    }

    @Test
    void toStringPrintsTheValue() {
        assertEquals("7", new ValueExp(new IntValue(7)).toString());
    }
}