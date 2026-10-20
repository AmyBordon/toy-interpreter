package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;

public record LogicExp(LogicOp op, IExp left, IExp right) implements IExp{
    @Override
    public IValue eval(IDictionary<String, IValue> symTable) throws MyException {
        if (!(left.eval(symTable) instanceof IntValue(int n1)))
            throw new ExpressionException("first operand is not an integer");
        if (!(right.eval(symTable) instanceof IntValue(int n2)))
            throw new ExpressionException("second operand is not an integer");
        return switch (op) {
            case AND -> new IntValue(n1 & n2);
            case OR -> new IntValue(n1 | n2);
        };
    }

    @Override
    public String toString() { return "(" + left + " " + op + " " + right + ")"; }
}
