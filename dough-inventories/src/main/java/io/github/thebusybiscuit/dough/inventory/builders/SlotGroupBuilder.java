package io.github.thebusybiscuit.dough.inventory.builders;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.dough.inventory.SlotGroup;

public class SlotGroupBuilder {

    protected final char identifier;
    protected final String name;
    protected final Set<Integer> slots = new HashSet<>();

    protected ItemStack defaultItem = new ItemStack(Material.AIR);
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

    public @Nonnull SlotGroupBuilder withDefaultItem(@Nonnull ItemStack item) {
        Validate.notNull(item, "The item cannot be null.");
        this.defaultItem = item;
        return this;
    }

    public @Nonnull SlotGroup build() {
        Validate.notNull(identifier, "The char identifier may not be null.");
        Validate.notNull(name, "The name may not be null.");
        Validate.notNull(defaultItem, "The default item may not be null.");
        Validate.notEmpty(slots, "A SlotGroup must have at least one slot.");

        return new SlotGroupBuilderResult(this);
    }

}
