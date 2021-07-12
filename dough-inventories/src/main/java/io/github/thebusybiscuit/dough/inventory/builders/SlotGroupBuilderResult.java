package io.github.thebusybiscuit.dough.inventory.builders;

import java.util.Set;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.dough.inventory.SlotGroup;

class SlotGroupBuilderResult implements SlotGroup {

    private final char identifier;
    private final String name;
    private final boolean interactable;
    private final ItemStack defaultItem;
    private final int[] slots;

    SlotGroupBuilderResult(@Nonnull SlotGroupBuilder builder) {
        this.identifier = builder.identifier;
        this.name = builder.name;
        this.interactable = builder.interactable;
        this.defaultItem = builder.defaultItem;
        this.slots = convertSlots(builder.slots);
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
    public String getName() {
        return name;
    }

    @Override
    public int[] getSlots() {
        return slots;
    }

    @Override
    public ItemStack getDefaultItemStack() {
        return defaultItem;
    }

}
