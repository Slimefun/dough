package io.github.bakedlibs.dough.inventory;

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

import io.github.bakedlibs.dough.inventory.factory.MenuFactory;

public interface Menu extends InventoryHolder {

    /**
     * This method returns the {@link MenuFactory} which was used
     * to create this {@link Menu}.
     * 
     * @return The original {@link MenuFactory}
     */
    @Nonnull
    MenuFactory getFactory();

    /**
     * This returns the {@link MenuLayout} which was used to create
     * this {@link Menu}.
     * 
     * @return The {@link MenuLayout}
     */
    @Nonnull
    MenuLayout getLayout();

    /**
     * This returns the title of this {@link Menu}.
     * If no title was set, null will be returned.
     * 
     * @return The title of this {@link Menu} or null
     */
    @Nullable
    String getTitle();

    void setAll(@Nonnull SlotGroup group, @Nullable ItemStack item);

    default void clear(@Nonnull SlotGroup group) {
        setAll(group, null);
    }

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

    /**
     * This returns the size of this {@link Menu}.
     * 
     * @return The size of the {@link Menu}.
     */
    default int getSize() {
        return getInventory().getSize();
    }

}
