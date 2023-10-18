package io.github.bakedlibs.dough.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.apache.commons.lang.Validate;

import io.github.bakedlibs.dough.items.ItemUtils;

public final class InvUtils {

    private InvUtils() {}

    /**
     * Returns whether the Inventory has at least one empty slot
     * 
     * @param inv
     *            The Inventory to check
     * 
     * @return Whether an empty slot exists
     */
    public static boolean hasEmptySlot(@Nonnull Inventory inv) {
        return inv.firstEmpty() != 1;
    }

    /**
     * This checks if the given {@link Inventory} is empty.
     * 
     * @param inv
     *            The {@link Inventory} to check
     * 
     * @return Whether that {@link Inventory} is empty
     */
    public static boolean isEmpty(@Nonnull Inventory inv) {
        // Sadly Inventory#isEmpty() is not available everywhere

        for (ItemStack item : inv) {
            if (item != null && !item.getType().isAir()) {
                return false;
            }
        }

        return true;
    }

    /**
     * This method checks both an ItemStack's and an Inventory's maxStackSize to determine
     * if a given ItemStack can stack with another ItemStack in the given Inventory
     * 
     * @author kii-chan-reloaded
     *
     * @param stack
     *            The ItemStack already in the inventory
     * @param item
     *            The ItemStack that shall be tested for
     * @param inv
     *            The Inventory these items are existing in
     * 
     * @return Whether the maxStackSizes allow for these items to stack
     */
    public static boolean isValidStackSize(@Nonnull ItemStack stack, @Nonnull ItemStack item, @Nonnull Inventory inv) {
        int newStackSize = stack.getAmount() + item.getAmount();
        return newStackSize <= stack.getMaxStackSize() && newStackSize <= inv.getMaxStackSize();
    }

    /**
     * This checks if a given {@link InventoryType} accepts items of the given {@link Material}
     * 
     * @param itemType
     *            The {@link Material} of the {@link ItemStack}
     * @param inventoryType
     *            The {@link InventoryType}
     * 
     * @return Whether the given {@link InventoryType} allows this {@link Material} to be stored within
     */
    public static boolean isItemAllowed(@Nonnull Material itemType, @Nonnull InventoryType inventoryType) {
        switch (inventoryType) {
            case LECTERN:
                // Lecterns only allow written books or writable books
                return itemType == Material.WRITABLE_BOOK || itemType == Material.WRITTEN_BOOK;
            case SHULKER_BOX:
                // Shulker Boxes do not allow Shulker boxes
                return itemType != Material.SHULKER_BOX && !itemType.name().endsWith("_SHULKER_BOX");
            default:
                return true;
        }
    }

    /**
     * This method checks if an Item can fit into the specified slots.
     * Note that this also checks {@link ItemStack#getAmount()}
     * 
     * If you do not specify any Slots, all Slots of the Inventory will be checked.
     *
     * @param inv
     *            The inventory to check
     * @param item
     *            The Item that shall be tested for
     * @param slots
     *            The Slots that shall be iterated over
     * 
     * @return Whether the slots have space for the {@link ItemStack}
     */
    public static boolean fits(@Nonnull Inventory inv, @Nonnull ItemStack item, int... slots) {
        if (!isItemAllowed(item.getType(), inv.getType())) {
            return false;
        }

        if (slots.length == 0) {
            slots = IntStream.range(0, inv.getSize()).toArray();
        }

        for (int slot : slots) {
            ItemStack stack = inv.getItem(slot);

            if (stack == null || stack.getType() == Material.AIR) {
                return true;
            }

            if (isValidStackSize(stack, item, inv) && ItemUtils.canStack(stack, item)) {
                return true;
            }
        }

        return false;
    }

    /**
     * This method works similar to {@link #fits(Inventory, ItemStack, int...)} but allows this check to be done for
     * multiple {@link ItemStack ItemStacks}.
     * 
     * If you do not specify any Slots, all Slots of the Inventory will be checked.
     *
     * @param inv
     *            The inventory to check
     * @param items
     *            The Items that shall be tested for
     * @param slots
     *            The Slots that shall be iterated over
     * 
     * @return Whether the slots have space for the given {@link ItemStack ItemStacks}
     */
    public static boolean fitAll(@Nonnull Inventory inv, @Nonnull ItemStack[] items, int... slots) {
        if (slots.length == 0) {
            slots = IntStream.range(0, inv.getSize()).toArray();
        }

        if (items.length == 0) {
            return true;
        } else if (items.length == 1) {
            return fits(inv, items[0], slots);
        }

        Map<Integer, ItemStack> cache = new HashMap<>();

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            boolean resolved = false;

            for (int slot : slots) {
                ItemStack stack = cache.getOrDefault(slot, inv.getItem(slot));

                if (stack == null || stack.getType() == Material.AIR) {
                    cache.put(slot, items[i]);
                    resolved = true;
                } else if (isValidStackSize(stack, item, inv) && ItemUtils.canStack(stack, item)) {
                    ItemStack clone = stack.clone();
                    clone.setAmount(stack.getAmount() + item.getAmount());
                    cache.put(slot, clone);
                    resolved = true;
                }

                if (resolved) {
                    break;
                }
            }

            if (!resolved) {
                return false;
            }
        }

        return true;
    }

    /**
     * This method removes a given amount of items from a given {@link Inventory}
     * if they pass the given {@link Predicate}
     * 
     * @param inv
     *            The {@link Inventory} from which to remove the item
     * @param amount
     *            The amount of items that should be removed
     * @param replaceConsumables
     *            Whether to replace consumables, e.g. turn potions into glass bottles etc...
     * @param predicate
     *            The Predicate that tests the item
     * @return Whether the operation was successful
     */
    public static boolean removeItem(@Nonnull Inventory inv, int amount, boolean replaceConsumables, @Nonnull Predicate<ItemStack> predicate) {
        int removed = 0;
        for (int slot = 0; slot < inv.getSize(); slot++) {
            ItemStack item = inv.getItem(slot);

            if (item != null && predicate.test(item)) {
                if (item.getAmount() + removed >= amount) {
                    ItemUtils.consumeItem(item, amount - removed, replaceConsumables);
                    return true;
                } else if (item.getAmount() > 0) {
                    removed += item.getAmount();
                    ItemUtils.consumeItem(item, item.getAmount(), replaceConsumables);
                }
            }
        }

        return false;
    }

    /**
     * Fills the borders of an {@link Inventory}
     *
     * @param inv The target {@link Inventory}
     * @param item Item to use for filling
     */
    public static void fillBorder(@Nonnull Inventory inv, @Nonnull ItemStack item) {
        Validate.notNull(inv, "Inventory must not be null");
        Validate.notNull(item, "Item must not be null");

        int size = inv.getSize();
        int rows = (size + 1) / 9;

        // Fill top
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, item);
        }

        // If inventory is only one row, no need for anything else
        if (size > 9) {
            // Fill bottom
            for (int i = size - 9; i < size; i++) {
                inv.setItem(i, item);
            }

            // Fill sides
            for (int i = 2; i <= rows - 1; i++) {
                inv.setItem(i * 9 - 1, item);
                inv.setItem((i - 1) * 9, item);
            }
        }
    }

    /**
     * Fills a row in an {@link Inventory}
     *
     * @param inventory The target {@link Inventory}
     * @param rowIndex Index of the row to fill (1 - 6)
     * @param item The {@link ItemStack} to use for filling
     * @param onlyEmpty If only empty slots should be filled
     */
    public static void fillRow(@Nonnull Inventory inventory, int rowIndex, @Nonnull ItemStack item, boolean onlyEmpty) {
        Validate.notNull(inventory, "Inventory must not be null");
        Validate.notNull(item, "Item must not be null");

        int x = rowIndex * 9;
        for (int i = 0; i < 9; i++) {
            int slot = x + i;

            if (!onlyEmpty) {
                inventory.setItem(slot, item);
            } else {
                ItemStack slotItem = inventory.getItem(i);
                if (slotItem == null || slotItem.getType().isAir()) {
                    inventory.setItem(slot, item);
                }
            }
        }
    }

    /**
     * Fills an inventory
     *
     * @param inventory The target {@link Inventory}
     * @param item The {@link ItemStack} used for filling empty slots
     * @param onlyEmpty If only empty slots should be filled
     */
    public static void fill(@Nonnull Inventory inventory, @Nonnull ItemStack item, boolean onlyEmpty) {
        Validate.notNull(inventory, "Inventory must not be null");
        Validate.notNull(item, "Item must not be null");

        for (int i = 0; i < inventory.getSize(); i++) {
            if (!onlyEmpty) {
                inventory.setItem(i, item);
            } else {
                ItemStack slotItem = inventory.getItem(i);
                if (slotItem == null || slotItem.getType().isAir()) {
                    inventory.setItem(i, item);
                }
            }
        }
    }

}