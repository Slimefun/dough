package io.github.bakedlibs.dough.inventory;

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.inventory.builders.SlotGroupBuilder;
import io.github.bakedlibs.dough.inventory.handlers.MenuClickHandler;

/**
 * A {@link SlotGroup} groups slots together and divides an {@link Inventory}
 * into distinct regions which can be used for easy access.
 * 
 * @author TheBusyBiscuit
 * 
 * @see MenuLayout
 * @see SlotGroupBuilder
 *
 */
public interface SlotGroup extends Iterable<Integer> {

    /**
     * This returns the unique identifier for this {@link SlotGroup},
     * we use a {@link Character} for this.
     * 
     * @return The unique identifier of this {@link SlotGroup}
     */
    @Nonnull
    char getIdentifier();

    /**
     * This method returns whether this {@link SlotGroup} is interactable.
     * An interactable {@link SlotGroup} allows the {@link Player} to take
     * and store items from/in slots of this group.
     * <p>
     * If {@link #isInteractable()} returns <code>false</code>, the click event will
     * be cancelled.
     * 
     * @return Whether this {@link SlotGroup} is interactable
     */
    boolean isInteractable();

    /**
     * This returns the name or label of this {@link SlotGroup}, names help
     * to make a {@link SlotGroup} easier to identify instead of relying on the
     * {@link Character} identifier all the time.
     * 
     * @return The name of this {@link SlotGroup}
     */
    @Nonnull
    String getName();

    /**
     * This method returns an array containing all the individual slots of
     * this {@link SlotGroup}.
     * 
     * @return An array with all slots of this {@link SlotGroup}
     */
    @Nonnull
    int[] getSlots();

    /**
     * This returns the size of this {@link SlotGroup}.
     * 
     * @return The size of this {@link SlotGroup}
     */
    default int size() {
        return getSlots().length;
    }

    /**
     * This returns the default {@link ItemStack} for this {@link SlotGroup}.
     * 
     * @return The default {@link ItemStack}, can be null
     */
    @Nullable
    ItemStack getDefaultItemStack();

    /**
     * This method returns the {@link MenuClickHandler} belonging to this {@link SlotGroup}.
     * If no {@link MenuClickHandler} was added, this will return null.
     * 
     * @return The {@link MenuClickHandler} or null
     */
    @Nullable
    MenuClickHandler getClickHandler();

    /**
     * This returns an {@link Iterator} allowing you to iterate through
     * all slots within this {@link SlotGroup}.
     */
    @Override
    default @Nonnull Iterator<Integer> iterator() {
        return new SlotGroupIterator(this);
    }

}
