package interpreter.model.stmt;
import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.IExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IfStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws ADTException {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(),
                new MyList<>(), new NopStmt());
        state.getExeStack().pop();   // remove the initial program so the stack starts empty
    }

    private static IExp bool(boolean b) { return new ValueExp(new BoolValue(b)); }

    private static IStmt printNum(int n) {
        return new PrintStmt(new ValueExp(new IntValue(n)));
    }

    @Test
    void trueConditionPushesThenBranch() throws MyException {
        IStmt thenS = printNum(1), elseS = printNum(2);
        new IfStmt(bool(true), thenS, elseS).execute(state);
        assertEquals(thenS, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void falseConditionPushesElseBranch() throws MyException {
        IStmt thenS = printNum(1), elseS = printNum(2);
        new IfStmt(bool(false), thenS, elseS).execute(state);
        assertEquals(elseS, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void conditionCanBeAVariable() throws MyException {
        state.getSymTable().put("a", new BoolValue(true));
        IStmt thenS = printNum(1);
        new IfStmt(new VarExp("a"), thenS, new NopStmt()).execute(state);
        assertEquals(thenS, state.getExeStack().pop());
    }

    @Test
    void nonBooleanConditionThrows() {
        var stmt = new IfStmt(new ValueExp(new IntValue(5)), new NopStmt(), new NopStmt());
        assertThrows(StatementException.class, () -> stmt.execute(state));
    }
}
