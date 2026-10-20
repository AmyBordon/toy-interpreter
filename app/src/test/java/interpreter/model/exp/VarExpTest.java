package interpreter.model.exp;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

class VarExpTest {
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();

    @Test
    void returnsTheValueStoredInTheSymbolTable() throws MyException {
        symTable.put("x", new IntValue(5));
        assertEquals(new IntValue(5), new VarExp("x").eval(symTable));
    }

    @Test
    void seesTheLatestValueAfterAnUpdate() throws MyException {
        symTable.put("x", new IntValue(5));
        symTable.update("x", new IntValue(10));
        assertEquals(new IntValue(10), new VarExp("x").eval(symTable));
    }

    @Test
    void undefinedVariableThrows() {
        assertThrows(MyException.class, () -> new VarExp("y").eval(symTable));
    }

    @Test
    void toStringPrintsTheVariableName() {
        assertEquals("x", new VarExp("x").toString());
    }
}