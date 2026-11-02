package interpreter.model.stmt.fileStmt;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.stmt.AssignStmt;
import interpreter.model.stmt.NopStmt;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class OpenRFileStmtTest {
    private PrgState state;
    StringValue name;
    @TempDir
    Path dir;

    @BeforeEach
    void setUp() throws ADTException, IOException {
        state = PrgState.of(new NopStmt());;
        state.getExeStack().pop();

        Path f = dir.resolve("file.txt");
        Files.writeString(f, "7\n");
        name = new StringValue(f.toString());
    }

    @AfterEach
    void tearDown() throws Exception {
        if (state.getFileTable().isDefined(name))
            state.getFileTable().lookup(name).close();
    }

    @Test
    void openAnExistingFile() throws MyException {
        new OpenRFileStmt(new ValueExp(name)).execute(state);
        assertTrue(state.getFileTable().isDefined(name));
    }

    @Test
    void openANonExistingFile() throws MyException {
        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(new ValueExp(
                        new StringValue(dir.resolve("missing.txt")
                                .toString()))).execute(state));
    }

    @Test
    void openWithNonStringExpressionThrows() {
        var stmt = new OpenRFileStmt(new ValueExp(new IntValue(5)));
        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void openTwiceAnExistingFile() throws MyException {
        new OpenRFileStmt(new ValueExp(name)).execute(state);
        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(new ValueExp(name)).execute(state));
    }

}
