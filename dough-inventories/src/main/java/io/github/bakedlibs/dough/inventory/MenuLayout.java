package io.github.bakedlibs.dough.inventory;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.inventory.Inventory;

import io.github.bakedlibs.dough.inventory.builders.MenuLayoutBuilder;

/**
 * The {@link MenuLayout} covers the different {@link SlotGroup}s and basic characteristics
 * of a {@link Menu}.
 * 
 * @author TheBusyBiscuit
 *
 * @see Menu
 * @see SlotGroup
 * @see MenuLayoutBuilder
 * 
 */
public interface MenuLayout {

    /**
     * This returns all defined {@link SlotGroup}s for this
     * {@link MenuLayout}.
     * 
     * @return A {@link Set} containing every {@link SlotGroup}
     */
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

    /**
     * This returns the {@link SlotGroup} with the given identifier.
     * <p>
     * If no corresponding {@link SlotGroup} was found, it will throw an
     * {@link IllegalArgumentException}.
     * 
     * @param identifier
     *            The unique identifier for this {@link SlotGroup}.
     * 
     * @return The corresponding {@link SlotGroup}
     */
    @Nonnull
    SlotGroup getGroup(char identifier);

    /**
     * This returns the {@link SlotGroup} present at the given slot.
     * <p>
     * If no corresponding {@link SlotGroup} was found, it will throw an
     * {@link IllegalArgumentException}.
     * 
     * @param slot
     *            The slot
     * 
     * @return The corresponding {@link SlotGroup}
     */
    @Nonnull
    SlotGroup getGroup(int slot);

    /**
     * This returns the {@link SlotGroup} with the given name.
     * <p>
     * If no corresponding {@link SlotGroup} was found, it will throw an
     * {@link IllegalArgumentException}.
     * 
     * @param name
     *            The unique name of this {@link SlotGroup}.
     * 
     * @return The corresponding {@link SlotGroup}
     */
    @Nonnull
    SlotGroup getGroup(@Nonnull String name);

}
