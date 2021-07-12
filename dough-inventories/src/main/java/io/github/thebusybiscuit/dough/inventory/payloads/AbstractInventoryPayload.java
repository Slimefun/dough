package io.github.thebusybiscuit.dough.inventory.payloads;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.dough.inventory.CustomInventory;

abstract class AbstractInventoryPayload {

    private final CustomInventory inventory;

    AbstractInventoryPayload(@Nonnull CustomInventory inventory) {
        this.inventory = inventory;
    }

    public @Nonnull CustomInventory getInventory() {
        return inventory;
    }

}
