package io.github.thebusybiscuit.dough.inventory.payloads;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.dough.inventory.Menu;

abstract class AbstractMenuPayload {

    private final Menu inventory;

    AbstractMenuPayload(@Nonnull Menu inventory) {
        this.inventory = inventory;
    }

    public @Nonnull Menu getInventory() {
        return inventory;
    }

}
