package io.github.thebusybiscuit.dough.inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

// TODO: Rename this class
public interface CustomInventory extends InventoryHolder {

    /**
     * This returns the {@link InventoryLayout} which was used to create
     * this {@link CustomInventory}.
     * 
     * @return The {@link InventoryLayout}
     */
    @Nonnull
    InventoryLayout getLayout();

    @Nullable
    String getTitle();

    void setAll(@Nonnull SlotGroup group, @Nullable ItemStack item);

    @ParametersAreNonnullByDefault
    boolean addItem(SlotGroup group, ItemStack item);

    void setItem(int slot, @Nullable ItemStack item);

    @Nullable
    ItemStack getItem(int slot);

    default int getSize() {
        return getInventory().getSize();
    }

}
