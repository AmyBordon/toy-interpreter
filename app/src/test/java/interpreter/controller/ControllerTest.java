package interpreter.controller;
import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.CompStmt;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.NopStmt;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ControllerTest {
    private static PrgState newState(IStmt program){
        return new PrgState(new MyStack<>(), new MyDictionary<>(),
                new MyList<>(), program);
    }

    @Test
    void oneStepOnEmptyStackThrows() throws ADTException {
        PrgState state = newState(new NopStmt());
        state.getExeStack().pop();
        var controller = new Controller(new Repository(state), false);
        assertThrows(StatementException.class, () -> controller.oneStep(state));
    }

    @Test
    void oneStepExecutesExactlyOneRule() throws MyException {
        PrgState state = newState(new CompStmt(new NopStmt(), new NopStmt()));
        new Controller(new Repository(state), false).oneStep(state);
        state.getExeStack().pop();
        state.getExeStack().pop();
        assertTrue(state.getExeStack().isEmpty());
    }

    private static PrgState run(IStmt program) throws MyException {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(),
                new MyList<>(), program);
        new Controller(new Repository(state), false).allStep();
        return state;
    }

    @Test
    void example1PrintsTwo() throws MyException {
        PrgState state = run(Examples.EX1);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void example2PrintsEighteen() throws MyException {
        PrgState state = run(Examples.EX2);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(18), state.getOut().get(0));
    }

    @Test
    void example3PrintsTwo() throws MyException {
        PrgState state = run(Examples.EX3);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }
}
