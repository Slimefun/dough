package io.github.thebusybiscuit.dough.inventory.builders;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.dough.inventory.SlotGroup;
import io.github.thebusybiscuit.dough.inventory.handlers.CustomInventoryClickHandler;

public class SlotGroupBuilder {

    protected final char identifier;
    protected final String name;
    protected final Set<Integer> slots = new HashSet<>();

    protected CustomInventoryClickHandler clickHandler = null;
    protected ItemStack defaultItem = null;
    protected boolean interactable = false;

    @ParametersAreNonnullByDefault
    public SlotGroupBuilder(char identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public @Nonnull SlotGroupBuilder interactable(boolean interactable) {
        this.interactable = interactable;
        return this;
    }

    public @Nonnull SlotGroupBuilder withSlot(int slot) {
        this.slots.add(slot);
        return this;
    }

    public @Nonnull SlotGroupBuilder withSlots(int... slots) {
        for (int slot : slots) {
            this.slots.add(slot);
        }
        return this;
    }

    public @Nonnull SlotGroupBuilder withDefaultItem(@Nullable ItemStack item) {
        this.defaultItem = item;
        return this;
    }

    public @Nonnull SlotGroupBuilder onClick(@Nullable CustomInventoryClickHandler clickHandler) {
        this.clickHandler = clickHandler;
        return this;
    }

    public @Nonnull SlotGroup build() {
        Validate.notNull(identifier, "The char identifier may not be null.");
        Validate.notNull(name, "The name may not be null.");
        Validate.notEmpty(slots, "A SlotGroup must have at least one slot.");

        return new SlotGroupBuilderResult(this);
    }

}
