package interpreter.model.stmt.fileStmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.stmt.IStmt;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public record ReadFileStmt(IExp exp, String varName) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (!(exp.eval(state.getSymTable()) instanceof StringValue fileName))
            throw new StatementException("ReadFile: " + exp + " is not a string");
        var fileTable = state.getFileTable();
        if (!fileTable.isDefined(fileName))
            throw new StatementException("ReadFile: " + fileName + " is not open");

        if (!(state.getSymTable().isDefined(varName)))
            throw new StatementException("ReadFile: " + varName + " is not declared");
        if (!(state.getSymTable().lookup(varName).getType().equals(new IntType())))
            throw new StatementException("ReadFile: " + varName + " is not an int");
        try {
            BufferedReader reader = fileTable.lookup(fileName);
            String line = reader.readLine();
            int n = 0;
            if (line!=null)
                n = Integer.parseInt(line.strip());
            state.getSymTable().update(varName, new IntValue(n));
        } catch (NumberFormatException | IOException e) {
            throw new StatementException("ReadFile: cannot read from " + fileName
                    + ": " + e.getMessage());
        }
        return state;
    }

    @Override
    public String toString() {
        return "ReadFile(" + exp + ")";
    }
}
