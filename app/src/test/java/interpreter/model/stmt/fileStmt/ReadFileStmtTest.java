package interpreter.model.stmt.fileStmt;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.stmt.NopStmt;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ReadFileStmtTest {
    private PrgState state;
    StringValue name;
    @TempDir
    Path dir;

    @BeforeEach
    void setUp() throws ADTException, IOException {
        state = PrgState.of(new NopStmt());
        Path f = dir.resolve("file.txt");
        Files.writeString(f, "15\n50\n");
        name = new StringValue(f.toString());
    }

    @AfterEach
    void tearDown() throws Exception {
        for (BufferedReader reader : state.getFileTable().toMap().values())
            reader.close();
    }

    private ValueExp file() {
        return new ValueExp(name);
    }

    private void openFileAndDeclareN() throws MyException {
        new OpenRFileStmt(file()).execute(state);
        state.getSymTable().put("n", new IntValue(0));
    }

    @Test
    void readsTheFirstLine() throws MyException {
        openFileAndDeclareN();
        new ReadFileStmt(file(), "n").execute(state);
        assertEquals(new IntValue(15), state.getSymTable().lookup("n"));
    }

    @Test
    void readsTheLinesInOrder() throws MyException {
        openFileAndDeclareN();
        new ReadFileStmt(file(), "n").execute(state);
        new ReadFileStmt(file(), "n").execute(state);
        assertEquals(new IntValue(50), state.getSymTable().lookup("n"));
    }

    @Test
    void readGivesZeroAtTheEndOfTheFile() throws MyException {
        openFileAndDeclareN();
        new ReadFileStmt(file(), "n").execute(state);   // 15
        new ReadFileStmt(file(), "n").execute(state);   // 50
        new ReadFileStmt(file(), "n").execute(state);   // end of file
        assertEquals(new IntValue(0), state.getSymTable().lookup("n"));
    }


    @Test
    void readFromAFileThatIsNotOpenThrows() throws MyException {
        state.getSymTable().put("n", new IntValue(0));
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(file(), "n").execute(state));
    }

    @Test
    void readIntoAnUndeclaredVariableThrows() throws MyException {
        new OpenRFileStmt(file()).execute(state);
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(file(), "n").execute(state));
    }

    @Test
    void readIntoANonIntVariableThrows() throws MyException {
        new OpenRFileStmt(file()).execute(state);
        state.getSymTable().put("n", new BoolValue(false));
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(file(), "n").execute(state));
    }

    @Test
    void nonStringExpressionThrows() throws MyException {
        state.getSymTable().put("n", new IntValue(0));
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(new ValueExp(new IntValue(5)), "n").execute(state));
    }

    @Test
    void lineThatIsNotANumberThrows() throws Exception {
        Path bad = dir.resolve("bad.txt");
        Files.writeString(bad, "abc\n");
        ValueExp badFile = new ValueExp(new StringValue(bad.toString()));
        new OpenRFileStmt(badFile).execute(state);
        state.getSymTable().put("n", new IntValue(0));
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(badFile, "n").execute(state));
    }

    @Test
    void failedReadLeavesTheVariableUnchanged() throws Exception {
        Path bad = dir.resolve("bad.txt");
        Files.writeString(bad, "abc\n");
        ValueExp badFile = new ValueExp(new StringValue(bad.toString()));
        new OpenRFileStmt(badFile).execute(state);
        state.getSymTable().put("n", new IntValue(7));
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(badFile, "n").execute(state));
        assertEquals(new IntValue(7), state.getSymTable().lookup("n"));
    }

}
