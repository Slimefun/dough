package io.github.thebusybiscuit.dough.inventory;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.inventory.Inventory;

public interface MenuLayout {

    @Nonnull
    Set<SlotGroup> getSlotGroups();

    /**
     * This returns the size of the resulting {@link Inventory}.
     * 
     * @return The {@link Inventory} size
     */
    int getSize();

    /**
     * This returns the title to be set for the resulting {@link Menu}.
     * If no title was set, this will return null.
     * 
     * @return The title or null
     */
    @Nullable
    String getTitle();

    @Nonnull
    SlotGroup getGroup(char identifier);

    @Nonnull
    SlotGroup getGroup(int slot);

    @Nonnull
    SlotGroup getGroup(@Nonnull String name);

}
