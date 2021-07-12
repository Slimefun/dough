package io.github.thebusybiscuit.dough.inventory.handlers;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.dough.inventory.payloads.InventoryClickPayload;

@FunctionalInterface
public interface CustomInventoryClickHandler {

    void onClick(@Nonnull InventoryClickPayload e);

}
