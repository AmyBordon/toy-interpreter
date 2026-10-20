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

class ArithExpTest {
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();

    private static IExp num(int n) {
        return new ValueExp(new IntValue(n));
    }

    private static IExp bool(boolean b) {
        return new ValueExp(new BoolValue(b));
    }

    @Test
    void addsTwoNumbers() throws MyException {
        IExp e = new ArithExp(ArithOp.ADD, num(2), num(3));
        assertEquals(new IntValue(5), e.eval(symTable));
    }

    @Test
    void subtractsTwoNumbers() throws MyException {
        IExp e = new ArithExp(ArithOp.SUB, num(2), num(5));
        assertEquals(new IntValue(-3), e.eval(symTable));
    }

    @Test
    void multipliesTwoNumbers() throws MyException {
        IExp e = new ArithExp(ArithOp.MUL, num(4), num(5));
        assertEquals(new IntValue(20), e.eval(symTable));
    }

    @Test
    void dividesTwoNumbers() throws MyException {
        IExp e = new ArithExp(ArithOp.DIV, num(7), num(2));
        assertEquals(new IntValue(3), e.eval(symTable));   // integer division
    }

    @Test
    void respectsOperatorPrecedenceEncodedInTheTree() throws MyException {
        IExp e = new ArithExp(ArithOp.ADD, num(2),
                new ArithExp(ArithOp.MUL, num(3), num(5)));
        assertEquals(new IntValue(17), e.eval(symTable));
    }

    @Test
    void usesVariablesFromTheSymbolTable() throws MyException {
        symTable.put("x", new IntValue(5));
        IExp e = new ArithExp(ArithOp.ADD, new VarExp("x"), num(1));
        assertEquals(new IntValue(6), e.eval(symTable));
    }

    @Test
    void divisionByZeroThrows() {
        IExp e = new ArithExp(ArithOp.DIV, num(1), num(0));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("division by zero", ex.getMessage());
    }

    @Test
    void nonIntegerFirstOperandThrows() {
        IExp e = new ArithExp(ArithOp.ADD, bool(true), num(1));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("first operand is not an integer", ex.getMessage());
    }

    @Test
    void nonIntegerSecondOperandThrows() {
        IExp e = new ArithExp(ArithOp.ADD, num(1), bool(true));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("second operand is not an integer", ex.getMessage());
    }

    @Test
    void undefinedVariableThrows() {
        IExp e = new ArithExp(ArithOp.ADD, new VarExp("y"), num(1));
        assertThrows(MyException.class, () -> e.eval(symTable));
    }

    @Test
    void toStringShowsTheExpressionInfix() {
        IExp e = new ArithExp(ArithOp.ADD, num(2),
                new ArithExp(ArithOp.MUL, num(3), num(5)));
        assertEquals("(2 + (3 * 5))", e.toString());
    }
}