package io.github.thebusybiscuit.dough.inventory;

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.dough.inventory.factory.CustomInventoryFactory;

// TODO: Rename this class
public interface CustomInventory extends InventoryHolder {

    @Nonnull
    CustomInventoryFactory getFactory();

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
    @Nullable
    ItemStack addItem(SlotGroup group, ItemStack item);

    void setItem(int slot, @Nullable ItemStack item);

    @Nullable
    ItemStack getItem(int slot);

    default @Nonnull InventoryView open(@Nonnull Player player) {
        Validate.notNull(player, "The Player must not be null");
        return player.openInventory(getInventory());
    }

    default void closeAllViews() {
        Inventory inv = getInventory();
        Iterator<HumanEntity> iterator = inv.getViewers().iterator();

        while (iterator.hasNext()) {
            iterator.next().closeInventory();
        }
    }

    default int getSize() {
        return getInventory().getSize();
    }

}
