package interpreter.model.type;

import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

public class StringType implements IType{
    @Override public IValue defaultValue() { return new StringValue(""); }
    @Override public String toString() { return "string"; }
}
