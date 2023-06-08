package io.github.bakedlibs.dough.inventory.json;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.inventory.Inventory;

import io.github.bakedlibs.dough.inventory.SlotGroup;

/**
 * Little helper class to transition a {@link String} array into
 * a {@link SlotGroup}-like map.
 * 
 * @author TheBusyBiscuit
 *
 */
class LayoutShape {

    private final Map<Character, Set<Integer>> groups = new HashMap<>();
    private final int size;

    LayoutShape(@Nonnull String[] rows) throws InvalidLayoutException {
        Validate.notNull(rows, "Layout cannot be null.");

        if (rows.length > 0 && rows.length < 7) {
            this.size = rows.length * 9;

            int i = 0;

            for (String row : rows) {
                if (row.length() == 9) {
                    addSlots(i, row);
                    i++;
                } else {
                    throw new InvalidLayoutException("Each row in a layout must have 9 characters.");
                }
            }
        } else {
            throw new InvalidLayoutException("Layout has " + rows.length + " rows. Must be 1, 2, 3, 4, 5 or 6.");
        }
    }

    /**
     * This returns the size of this {@link LayoutShape}, aka the size of the
     * corresponding {@link Inventory}.
     * 
     * @return The size of this {@link LayoutShape}
     */
    public int getSize() {
        return size;
    }

    /**
     * This returns the {@link SlotGroup}-ready representation of this shape.
     * Grouped by their unique character keys.
     * 
     * @return The "{@link SlotGroup} map".
     */
    public @Nonnull Map<Character, Set<Integer>> getGroups() {
        return groups;
    }

    @ParametersAreNonnullByDefault
    private void addSlots(int i, String row) {
        int j = 0;

        for (char identifier : row.toCharArray()) {
            Set<Integer> slots = groups.computeIfAbsent(identifier, id -> new HashSet<>());
            slots.add(i * 9 + j);
            j++;
        }
    }

}
