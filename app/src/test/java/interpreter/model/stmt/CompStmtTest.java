package interpreter.model.stmt;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws ADTException {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(),
                new MyList<>(), new NopStmt());
        state.getExeStack().pop();   // start with an empty stack
    }

    private static IStmt printNum(int n) {
        return new PrintStmt(new ValueExp(new IntValue(n)));
    }

    @Test
    void pushesFirstOnTopOfSecond() throws MyException {
        IStmt first = printNum(1), second = printNum(2);
        new CompStmt(first, second).execute(state);
        assertEquals(first, state.getExeStack().pop());
        assertEquals(second, state.getExeStack().pop());
    }

    @Test
    void pushesExactlyTwoStatements() throws MyException {
        new CompStmt(printNum(1), printNum(2)).execute(state);
        state.getExeStack().pop();
        state.getExeStack().pop();
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void doesNotExecuteTheStatementsItself() throws MyException {
        new CompStmt(printNum(1), printNum(2)).execute(state);
        assertEquals(0, state.getOut().size());
    }

    @Test
    void nestedCompStmtIsUnpackedOneLevelAtATime() throws MyException {
        IStmt a = printNum(1), b = printNum(2), c = printNum(3);
        IStmt inner = new CompStmt(b, c);
        new CompStmt(a, inner).execute(state);
        assertEquals(a, state.getExeStack().pop());
        assertEquals(inner, state.getExeStack().pop());   // still packed
    }

    @Test
    void toStringShowsBothStatements() {
        assertEquals("(print(1); print(2))",
                new CompStmt(printNum(1), printNum(2)).toString());
    }
}