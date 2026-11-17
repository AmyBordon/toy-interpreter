package interpreter.model.exp;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.Test;

class ReadHeapExpTest {
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();
    private final IHeap heap = new MyHeap();

    private static IExp num(int n) {
        return new ValueExp(new IntValue(n));
    }

    @Test
    void readsTheValueAtTheAddressOfAReference() throws MyException {
        int address = heap.allocate(new IntValue(20));
        symTable.put("v", new RefValue(address, new IntType()));

        IExp e = new ReadHeapExp(new VarExp("v"));

        assertEquals(new IntValue(20), e.eval(symTable, heap));
    }

    @Test
    void readsThroughTwoLevelsOfReferences() throws MyException {
        // Ex7: v -> (1, int), a -> (2, Ref(int)), heap {1 -> 20, 2 -> (1, int)}
        int vAddress = heap.allocate(new IntValue(20));
        int aAddress = heap.allocate(new RefValue(vAddress, new IntType()));
        symTable.put("v", new RefValue(vAddress, new IntType()));
        symTable.put("a", new RefValue(aAddress, new RefType(new IntType())));

        IExp e = new ReadHeapExp(new ReadHeapExp(new VarExp("a")));

        assertEquals(new IntValue(20), e.eval(symTable, heap));
    }

    @Test
    void theReadValueCanBeUsedInAnArithmeticExpression() throws MyException {
        int address = heap.allocate(new IntValue(20));
        symTable.put("v", new RefValue(address, new IntType()));

        IExp e = new ArithExp(ArithOp.ADD, new ReadHeapExp(new VarExp("v")), num(5));

        assertEquals(new IntValue(25), e.eval(symTable, heap));
    }

    @Test
    void readingDoesNotChangeTheHeap() throws MyException {
        int address = heap.allocate(new IntValue(20));
        symTable.put("v", new RefValue(address, new IntType()));

        new ReadHeapExp(new VarExp("v")).eval(symTable, heap);

        assertEquals(1, heap.getContent().size());
        assertEquals(new IntValue(20), heap.get(address));
    }

    @Test
    void anOperandThatIsNotAReferenceThrows() {
        IExp e = new ReadHeapExp(num(5));

        assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
    }

    @Test
    void aNullReferenceThrows() throws MyException {
        symTable.put("v", new RefValue(0, new IntType()));
        IExp e = new ReadHeapExp(new VarExp("v"));

        assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
    }

    @Test
    void anAddressThatIsNotInTheHeapThrows() throws MyException {
        symTable.put("v", new RefValue(7, new IntType()));
        IExp e = new ReadHeapExp(new VarExp("v"));

        assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
    }

    @Test
    void anUndefinedVariableThrows() {
        IExp e = new ReadHeapExp(new VarExp("missing"));

        assertThrows(MyException.class, () -> e.eval(symTable, heap));
    }

    @Test
    void toStringShowsTheExpressionInRHForm() {
        assertEquals("rH(v)", new ReadHeapExp(new VarExp("v")).toString());
        assertEquals("rH(rH(a))",
                new ReadHeapExp(new ReadHeapExp(new VarExp("a"))).toString());
    }
}