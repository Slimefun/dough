package io.github.thebusybiscuit.dough.inventory.builders;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.dough.inventory.InventoryLayout;
import io.github.thebusybiscuit.dough.inventory.SlotGroup;

class InventoryLayoutBuilderResult implements InventoryLayout {

    private final int size;
    private final Set<SlotGroup> groups = new HashSet<>();

    InventoryLayoutBuilderResult(@Nonnull InventoryLayoutBuilder builder) {
        this.size = builder.size;
        this.groups.addAll(builder.groups);
    }

    @Override
    public @Nonnull Set<SlotGroup> getSlotGroups() {
        return groups;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public @Nonnull SlotGroup getGroup(char identifier) {
        SlotGroup result = findGroup(group -> group.getCharIdentifier() == identifier);

        if (result != null) {
            return result;
        } else {
            throw new IllegalArgumentException("Could not find a SlotGroup with the identifier '" + identifier + "'");
        }
    }

    @Override
    public @Nonnull SlotGroup getGroup(int slot) {
        SlotGroup result = findGroup(group -> {
            for (int groupSlot : group.getSlots()) {
                if (groupSlot == slot) {
                    return true;
                }
            }

            return false;
        });

        if (result != null) {
            return result;
        } else {
            throw new IllegalArgumentException("Could not find a SlotGroup at slot: '" + slot + "'");
        }
    }

    @Override
    public @Nonnull SlotGroup getGroup(@Nonnull String name) {
        SlotGroup result = findGroup(group -> group.getName().equals(name));

        if (result != null) {
            return result;
        } else {
            throw new IllegalArgumentException("Could not find a SlotGroup with the name '" + name + "'");
        }
    }

    @ParametersAreNonnullByDefault
    private @Nullable SlotGroup findGroup(Predicate<SlotGroup> predicate) {
        for (SlotGroup group : getSlotGroups()) {
            if (predicate.test(group)) {
                return group;
            }
        }

        return null;
    }

}
