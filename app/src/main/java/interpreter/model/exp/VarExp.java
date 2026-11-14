package interpreter.model.exp;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.value.IValue;

public record VarExp(String id) implements IExp {

    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws MyException{
        return symTable.lookup(id);
    }

    @Override
    public String toString(){
        return id;
    }
}
