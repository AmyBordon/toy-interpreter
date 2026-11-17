package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.type.RefType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;

public record ReadHeapExp(IExp exp) implements IExp{
    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws MyException {
        if (!(exp.eval(symTable, heap) instanceof RefValue))
            throw new ExpressionException("rH: the operand is not af type Ref");
        if(!(heap.isDefined(((RefValue) exp.eval(symTable, heap)).address())))
            throw new ExpressionException("rH: the address is not in the heap");
        return heap.get(((RefValue) exp.eval(symTable, heap)).address());
    }

    @Override
    public String toString() {
        return "rH(" + exp + ")";
    }
}
