package io.github.bakedlibs.dough.inventory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import io.github.bakedlibs.dough.inventory.builders.SlotGroupBuilder;

class TestSlotGroupIterator {

    @Test
    void testIteratorEnd() {
        SlotGroup group = new SlotGroupBuilder('x', "test").withSlots(0, 1, 2).build();

        assertNotNull(group);

        Iterator<Integer> iterator = group.iterator();

        while (iterator.hasNext()) {
            iterator.next();
        }

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testIteratorRemove() {
        SlotGroup group = new SlotGroupBuilder('x', "test").withSlots(0, 1, 2).build();

        assertNotNull(group);

        Iterator<Integer> iterator = group.iterator();

        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

}
