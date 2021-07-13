package io.github.thebusybiscuit.dough.inventory.factory;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.dough.inventory.CustomInventory;
import io.github.thebusybiscuit.dough.inventory.InventoryLayout;
import io.github.thebusybiscuit.dough.inventory.SlotGroup;

public class CustomInventoryFactory {

    private final Plugin plugin;

    public CustomInventoryFactory(@Nonnull Plugin plugin) {
        this.plugin = plugin;
        registerListener(plugin);
    }

    private @Nonnull CustomInventoryListener registerListener(@Nonnull Plugin plugin) {
        CustomInventoryListener listener = new CustomInventoryListener(this);
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        return listener;
    }

    public final @Nonnull Plugin getPlugin() {
        return plugin;
    }

    @OverridingMethodsMustInvokeSuper
    public @Nonnull CustomInventory createInventory(@Nonnull InventoryLayout layout) {
        CustomInventoryImpl impl = new CustomInventoryImpl(this, layout);
        String title = layout.getTitle();
        Inventory inv;

        if (title == null) {
            inv = Bukkit.createInventory(impl, layout.getSize());
        } else {
            inv = Bukkit.createInventory(impl, layout.getSize(), title);
        }

        impl.setInventory(inv);

        // Set all default items
        for (SlotGroup group : layout.getSlotGroups()) {
            impl.setAll(group, group.getDefaultItemStack());
        }

        return impl;
    }

}
