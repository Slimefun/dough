package io.github.bakedlibs.dough.inventory.builders;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;

import io.github.bakedlibs.dough.inventory.MenuLayout;
import io.github.bakedlibs.dough.inventory.SlotGroup;

class MenuLayoutImpl implements MenuLayout {

    private final int size;
    private final String title;

    private final Set<SlotGroup> groups = new HashSet<>();
    private final SlotGroup[] groupsBySlot;

    MenuLayoutImpl(@Nonnull MenuLayoutBuilder builder) {
        this.size = builder.size;
        this.title = builder.title;
        this.groups.addAll(builder.groups);
        this.groupsBySlot = new SlotGroup[size];

        Set<Character> uniqueCharacters = new HashSet<>();
        Set<String> uniqueNames = new HashSet<>();
        Set<Integer> coveredSlots = new HashSet<>();

        for (SlotGroup group : groups) {
            Validate.notNull(group, "SlotGroups cannot be null.");

            // Check for duplicate identifiers
            if (!uniqueCharacters.add(group.getIdentifier())) {
                throw new IllegalStateException("Identifier '" + group.getIdentifier() + "' is used more than once!");
            }

            // Check for duplicate names
            if (!uniqueNames.add(group.getName())) {
                throw new IllegalStateException("Name '" + group.getName() + "' is used more than once!");
            }

            for (int slot : group) {
                Validate.isTrue(slot >= 0 && slot < size, "The slot " + slot + " is outside the bounds of this inventory (0 - " + size + ')');

                if (!coveredSlots.add(slot)) {
                    throw new IllegalStateException("Slot " + slot + " is defined by multiple slot groups.");
                }

                groupsBySlot[slot] = group;
            }
        }

        if (coveredSlots.size() != size) {
            throw new IllegalStateException("Only " + coveredSlots.size() + " / " + size + " slots are covered by slot groups.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull Set<SlotGroup> getSlotGroups() {
        return Collections.unmodifiableSet(groups);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable String getTitle() {
        return title;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull SlotGroup getGroup(char identifier) {
        SlotGroup result = findGroup(group -> group.getIdentifier() == identifier);

        if (result != null) {
            return result;
        } else {
            throw new IllegalArgumentException("Could not find a SlotGroup with the identifier '" + identifier + "'");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull SlotGroup getGroup(int slot) {
        Validate.isTrue(slot >= 0, "Slot cannot be a negative number: " + slot);
        Validate.isTrue(slot < size, "Slot " + slot + " is not within the inventory size of " + size);

        /*
         * Using an Array makes this much faster.
         * And since this method will be used for the click events, some
         * optimization here will be good to have.
         */
        return groupsBySlot[slot];
    }

    /**
     * {@inheritDoc}
     */
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
        for (SlotGroup group : groups) {
            if (predicate.test(group)) {
                return group;
            }
        }

        return null;
    }

}
