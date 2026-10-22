package interpreter.model.stmt;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VarDeclStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(),
                new MyList<>(), new NopStmt());
    }

    @Test
    void declaringIntAddsZeroToSymTable() throws MyException {
        new VarDeclStmt("v", new IntType()).execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
    }

    @Test
    void declaringBoolAddsFalseToSymTable() throws MyException {
        new VarDeclStmt("a", new BoolType()).execute(state);
        assertEquals(new BoolValue(false), state.getSymTable().lookup("a"));
    }

    @Test
    void declaringTwiceThrows() throws MyException {
        var decl = new VarDeclStmt("v", new IntType());
        decl.execute(state);
        var ex = assertThrows(StatementException.class, () -> decl.execute(state));
        assertEquals("variable v is already declared", ex.getMessage());
    }
}