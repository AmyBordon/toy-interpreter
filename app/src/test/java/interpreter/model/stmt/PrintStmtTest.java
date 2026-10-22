package interpreter.model.stmt;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrintStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(),
                new MyList<>(), new NopStmt());
    }

    @Test
    void printAppendsValueToOut() throws MyException {
        new PrintStmt(new ValueExp(new IntValue(7))).execute(state);
        assertEquals(new IntValue(7), state.getOut().get(0));
    }

    @Test
    void printEvaluatesVariables() throws MyException {
        state.getSymTable().put("v", new IntValue(2));
        new PrintStmt(new VarExp("v")).execute(state);
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void printsAreAppendedInOrder() throws MyException {
        new PrintStmt(new ValueExp(new IntValue(1))).execute(state);
        new PrintStmt(new ValueExp(new IntValue(2))).execute(state);
        assertEquals(new IntValue(1), state.getOut().get(0));
        assertEquals(new IntValue(2), state.getOut().get(1));
    }

    @Test
    void printOfUndefinedVariableThrows() {
        assertThrows(MyException.class,
                () -> new PrintStmt(new VarExp("x")).execute(state));
    }
}
