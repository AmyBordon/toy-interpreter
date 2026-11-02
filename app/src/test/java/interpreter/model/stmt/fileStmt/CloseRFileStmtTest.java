package interpreter.model.stmt.fileStmt;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.stmt.NopStmt;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CloseRFileStmtTest {
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

    private ValueExp file() {
        return new ValueExp(name);
    }

    @Test
    void closeExistingFile() throws MyException {
        new OpenRFileStmt(file()).execute(state);
        new CloseRFileStmt(file()).execute(state);
        assertFalse(state.getFileTable().isDefined(name));
    }

    @Test
    void closeFileThatIsNotOpenThrows() {
        assertThrows(StatementException.class,
                () -> new CloseRFileStmt(file()).execute(state));
    }

    @Test
    void twoCloseFileThrows() throws MyException {
        new OpenRFileStmt(file()).execute(state);
        new CloseRFileStmt(file()).execute(state);
        assertThrows(StatementException.class,
                () -> new CloseRFileStmt(file()).execute(state));
    }

    @Test
    void closeWithNonStringExpressionThrows() {
        var stmt = new CloseRFileStmt(new ValueExp(new IntValue(5)));
        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void closeActuallyClosesTheReader() throws MyException {
        new OpenRFileStmt(file()).execute(state);
        BufferedReader reader = state.getFileTable().lookup(name);   // cititorul real, din tabel

        new CloseRFileStmt(file()).execute(state);

        assertThrows(IOException.class, () -> reader.read());
    }
}
