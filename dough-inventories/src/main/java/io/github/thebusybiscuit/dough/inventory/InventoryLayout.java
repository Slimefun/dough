package io.github.thebusybiscuit.dough.inventory;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public interface InventoryLayout {

    @Nonnull
    Set<SlotGroup> getSlotGroups();

    /**
     * This returns the size of the resulting {@link Inventory}.
     * 
     * @return The {@link Inventory} size
     */
    int getSize();

    @Nullable
    String getTitle();

    @Nonnull
    SlotGroup getGroup(char identifier);

    @Nonnull
    SlotGroup getGroup(int slot);

    @Nonnull
    SlotGroup getGroup(@Nonnull String name);

    default @Nonnull CustomInventory createInventory() {
        CustomInventoryImpl impl = new CustomInventoryImpl(this);
        String title = getTitle();
        Inventory inv;

        if (title == null) {
            inv = Bukkit.createInventory(impl, getSize());
        } else {
            inv = Bukkit.createInventory(impl, getSize(), title);
        }

        impl.setInventory(inv);

        for (SlotGroup group : getSlotGroups()) {
            impl.setAll(group, group.getDefaultItemStack());
        }

        return impl;
    }

}
