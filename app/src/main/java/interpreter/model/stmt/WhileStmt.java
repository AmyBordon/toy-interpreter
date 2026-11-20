package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.type.BoolType;
import interpreter.model.value.BoolValue;

public record WhileStmt(IExp cond, IStmt body) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        var heap = state.getHeap();
        if(!(cond.eval(symTable, heap) instanceof BoolValue b))
            throw new StatementException(cond + " is not of type bool");
        if(b.val()) {
            state.getExeStack().push(this);
            state.getExeStack().push(body);
        }
        return state;
    }

    @Override public String toString() { return "(while (" + cond + ") " + body + ")"; }
}