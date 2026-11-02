package interpreter.model;

import interpreter.model.adt.*;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;

public class PrgState {
    private IStack<IStmt> exeStack;
    private IDictionary<String, IValue> symTable;
    private IList<IValue> out;
    private final IStmt originalProgram;
    private IDictionary<StringValue, BufferedReader> fileTable;

    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    public PrgState(IStack<IStmt> exeStack, IDictionary<String,
            IValue> symTable, IList<IValue> out,
                    IDictionary<StringValue, BufferedReader> fileTable,
                    IStmt program) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.originalProgram = program;
        this.fileTable = fileTable;
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
        return "ExeStack: " + exeStack + "\nSymTable: " + symTable + "\nFileTable: " + fileTable.toMap().keySet()+ "\nOut: " + out;
    }

    public static PrgState of(IStmt program) {
        return new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(),
                new MyDictionary<>(), program);
    }

    public void setFileTable(IDictionary<StringValue, BufferedReader> fileTable) {
        this.fileTable = fileTable;
    }

    public IDictionary<StringValue, BufferedReader> getFileTable(){
        return this.fileTable;
    }
}
