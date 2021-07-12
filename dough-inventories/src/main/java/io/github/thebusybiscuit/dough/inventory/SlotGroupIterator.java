package io.github.thebusybiscuit.dough.inventory;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.annotation.Nonnull;

class SlotGroupIterator implements Iterator<Integer> {

    private final int[] slots;
    private int index = 0;

    SlotGroupIterator(@Nonnull SlotGroup slotGroup) {
        this.slots = slotGroup.getSlots();
    }

    @Override
    public boolean hasNext() {
        return index < slots.length;
    }

    @Override
    public Integer next() {
        if (index < slots.length) {
            int slot = slots[index];
            index++;
            return slot;
        } else {
            throw new NoSuchElementException("No more slots available.");
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Iterator#remove() is not supported!");
    }

}
