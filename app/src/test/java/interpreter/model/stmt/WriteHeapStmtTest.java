package interpreter.model.stmt;

import static org.junit.jupiter.api.Assertions.*;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.IExp;
import interpreter.model.exp.ReadHeapExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WriteHeapStmtTest {
    private PrgState state;
    private int address;   // the heap location that v refers to

    @BeforeEach
    void setUp() throws MyException {
        state = PrgState.of(new NopStmt());
        address = state.getHeap().allocate(new IntValue(20));
        state.getSymTable().put("v", new RefValue(address, new IntType()));
    }

    private static IExp num(int n) {
        return new ValueExp(new IntValue(n));
    }

    @Test
    void writeChangesTheHeapLocation() throws MyException {
        new WriteHeapStmt("v", num(30)).execute(state);

        assertEquals(new IntValue(30), state.getHeap().get(address));
    }

    @Test
    void writeKeepsTheAddressInTheVariable() throws MyException {
        new WriteHeapStmt("v", num(30)).execute(state);

        assertEquals(new RefValue(address, new IntType()), state.getSymTable().lookup("v"));
    }

    @Test
    void writeDoesNotTouchOtherLocations() throws MyException {
        int other = state.getHeap().allocate(new IntValue(99));

        new WriteHeapStmt("v", num(30)).execute(state);

        assertEquals(new IntValue(99), state.getHeap().get(other));
    }

    @Test
    void writeDoesNotAllocateANewLocation() throws MyException {
        new WriteHeapStmt("v", num(30)).execute(state);

        assertEquals(1, state.getHeap().getContent().size());
    }

    @Test
    void theWrittenValueCanDependOnTheHeap() throws MyException {
        // wH(v, rH(v) + 1)
        IExp exp = new ArithExp(ArithOp.ADD, new ReadHeapExp(new VarExp("v")), num(1));

        new WriteHeapStmt("v", exp).execute(state);

        assertEquals(new IntValue(21), state.getHeap().get(address));
    }

    @Test
    void writingTwiceKeepsTheLastValue() throws MyException {
        new WriteHeapStmt("v", num(30)).execute(state);
        new WriteHeapStmt("v", num(40)).execute(state);

        assertEquals(new IntValue(40), state.getHeap().get(address));
    }

    @Test
    void aVariableThatIsNotDeclaredThrows() {
        var stmt = new WriteHeapStmt("missing", num(30));

        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void aVariableThatIsNotAReferenceThrows() throws MyException {
        state.getSymTable().put("x", new IntValue(5));
        var stmt = new WriteHeapStmt("x", num(30));

        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void aNullReferenceThrows() throws MyException {
        state.getSymTable().put("n", new RefValue(0, new IntType()));
        var stmt = new WriteHeapStmt("n", num(30));

        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void anAddressThatIsNotInTheHeapThrows() throws MyException {
        state.getSymTable().put("n", new RefValue(9, new IntType()));
        var stmt = new WriteHeapStmt("n", num(30));

        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void aValueOfTheWrongTypeThrows() {
        var stmt = new WriteHeapStmt("v", new ValueExp(new BoolValue(true)));

        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void aRejectedWriteLeavesTheHeapUnchanged() throws MyException {
        var stmt = new WriteHeapStmt("v", new ValueExp(new BoolValue(true)));

        assertThrows(StatementException.class, () -> stmt.execute(state));

        assertEquals(new IntValue(20), state.getHeap().get(address));
    }

    @Test
    void anErrorInsideTheExpressionPropagates() {
        var stmt = new WriteHeapStmt("v", new VarExp("missing"));

        assertThrows(MyException.class, () -> stmt.execute(state));
    }

    @Test
    void toStringShowsTheStatementInWHForm() {
        assertEquals("wH(v, 30)", new WriteHeapStmt("v", num(30)).toString());
    }
}