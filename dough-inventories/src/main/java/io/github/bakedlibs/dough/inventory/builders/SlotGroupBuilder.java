package io.github.bakedlibs.dough.inventory.builders;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.inventory.SlotGroup;
import io.github.bakedlibs.dough.inventory.handlers.MenuClickHandler;

/**
 * The {@link SlotGroupBuilder} allows you to construct a {@link SlotGroup}
 * easily via the builder pattern.
 * 
 * @author TheBusyBiscuit
 *
 */
public class SlotGroupBuilder {

    protected final char identifier;
    protected final String name;
    protected final Set<Integer> slots = new HashSet<>();

    protected MenuClickHandler clickHandler = null;
    protected ItemStack defaultItem = null;
    protected boolean interactable = false;

    /**
     * This creates a new {@link SlotGroupBuilder} of the given name and char id.
     * You can construct the corresponding {@link SlotGroup} using {@link #build()}.
     * 
     * @param identifier
     *            The unique {@link Character} id for this {@link SlotGroup}
     * @param name
     *            A unique name for this {@link SlotGroup}
     */
    @ParametersAreNonnullByDefault
    public SlotGroupBuilder(char identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    /**
     * This simply returns the name of this {@link SlotGroup}.
     * 
     * @return The name of this {@link SlotGroup}
     */
    public @Nonnull String name() {
        return name;
    }

    /**
     * This marks this {@link SlotGroup} as "interactable" or "non-interactable".
     * Interactable {@link SlotGroup}s allow the {@link Player} to take items from this
     * {@link SlotGroup} or place items into those slots.
     * <p>
     * Non-interactable {@link SlotGroup}s will cancel any {@link InventoryClickEvent} within
     * their bounds.
     * 
     * @param interactable
     *            Whether this {@link SlotGroup} is interactable
     * 
     * @return The {@link SlotGroupBuilder} instance
     */
    public @Nonnull SlotGroupBuilder interactable(boolean interactable) {
        this.interactable = interactable;
        return this;
    }

    /**
     * This method adds the given slot to this {@link SlotGroup}
     * 
     * @param slot
     *            The slot to be added
     * 
     * @return The {@link SlotGroupBuilder} instance
     */
    public @Nonnull SlotGroupBuilder withSlot(int slot) {
        this.slots.add(slot);
        return this;
    }

    /**
     * This method adds all the provided slots to this {@link SlotGroup}
     * 
     * @param slots
     *            All slots that should be part of this {@link SlotGroup}
     * 
     * @return The {@link SlotGroupBuilder} instance
     */
    public @Nonnull SlotGroupBuilder withSlots(int... slots) {
        for (int slot : slots) {
            this.slots.add(slot);
        }

        return this;
    }

    /**
     * This method adds all slots within the given range to this {@link SlotGroup}.
     * Note that both parameters are inclusive.
     * 
     * @param from
     *            The start of this slot range (inclusive)
     * @param to
     *            The end of this slot range (inclusive)
     * 
     * @return The {@link SlotGroupBuilder} instance
     */
    public @Nonnull SlotGroupBuilder withSlotRange(int from, int to) {
        IntStream.range(from, to + 1).forEach(slots::add);
        return this;
    }

    /**
     * This sets the {@link ItemStack} for this {@link SlotGroup}.
     * This {@link ItemStack} will be placed into all slots from this {@link SlotGroup}
     * by default. It can be overridden though.
     * 
     * @param item
     *            The default {@link ItemStack} for this {@link SlotGroup}
     * 
     * @return The {@link SlotGroupBuilder} instance
     */
    public @Nonnull SlotGroupBuilder withDefaultItem(@Nullable ItemStack item) {
        this.defaultItem = item;
        return this;
    }

    /**
     * This adds a {@link MenuClickHandler} to this {@link SlotGroup}.
     * 
     * @param clickHandler
     *            The {@link MenuClickHandler} to fire when a slot from this {@link SlotGroup} was clicked
     * 
     * @return The {@link SlotGroupBuilder} instance
     */
    public @Nonnull SlotGroupBuilder onClick(@Nullable MenuClickHandler clickHandler) {
        this.clickHandler = clickHandler;
        return this;
    }

    /**
     * This creates the final {@link SlotGroup} object from this {@link SlotGroupBuilder}.
     * 
     * @return The resulting {@link SlotGroup}
     */
    public @Nonnull SlotGroup build() {
        Validate.notNull(identifier, "The char identifier may not be null.");
        Validate.notNull(name, "The name may not be null.");
        Validate.notEmpty(slots, "A SlotGroup must have at least one slot.");

        return new SlotGroupImpl(this);
    }

}
