package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RelationalExpTest {
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();

    private static IExp num(int n) {
        return new ValueExp(new IntValue(n));
    }

    private static IExp bool(boolean b) {
        return new ValueExp(new BoolValue(b));
    }

    @Test
    void greaterThanIsTrue() throws MyException {
        IExp e = new RelationalExp(RelOp.GT, num(4), num(3));
        assertEquals(new BoolValue(true), e.eval(symTable));
    }

    @Test
    void greaterThanIsFalseWhenEqual() throws MyException {
        IExp e = new RelationalExp(RelOp.GT, num(4), num(4));
        assertEquals(new BoolValue(false), e.eval(symTable));
    }

    @Test
    void greaterEqualIsTrue() throws MyException {
        IExp e1 = new RelationalExp(RelOp.GE, num(4), num(4));
        assertEquals(new BoolValue(true), e1.eval(symTable));

        IExp e2 = new RelationalExp(RelOp.GE, num(4), num(3));
        assertEquals(new BoolValue(true), e2.eval(symTable));
    }

    @Test
    void greaterEqualIsFalse() throws MyException {
        IExp e = new RelationalExp(RelOp.GE, num(3), num(4));
        assertEquals(new BoolValue(false), e.eval(symTable));
    }

    @Test
    void lessThanIsTrue() throws MyException {
        IExp e = new RelationalExp(RelOp.LT, num(3), num(4));
        assertEquals(new BoolValue(true), e.eval(symTable));
    }

    @Test
    void lessThanIsFalseWhenEqual() throws MyException {
        IExp e = new RelationalExp(RelOp.LT, num(4), num(4));
        assertEquals(new BoolValue(false), e.eval(symTable));
    }

    @Test
    void lessEqualIsTrue() throws MyException {
        IExp e1 = new RelationalExp(RelOp.LE, num(3), num(4));
        assertEquals(new BoolValue(true), e1.eval(symTable));

        IExp e2 = new RelationalExp(RelOp.LE, num(4), num(4));
        assertEquals(new BoolValue(true), e2.eval(symTable));
    }

    @Test
    void lessEqualIsFalse() throws MyException {
        IExp e = new RelationalExp(RelOp.LE, num(5), num(4));
        assertEquals(new BoolValue(false), e.eval(symTable));
    }

    @Test
    void equalIsTrue() throws MyException {
        IExp e = new RelationalExp(RelOp.EQ, num(4), num(4));
        assertEquals(new BoolValue(true), e.eval(symTable));
    }

    @Test
    void equalIsFalse() throws MyException {
        IExp e = new RelationalExp(RelOp.EQ, num(3), num(4));
        assertEquals(new BoolValue(false), e.eval(symTable));
    }

    @Test
    void notEqualIsTrue() throws MyException {
        IExp e = new RelationalExp(RelOp.NE, num(3), num(4));
        assertEquals(new BoolValue(true), e.eval(symTable));
    }

    @Test
    void notEqualIsFalse() throws MyException {
        IExp e = new RelationalExp(RelOp.NE, num(4), num(4));
        assertEquals(new BoolValue(false), e.eval(symTable));
    }

    @Test
    void nonIntegerFirstOperandThrows() {
        IExp e = new RelationalExp(RelOp.LT, bool(true), num(1));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("first operand is not an integer", ex.getMessage());
    }

    @Test
    void nonIntegerSecondOperandThrows() {
        IExp e = new RelationalExp(RelOp.LT, num(1), bool(true));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("second operand is not an integer", ex.getMessage());
    }
}