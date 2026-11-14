package interpreter.model.adt;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.ADTException;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import java.util.Map;
import org.junit.jupiter.api.Test;

class MyHeapTest {
    private final IHeap heap = new MyHeap();

    @Test
    void firstAllocationGetsAddressOne() {
        assertEquals(1, heap.allocate(new IntValue(10)));
    }

    @Test
    void addressesGrowByOne() {
        assertEquals(1, heap.allocate(new IntValue(10)));
        assertEquals(2, heap.allocate(new IntValue(20)));
        assertEquals(3, heap.allocate(new IntValue(30)));
    }

    @Test
    void getReturnsTheAllocatedValue() throws ADTException {
        int address = heap.allocate(new IntValue(10));
        assertEquals(new IntValue(10), heap.get(address));
    }

    @Test
    void eachAddressKeepsItsOwnValue() throws ADTException {
        int a = heap.allocate(new IntValue(10));
        int b = heap.allocate(new IntValue(20));
        assertEquals(new IntValue(10), heap.get(a));
        assertEquals(new IntValue(20), heap.get(b));
    }

    @Test
    void getOfAMissingAddressThrows() {
        assertThrows(ADTException.class, () -> heap.get(1));
    }

    @Test
    void getOfAddressZeroThrows() {
        heap.allocate(new IntValue(10));
        assertThrows(ADTException.class, () -> heap.get(0));
    }

    @Test
    void updateChangesTheValue() throws ADTException {
        int address = heap.allocate(new IntValue(10));
        heap.update(address, new IntValue(99));
        assertEquals(new IntValue(99), heap.get(address));
    }

    @Test
    void updateDoesNotChangeOtherLocations() throws ADTException {
        int a = heap.allocate(new IntValue(10));
        int b = heap.allocate(new IntValue(20));
        heap.update(a, new IntValue(99));
        assertEquals(new IntValue(20), heap.get(b));
    }

    @Test
    void updateOfAMissingAddressThrows() {
        assertThrows(ADTException.class, () -> heap.update(5, new IntValue(1)));
    }

    @Test
    void failedUpdateDoesNotCreateTheAddress() {
        assertThrows(ADTException.class, () -> heap.update(5, new IntValue(1)));
        assertFalse(heap.isDefined(5));
    }

    @Test
    void isDefinedIsFalseBeforeAllocation() {
        assertFalse(heap.isDefined(1));
    }

    @Test
    void isDefinedIsTrueAfterAllocation() {
        int address = heap.allocate(new IntValue(10));
        assertTrue(heap.isDefined(address));
    }

    @Test
    void getContentHoldsAllLocations() {
        heap.allocate(new IntValue(10));
        heap.allocate(new IntValue(20));
        assertEquals(Map.of(1, new IntValue(10), 2, new IntValue(20)), heap.getContent());
    }

    @Test
    void getContentOfAnEmptyHeapIsEmpty() {
        assertTrue(heap.getContent().isEmpty());
    }

    @Test
    void getContentIsNotAffectedByLaterAllocations() {
        heap.allocate(new IntValue(10));
        Map<Integer, IValue> copy = heap.getContent();
        heap.allocate(new IntValue(20));
        assertEquals(1, copy.size());
    }

    @Test
    void getContentCannotBeModified() {
        heap.allocate(new IntValue(10));
        Map<Integer, IValue> copy = heap.getContent();
        assertThrows(UnsupportedOperationException.class,
                () -> copy.put(9, new IntValue(1)));
    }

    @Test
    void setContentReplacesTheLocations() {
        heap.allocate(new IntValue(10));
        heap.allocate(new IntValue(20));
        heap.setContent(Map.of(2, new IntValue(20)));
        assertFalse(heap.isDefined(1));
        assertTrue(heap.isDefined(2));
    }

    @Test
    void setContentDoesNotReuseOldAddresses() {
        heap.allocate(new IntValue(10));
        heap.allocate(new IntValue(20));
        heap.allocate(new IntValue(30));
        heap.setContent(Map.of());
        assertEquals(4, heap.allocate(new IntValue(40)));
    }
}