package interpreter.model.stmt;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WhileStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws ADTException {
        state = PrgState.of(new NopStmt());
        state.getExeStack().pop();   // remove the initial program so the stack starts empty
    }

    private static IStmt printNum(int n) {
        return new PrintStmt(new ValueExp(new IntValue(n)));
    }
    private static IExp bool(boolean b) {
        return new ValueExp(new BoolValue(b));
    }

    @Test
    void zeroIterations() throws MyException {
        IStmt innerStmt = printNum(1);
        new WhileStmt(bool(false), innerStmt).execute(state);
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void moreIterations() throws MyException {
        state.getSymTable().put("a", new BoolValue(true));
        IStmt innerStmt = printNum(1);
        WhileStmt w = new WhileStmt(new VarExp("a"), innerStmt);
        w.execute(state);
        assertEquals(innerStmt, state.getExeStack().pop());
        assertEquals(w, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void nonBooleanCond() throws MyException {
        state.getSymTable().put("a", new IntValue(10));
        IStmt innerStmt = printNum(1);
        assertThrows(StatementException.class, () -> new
                WhileStmt(new VarExp("a"), innerStmt).execute(state));
    }


    @Test
    void innerErrorPropagates() throws MyException {
        state.getSymTable().put("a", new BoolValue(true));
        state.getSymTable().put("x", new IntValue(0));
        IStmt innerStmt = new AssignStmt("x", bool(true)); // tip incompatibil
        new WhileStmt(new VarExp("a"), innerStmt).execute(state);
        assertThrows(StatementException.class, () ->
                state.getExeStack().pop().execute(state));
    }


}
