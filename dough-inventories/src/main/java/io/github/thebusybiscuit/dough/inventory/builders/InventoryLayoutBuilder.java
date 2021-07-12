package io.github.thebusybiscuit.dough.inventory.builders;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.dough.inventory.InventoryLayout;
import io.github.thebusybiscuit.dough.inventory.SlotGroup;

public class InventoryLayoutBuilder {

    private final int size;

    public InventoryLayoutBuilder(int size) {
        // TODO: Validate size input
        this.size = size;
    }

    @ParametersAreNonnullByDefault
    public @Nonnull InventoryLayoutBuilder addSlotGroup(SlotGroup group) {
        // TODO: Create slot group
        return this;
    }

    public @Nonnull InventoryLayout build() {
        // TODO: Implement builder
        return null;
    }

}
