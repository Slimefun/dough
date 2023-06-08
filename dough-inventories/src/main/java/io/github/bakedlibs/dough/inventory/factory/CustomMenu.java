package io.github.bakedlibs.dough.inventory.factory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.inventory.Menu;
import io.github.bakedlibs.dough.inventory.MenuLayout;
import io.github.bakedlibs.dough.inventory.SlotGroup;

public class CustomMenu implements Menu {

    private final MenuFactory factory;
    private final MenuLayout layout;
    private final String title;
    private Inventory inventory;

    @ParametersAreNonnullByDefault
    protected CustomMenu(MenuFactory factory, MenuLayout layout) {
        Validate.notNull(factory, "The factory cannot be null.");
        Validate.notNull(layout, "The layout cannot be null.");

        this.factory = factory;
        this.layout = layout;
        this.title = layout.getTitle();
    }

    final void setInventory(@Nonnull Inventory inventory) {
        Validate.notNull(inventory, "The Inventory must not be null.");
        Validate.isTrue(inventory.getSize() == layout.getSize(), "The inventory has a different size.");
        Validate.isTrue(inventory.getHolder() == this, "The Inventory does not seem to belong here. Holder: " + inventory.getHolder());

        this.inventory = inventory;
    }

    private void validate() {
        if (inventory == null) {
            throw new UnsupportedOperationException("No inventory found! Menus must be created using MenuFactory#createMenu(...)");
        }
    }

    @Override
    public final @Nonnull MenuFactory getFactory() {
        validate();

        return factory;
    }

    @Override
    public final @Nonnull Inventory getInventory() {
        validate();

        return inventory;
    }

    @Override
    public final @Nonnull MenuLayout getLayout() {
        validate();

        return layout;
    }

    @Override
    public String getTitle() {
        validate();

        return title;
    }

    @Override
    public void setAll(SlotGroup group, ItemStack item) {
        validate();

        if (group.size() == 1) {
            // Little optimization, we don't need to create an Iterator for this
            setItem(group.getSlots()[0], item);
        } else {
            for (int slot : group) {
                setItem(slot, item);
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public @Nullable ItemStack addItem(SlotGroup group, ItemStack item) {
        Validate.notNull(group, "The Slot group cannot be null!");
        Validate.notNull(item, "The Item cannot be null!");
        validate();

        for (int slot : group) {
            ItemStack itemInSlot = getItem(slot);

            if (itemInSlot == null || itemInSlot.getType().isAir()) {
                setItem(slot, item);
                return null;
            } else {
                int currentAmount = itemInSlot.getAmount();
                int maxStackSize = itemInSlot.getType().getMaxStackSize();

                if (currentAmount < maxStackSize && itemInSlot.isSimilar(item)) {
                    int amount = currentAmount + item.getAmount();

                    if (amount > maxStackSize) {
                        item.setAmount(amount - maxStackSize);
                        itemInSlot.setAmount(maxStackSize);
                    } else {
                        itemInSlot.setAmount(Math.min(amount, maxStackSize));
                        return null;
                    }
                }
            }
        }

        return item;
    }

    @Override
    public void setItem(int slot, @Nullable ItemStack item) {
        validate();

        inventory.setItem(slot, item);
    }

    @Override
    public @Nullable ItemStack getItem(int slot) {
        validate();

        return inventory.getItem(slot);
    }

    @Override
    public @Nonnull String toString() {
        return getClass().getSimpleName() + " [size: " + layout.getSize() + ", title=" + title + "]";
    }

}
