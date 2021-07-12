package io.github.thebusybiscuit.dough.inventory.builders;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.dough.inventory.SlotGroup;

public class SlotGroupBuilder {

    private final char identifier;
    private final String name;

    private boolean interactable = false;

    @ParametersAreNonnullByDefault
    public SlotGroupBuilder(char identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public @Nonnull SlotGroupBuilder interactable(boolean interactable) {
        this.interactable = interactable;
        return this;
    }

    public @Nonnull SlotGroupBuilder withSlot(int slot) {
        // TODO: add slot
        return this;
    }

    public @Nonnull SlotGroupBuilder withSlots(int... slots) {
        // TODO: add slots
        return this;
    }

    public @Nonnull SlotGroupBuilder withDefaultItem(@Nonnull ItemStack item) {
        // TODO: Set default item
        return this;
    }

    public @Nonnull SlotGroup build() {
        // TODO: Implement builder
        return null;
    }

}
