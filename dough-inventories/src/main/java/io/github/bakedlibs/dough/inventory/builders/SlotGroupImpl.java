package io.github.bakedlibs.dough.inventory.builders;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.inventory.SlotGroup;
import io.github.bakedlibs.dough.inventory.handlers.MenuClickHandler;

class SlotGroupImpl implements SlotGroup {

    private final char identifier;
    private final String name;
    private final boolean interactable;
    private final ItemStack defaultItem;
    private final MenuClickHandler clickHandler;
    private final int[] slots;

    SlotGroupImpl(@Nonnull SlotGroupBuilder builder) {
        this.identifier = builder.identifier;
        this.name = builder.name;
        this.interactable = builder.interactable;
        this.defaultItem = builder.defaultItem;
        this.slots = convertSlots(builder.slots);
        this.clickHandler = builder.clickHandler;
    }

    /**
     * This method converts our {@link Set} of slots into a sorted array.
     * 
     * @param slots
     *            The slots
     * 
     * @return A sorted array of our slots
     */
    private @Nonnull int[] convertSlots(@Nonnull Set<Integer> slots) {
        // @formatter:off
        return slots.stream()
                .mapToInt(Integer::intValue)
                .sorted()
                .toArray();
        // @formatter:on
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public char getIdentifier() {
        return identifier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInteractable() {
        return interactable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull int[] getSlots() {
        return slots;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable ItemStack getDefaultItemStack() {
        return defaultItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull MenuClickHandler getClickHandler() {
        return clickHandler;
    }

}
