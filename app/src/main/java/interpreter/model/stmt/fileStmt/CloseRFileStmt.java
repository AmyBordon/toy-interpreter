package interpreter.model.stmt.fileStmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public record CloseRFileStmt(IExp exp) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (!(exp.eval(state.getSymTable()) instanceof StringValue fileName))
            throw new StatementException("CloseRFile: " + exp + " is not a string");
        var fileTable = state.getFileTable();
        if (!fileTable.isDefined(fileName))
            throw new StatementException("CloseRFile: " + fileName + " was not previously opened");

        try {
            BufferedReader reader = fileTable.lookup(fileName);
            reader.close();
            fileTable.remove(fileName);
        } catch (IOException e) {
            throw new StatementException("CloseFile: cannot close " + fileName
                    + ": " + e.getMessage());
        }
        return state;
    }
}
