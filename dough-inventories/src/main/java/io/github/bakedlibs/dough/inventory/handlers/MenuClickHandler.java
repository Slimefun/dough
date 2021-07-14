package io.github.bakedlibs.dough.inventory.handlers;

import javax.annotation.Nonnull;

import io.github.bakedlibs.dough.inventory.payloads.MenuClickPayload;

@FunctionalInterface
public interface MenuClickHandler {

    void onClick(@Nonnull MenuClickPayload e);

}
