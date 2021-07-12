package io.github.thebusybiscuit.dough.inventory;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

// TODO: Rename this class
public interface CustomInventory {

    /**
     * This returns the {@link InventoryLayout} which was used to create
     * this {@link CustomInventory}.
     * 
     * @return The {@link InventoryLayout}
     */
    @Nonnull
    InventoryLayout getLayout();

    @ParametersAreNonnullByDefault
    void setAll(SlotGroup group, ItemStack item);

    @ParametersAreNonnullByDefault
    boolean addItem(SlotGroup group, ItemStack item);

}
