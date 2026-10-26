package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public record LogicExp(LogicOp op, IExp left, IExp right) implements IExp {
    @Override
    public IValue eval(IDictionary<String, IValue> symTable) throws MyException {
        if (!(left.eval(symTable) instanceof BoolValue l))
            throw new ExpressionException("first operand is not a boolean");
        if (!(right.eval(symTable) instanceof BoolValue r))
            throw new ExpressionException("second operand is not a boolean");
        return switch (op) {
            case AND -> new BoolValue(l.val() && r.val());
            case OR -> new BoolValue(l.val() || r.val());
        };
    }

    @Override
    public String toString() { return "(" + left + " " + op + " " + right + ")"; }
}