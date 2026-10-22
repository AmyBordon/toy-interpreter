package interpreter.model.stmt;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssignStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws ADTException {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(),
                new MyList<>(), new NopStmt());
        state.getExeStack().pop();
    }

    private static ValueExp num(int n) { return new ValueExp(new IntValue(n)); }

    @Test
    void assignsIntToDeclaredVariable() throws MyException {
        state.getSymTable().put("v", new IntValue(0));
        new AssignStmt("v", num(2)).execute(state);
        assertEquals(new IntValue(2), state.getSymTable().lookup("v"));
    }

    @Test
    void assignsBoolToDeclaredVariable() throws MyException {
        state.getSymTable().put("a", new BoolValue(false));
        new AssignStmt("a", new ValueExp(new BoolValue(true))).execute(state);
        assertEquals(new BoolValue(true), state.getSymTable().lookup("a"));
    }

    @Test
    void secondAssignmentOverwritesTheFirst() throws MyException {
        state.getSymTable().put("v", new IntValue(0));
        new AssignStmt("v", num(2)).execute(state);
        new AssignStmt("v", num(9)).execute(state);
        assertEquals(new IntValue(9), state.getSymTable().lookup("v"));
    }

    @Test
    void evaluatesExpressionUsingOtherVariables() throws MyException {
        state.getSymTable().put("a", new IntValue(17));
        state.getSymTable().put("b", new IntValue(0));
        new AssignStmt("b",
                new ArithExp(ArithOp.ADD, new VarExp("a"), num(1))).execute(state);
        assertEquals(new IntValue(18), state.getSymTable().lookup("b"));
    }

    @Test
    void doesNotTouchOtherVariablesOrOut() throws MyException {
        state.getSymTable().put("v", new IntValue(0));
        state.getSymTable().put("w", new IntValue(5));
        new AssignStmt("v", num(2)).execute(state);
        assertEquals(new IntValue(5), state.getSymTable().lookup("w"));
        assertEquals(0, state.getOut().size());
    }

    @Test
    void assigningToUndeclaredVariableThrows() {
        var ex = assertThrows(StatementException.class,
                () -> new AssignStmt("x", num(1)).execute(state));
        assertEquals("variable x was not declared", ex.getMessage());
    }

    @Test
    void assigningBoolToIntVariableThrows() throws MyException {
        state.getSymTable().put("v", new IntValue(0));
        var ex = assertThrows(StatementException.class,
                () -> new AssignStmt("v", new ValueExp(new BoolValue(true))).execute(state));
        assertEquals("declared type of variable v and type of the assigned "
                + "expression do not match", ex.getMessage());
    }

    @Test
    void assigningIntToBoolVariableThrows() throws MyException {
        state.getSymTable().put("a", new BoolValue(false));
        assertThrows(StatementException.class,
                () -> new AssignStmt("a", num(1)).execute(state));
    }

    @Test
    void failedAssignmentLeavesVariableUnchanged() throws MyException {
        state.getSymTable().put("v", new IntValue(4));
        assertThrows(StatementException.class,
                () -> new AssignStmt("v", new ValueExp(new BoolValue(true))).execute(state));
        assertEquals(new IntValue(4), state.getSymTable().lookup("v"));
    }

    @Test
    void errorInsideExpressionPropagates() throws MyException {
        state.getSymTable().put("v", new IntValue(0));
        var stmt = new AssignStmt("v", new ArithExp(ArithOp.DIV, num(1), num(0)));
        assertThrows(ExpressionException.class, () -> stmt.execute(state));
    }

    @Test
    void toStringShowsAssignment() {
        assertEquals("v = 2", new AssignStmt("v", num(2)).toString());
    }
}