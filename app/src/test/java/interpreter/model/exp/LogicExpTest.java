package interpreter.model.exp;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

class LogicExpTest {
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();

    private static IExp bool(boolean b) {
        return new ValueExp(new BoolValue(b));
    }

    private static IExp num(int n) {
        return new ValueExp(new IntValue(n));
    }

    private IValue eval(LogicOp op, boolean a, boolean b) throws MyException {
        return new LogicExp(op, bool(a), bool(b)).eval(symTable);
    }

    @Test
    void andTruthTable() throws MyException {
        assertEquals(new BoolValue(true),  eval(LogicOp.AND, true,  true));
        assertEquals(new BoolValue(false), eval(LogicOp.AND, true,  false));
        assertEquals(new BoolValue(false), eval(LogicOp.AND, false, true));
        assertEquals(new BoolValue(false), eval(LogicOp.AND, false, false));
    }

    @Test
    void orTruthTable() throws MyException {
        assertEquals(new BoolValue(true),  eval(LogicOp.OR, true,  true));
        assertEquals(new BoolValue(true),  eval(LogicOp.OR, true,  false));
        assertEquals(new BoolValue(true),  eval(LogicOp.OR, false, true));
        assertEquals(new BoolValue(false), eval(LogicOp.OR, false, false));
    }

    @Test
    void usesVariablesFromTheSymbolTable() throws MyException {
        symTable.put("flag", new BoolValue(true));
        IExp e = new LogicExp(LogicOp.AND, new VarExp("flag"), bool(true));
        assertEquals(new BoolValue(true), e.eval(symTable));
    }

    @Test
    void nestedExpressionsEvaluateInnerFirst() throws MyException {
        // true and (false or true)
        IExp e = new LogicExp(LogicOp.AND, bool(true),
                new LogicExp(LogicOp.OR, bool(false), bool(true)));
        assertEquals(new BoolValue(true), e.eval(symTable));
    }

    @Test
    void nonBooleanFirstOperandThrows() {
        IExp e = new LogicExp(LogicOp.AND, num(1), bool(true));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("first operand is not a boolean", ex.getMessage());
    }

    @Test
    void nonBooleanSecondOperandThrows() {
        IExp e = new LogicExp(LogicOp.OR, bool(true), num(1));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("second operand is not a boolean", ex.getMessage());
    }

    @Test
    void undefinedVariableThrows() {
        IExp e = new LogicExp(LogicOp.AND, new VarExp("y"), bool(true));
        assertThrows(MyException.class, () -> e.eval(symTable));
    }

    @Test
    void toStringShowsTheExpressionInfix() {
        IExp e = new LogicExp(LogicOp.AND, bool(true), bool(false));
        assertEquals("(true and false)", e.toString());
    }
}