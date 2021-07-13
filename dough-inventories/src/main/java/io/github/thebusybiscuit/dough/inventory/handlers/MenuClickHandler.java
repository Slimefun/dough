package io.github.thebusybiscuit.dough.inventory.handlers;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.dough.inventory.payloads.MenuClickPayload;

@FunctionalInterface
public interface MenuClickHandler {

    void onClick(@Nonnull MenuClickPayload e);

}
