package io.github.thebusybiscuit.dough.inventory.implementation;

import java.io.InputStream;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import io.github.thebusybiscuit.dough.inventory.CustomInventory;
import io.github.thebusybiscuit.dough.inventory.InventoryLayout;

// TODO: Perhaps rename this too
public final class Inventories {

    private Inventories() {}

    @ParametersAreNonnullByDefault
    public static @Nonnull InventoryLayout getLayoutFromStream(InputStream stream) {
        // TODO: Load an InventoryLayout from an InputStream
        return null;
    }

    public static @Nonnull CustomInventory createInventory(@Nonnull InventoryLayout layout) {
        Inventory inv = Bukkit.createInventory(layout, layout.getSize());

        // TODO Wrap Inventory using CustomInventory

        return null;
    }

}
