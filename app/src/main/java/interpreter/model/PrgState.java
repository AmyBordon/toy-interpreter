package interpreter.model;

import interpreter.model.adt.*;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;

public class PrgState {
    private IStack<IStmt> exeStack;
    private IDictionary<String, IValue> symTable;
    private IList<IValue> out;
    private final IStmt originalProgram;
    public PrgState(IStack<IStmt> exeStack, IDictionary<String, IValue> symTable,
                    IList<IValue> out, IStmt program) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.originalProgram = program;
        exeStack.push(program);
    }

    public IStack<IStmt> getExeStack(){
        return exeStack;
    }

    public IDictionary<String, IValue> getSymTable(){
        return symTable;
    }

    public IList<IValue> getOut(){
        return out;
    }

    public void setExeStack(IStack<IStmt> newExeStack){
        exeStack = newExeStack;
    }

    public void setSymTable(IDictionary<String, IValue> newSymTable){
        symTable = newSymTable;
    }

    public void setOut(IList<IValue> newOut){
        out = newOut;
    }

    @Override
    public String toString() {
        return "ExeStack: " + exeStack + "\nSymTable: " + symTable + "\nOut: " + out;
    }
}

//PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(),
//        new MyList<>(), program);
