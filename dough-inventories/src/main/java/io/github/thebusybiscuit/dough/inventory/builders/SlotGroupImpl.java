package io.github.thebusybiscuit.dough.inventory.builders;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.dough.inventory.SlotGroup;
import io.github.thebusybiscuit.dough.inventory.handlers.MenuClickHandler;

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

    private @Nonnull int[] convertSlots(@Nonnull Set<Integer> slots) {
        // @formatter:off
        return slots.stream()
                .mapToInt(Integer::intValue)
                .sorted()
                .toArray();
        // @formatter:on
    }

    @Override
    public char getCharIdentifier() {
        return identifier;
    }

    @Override
    public boolean isInteractable() {
        return interactable;
    }

    @Override
    public @Nonnull String getName() {
        return name;
    }

    @Override
    public @Nonnull int[] getSlots() {
        return slots;
    }

    @Override
    public @Nullable ItemStack getDefaultItemStack() {
        return defaultItem;
    }

    @Override
    public @Nonnull MenuClickHandler getClickHandler() {
        return clickHandler;
    }

}
