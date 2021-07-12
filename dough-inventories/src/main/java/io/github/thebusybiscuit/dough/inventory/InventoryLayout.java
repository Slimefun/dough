package io.github.thebusybiscuit.dough.inventory;

import java.util.Set;

import javax.annotation.Nonnull;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface InventoryLayout {

    @Nonnull
    Set<SlotGroup> getSlotGroups();

    /**
     * This returns the size of the resulting {@link Inventory}.
     * 
     * @return The {@link Inventory} size
     */
    int getSize();

    @Nonnull
    SlotGroup getGroup(char identifier);

    @Nonnull
    SlotGroup getGroup(int slot);

    @Nonnull
    SlotGroup getGroup(@Nonnull String name);

    default @Nonnull CustomInventory createInventory() {
        return Inventories.createInventory(this);
    }

}
