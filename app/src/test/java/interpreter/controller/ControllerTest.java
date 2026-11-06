package interpreter.controller;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.RelOp;
import interpreter.model.exp.RelationalExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.stmt.AssignStmt;
import interpreter.model.stmt.CompStmt;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.IfStmt;
import interpreter.model.stmt.NopStmt;
import interpreter.model.stmt.PrintStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {
    @TempDir
    Path dir;

    private static PrgState newState(IStmt program) {
        return PrgState.of(program);
    }

    private PrgState run(IStmt program) throws MyException {
        PrgState state = PrgState.of(program);
        var repo = new Repository(state, dir.resolve("log.txt").toString());
        new Controller(repo, false).allStep(); // display off
        return state;
    }

    @Test
    void oneStepOnEmptyStackThrows() throws ADTException {
        PrgState state = newState(new NopStmt());
        state.getExeStack().pop();
        var controller = new Controller(
                new Repository(state, dir.resolve("log.txt").toString()), false);
        assertThrows(StatementException.class, () -> controller.oneStep(state));
    }

    @Test
    void oneStepExecutesExactlyOneRule() throws MyException {
        PrgState state = newState(new CompStmt(new NopStmt(), new NopStmt()));
        new Controller(new Repository(state, dir.resolve("log.txt").toString()), false)
                .oneStep(state);
        state.getExeStack().pop();
        state.getExeStack().pop();
        assertTrue(state.getExeStack().isEmpty());
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

    // ---------- example 4 (files): input files live in the temp folder ----------

    @Test
    void example4PrintsFifteenAndFifty() throws Exception {
        Path in = dir.resolve("test.in");
        Files.writeString(in, "15\n50\n");

        PrgState state = run(Examples.fileExample(in.toString()));

        assertTrue(state.getExeStack().isEmpty());
        assertEquals(2, state.getOut().size());
        assertEquals(new IntValue(15), state.getOut().get(0));
        assertEquals(new IntValue(50), state.getOut().get(1));
    }

    @Test
    void example4ClosesTheFile() throws Exception {
        Path in = dir.resolve("test.in");
        Files.writeString(in, "15\n50\n");

        PrgState state = run(Examples.fileExample(in.toString()));

        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void example4WithAnEmptyFilePrintsZeroAndZero() throws Exception {
        Path in = dir.resolve("test.in");
        Files.writeString(in, "");

        PrgState state = run(Examples.fileExample(in.toString()));

        assertEquals(2, state.getOut().size());
        assertEquals(new IntValue(0), state.getOut().get(0));
        assertEquals(new IntValue(0), state.getOut().get(1));
    }

    // ---------- example 5 (relational) ----------

    @Test
    void example5Prints5() throws MyException {
        PrgState state = run(Examples.EX5);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(5), state.getOut().get(0));
    }

    @Test
    void relationalIfTakesTheElseBranchWhenTheConditionIsFalse() throws MyException {
        // same program as Ex5 but with a = 9, so (a < b) is false and print(b) runs
        IStmt program = new CompStmt(new VarDeclStmt("a", new IntType()),
                new CompStmt(new VarDeclStmt("b", new IntType()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new IntValue(9))),
                                new CompStmt(new AssignStmt("b", new ValueExp(new IntValue(7))),
                                        new IfStmt(
                                                new RelationalExp(RelOp.LT, new VarExp("a"), new VarExp("b")),
                                                new PrintStmt(new VarExp("a")),
                                                new PrintStmt(new VarExp("b")))))));

        PrgState state = run(program);

        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(7), state.getOut().get(0));
    }

    //log file

    @Test
    void logFileContainsTheFourSectionsInOrder() throws Exception {
        run(Examples.EX1);
        String log = Files.readString(dir.resolve("log.txt"));
        int a = log.indexOf("ExeStack:"), b = log.indexOf("SymTable:"),
                c = log.indexOf("Out:"), d = log.indexOf("FileTable:");
        assertTrue(0 <= a && a < b && b < c && c < d);
    }
}