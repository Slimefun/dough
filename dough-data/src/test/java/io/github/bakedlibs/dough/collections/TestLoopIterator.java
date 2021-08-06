package io.github.bakedlibs.dough.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class TestLoopIterator {

    @Test
    void testLooping() {
        Collection<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        LoopIterator<Integer> iterator = new LoopIterator<>(integers);
        Assertions.assertTrue(iterator.hasNext());
        int index = 0;
        for (Integer integer : integers) {
            Assertions.assertTrue(iterator.hasNext());
            Assertions.assertEquals(index, iterator.getIndex());
            Assertions.assertEquals(integer, iterator.next());
            index++;
        }
        // Check loop
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(1, iterator.next());
    }

    @Test
    void testStream() {
        Collection<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        LoopIterator<Integer> iterator = new LoopIterator<>(integers);
        Collection<Integer> integersCopy = iterator
                .stream()
                .limit(integers.size())
                .collect(Collectors.toList());
        CollectionTestUtils.assertHaveEqualElements(integers, integersCopy);
    }

    @Test
    void testInvalidCases() {
        LoopIterator<Object> emptyIterator = new LoopIterator<>(Collections.emptyList());
        Assertions.assertFalse(emptyIterator.hasNext());
        Assertions.assertThrows(NoSuchElementException.class, emptyIterator::next, "The given collection was empty.");
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LoopIterator<>(emptyIterator), "Cannot loop-iterate over a LoopIterator");
    }

    @Test
    void testFinding() {
        Collection<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        LoopIterator<Integer> iterator = new LoopIterator<>(integers);
        Assertions.assertTrue(iterator.find(i -> i <= 10 && i > 0).isPresent());
        Assertions.assertFalse(iterator.find(i -> i > 10 || i < 1).isPresent());
    }
}
