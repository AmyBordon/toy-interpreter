package interpreter.controller;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GarbageCollectorTest {

    private static IValue ref(int address) {
        return new RefValue(address, new IntType());
    }

    private static IValue refToRef(int address) {
        return new RefValue(address, new RefType(new IntType()));
    }

    private static IValue num(int n) {
        return new IntValue(n);
    }

    // The state of Ex9 after new(v, 30): v -> (3, int), a -> (2, Ref(int)).
    // Location 1 is reachable only through location 2; location 4 is real garbage.
    private final List<IValue> ex9SymTable = List.of(ref(3), refToRef(2));
    private final Map<Integer, IValue> ex9Heap = Map.of(
            1, num(20), 2, ref(1), 3, num(30), 4, num(7));

    // A longer example: x -> 4 -> 2 -> 1 and y -> 6; the chain 7 -> 5 -> 3 is garbage.
    private final Map<Integer, IValue> chainHeap = Map.of(
            1, num(20), 2, ref(1), 3, num(99), 4, refToRef(2),
            5, ref(3), 6, num(30), 7, refToRef(5));

    // ---------- getAddrFromSymTable ----------

    @Test
    void addressesOfTheReferencesInTheSymTableAreCollected() {
        assertEquals(Set.of(2, 3), GarbageCollector.getAddrFromSymTable(ex9SymTable));
    }

    @Test
    void valuesThatAreNotReferencesAreIgnored() {
        List<IValue> values = List.of(num(5), new BoolValue(true), ref(8));

        assertEquals(Set.of(8), GarbageCollector.getAddrFromSymTable(values));
    }

    @Test
    void twoVariablesWithTheSameAddressGiveOneAddress() {
        List<IValue> values = List.of(ref(2), ref(2));

        assertEquals(Set.of(2), GarbageCollector.getAddrFromSymTable(values));
    }

    @Test
    void anEmptySymTableGivesNoAddresses() {
        assertTrue(GarbageCollector.getAddrFromSymTable(List.of()).isEmpty());
    }

    // ---------- reachableAddresses ----------

    @Test
    void rootsWithoutReferencesInTheHeapAreTheirOwnClosure() {
        Map<Integer, IValue> heap = Map.of(1, num(20), 2, num(30));

        assertEquals(Set.of(1, 2), GarbageCollector.reachableAddresses(Set.of(1, 2), heap));
    }

    @Test
    void followsOneReferenceInsideTheHeap() {
        Map<Integer, IValue> heap = Map.of(1, num(20), 2, ref(1));

        assertEquals(Set.of(1, 2), GarbageCollector.reachableAddresses(Set.of(2), heap));
    }

    @Test
    void followsAChainOfReferences() {
        Set<Integer> reachable = GarbageCollector.reachableAddresses(Set.of(4, 6), chainHeap);

        assertEquals(Set.of(1, 2, 4, 6), reachable);
    }

    @Test
    void doesNotReachLocationsNobodyPointsToFromTheRoots() {
        Set<Integer> reachable = GarbageCollector.reachableAddresses(Set.of(4, 6), chainHeap);

        assertFalse(reachable.contains(3));
        assertFalse(reachable.contains(5));
        assertFalse(reachable.contains(7));
    }

    @Test
    void locationsThatPointToEachOtherDoNotCauseAnEndlessLoop() {
        Map<Integer, IValue> heap = Map.of(2, ref(4), 4, ref(2));

        assertEquals(Set.of(2, 4), GarbageCollector.reachableAddresses(Set.of(2), heap));
    }

    @Test
    void aLocationThatPointsToItselfDoesNotCauseAnEndlessLoop() {
        Map<Integer, IValue> heap = Map.of(1, ref(1));

        assertEquals(Set.of(1), GarbageCollector.reachableAddresses(Set.of(1), heap));
    }

    @Test
    void aRootThatIsNotInTheHeapDoesNotThrow() {
        Set<Integer> reachable = GarbageCollector.reachableAddresses(Set.of(9), Map.of());

        assertEquals(Set.of(9), reachable);
    }

    @Test
    void aReferenceToAnAddressThatIsNotInTheHeapDoesNotThrow() {
        Map<Integer, IValue> heap = Map.of(1, ref(0));   // 0 is the null address

        assertDoesNotThrow(() -> GarbageCollector.reachableAddresses(Set.of(1), heap));
    }

    @Test
    void noRootsReachNothing() {
        assertTrue(GarbageCollector.reachableAddresses(Set.of(), ex9Heap).isEmpty());
    }

    @Test
    void theRootsGivenAsArgumentAreNotModified() {
        Set<Integer> roots = new HashSet<>(Set.of(4, 6));

        GarbageCollector.reachableAddresses(roots, chainHeap);

        assertEquals(Set.of(4, 6), roots);
    }

    // ---------- unsafeGarbageCollector ----------

    @Test
    void theUnsafeCollectorKeepsOnlyTheAddressesOfTheSymTable() {
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(ex9SymTable);

        Map<Integer, IValue> kept = GarbageCollector.unsafeGarbageCollector(roots, ex9Heap);

        assertEquals(Set.of(2, 3), kept.keySet());
    }

    @Test
    void theUnsafeCollectorRemovesALocationThatIsReachableThroughTheHeap() {
        // the bug of Ex9: location 1 is still needed by rH(rH(a))
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(ex9SymTable);

        Map<Integer, IValue> kept = GarbageCollector.unsafeGarbageCollector(roots, ex9Heap);

        assertFalse(kept.containsKey(1));
    }

    // ---------- safeGarbageCollector ----------

    @Test
    void theSafeCollectorKeepsTheLocationsReachableThroughTheHeap() {
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(ex9SymTable);

        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(roots, ex9Heap);

        assertEquals(Set.of(1, 2, 3), kept.keySet());   // 4 is garbage, 1 is not
    }

    @Test
    void theSafeCollectorKeepsALocationThatIsReachableOnlyThroughAnotherLocation() {
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(ex9SymTable);

        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(roots, ex9Heap);

        assertTrue(kept.containsKey(1));
    }

    @Test
    void theSafeCollectorRemovesALocationNobodyPointsTo() {
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(ex9SymTable);

        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(roots, ex9Heap);

        assertFalse(kept.containsKey(4));
    }

    @Test
    void theSafeCollectorKeepsTheStoredValuesUnchanged() {
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(ex9SymTable);

        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(roots, ex9Heap);

        assertEquals(num(20), kept.get(1));
        assertEquals(ref(1), kept.get(2));
        assertEquals(num(30), kept.get(3));
    }

    @Test
    void theSafeCollectorRemovesAWholeChainOfGarbage() {
        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(Set.of(4, 6), chainHeap);

        assertEquals(Set.of(1, 2, 4, 6), kept.keySet());   // 7 -> 5 -> 3 is gone
    }

    @Test
    void theSafeCollectorRemovesGarbageThatPointsToItself() {
        // two locations that refer to each other but are not reachable from any variable
        Map<Integer, IValue> heap = Map.of(1, num(5), 10, ref(11), 11, ref(10));

        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(Set.of(1), heap);

        assertEquals(Set.of(1), kept.keySet());
    }

    @Test
    void theSafeCollectorRemovesEverythingWhenThereAreNoVariables() {
        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(Set.of(), ex9Heap);

        assertTrue(kept.isEmpty());
    }

    @Test
    void theSafeCollectorOnAnEmptyHeapGivesAnEmptyHeap() {
        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(Set.of(1), Map.of());

        assertTrue(kept.isEmpty());
    }

    @Test
    void theSafeCollectorKeepsEverythingThatIsReachable() {
        Map<Integer, IValue> heap = Map.of(1, num(20), 2, ref(1));

        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(Set.of(2), heap);

        assertEquals(heap, kept);
    }
}