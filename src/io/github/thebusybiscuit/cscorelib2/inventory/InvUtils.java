package io.github.thebusybiscuit.cscorelib2.inventory;

import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lombok.NonNull;

public final class InvUtils {
	
	/**
	 * This field represents a static and final instance of {@link MenuClickHandler} that prevents clicks.
	 */
	public static final MenuClickHandler EMPTY_CLICK = (p, slot, item, cursor, action) -> false;
	
	private InvUtils() {}
	
	/**
	 * Returns whether the Inventory has at least one empty slot
	 * 
	 * @param inv	The Inventory to check
	 * @return		Whether an empty slot exists
	 */
	public static boolean hasEmptySlot(@NonNull Inventory inv) {
		return inv.firstEmpty() != 1;
	}
	
	/**
	 * This method checks if an Item can fit into the specified slots.
	 * Note that this also checks {@link ItemStack#getAmount()}
	 * 
	 * If you do not specify any Slots, all Slots of the Inventory will be checked.
	 *
	 * @param inv		The inventory to check
	 * @param item		The Item that shall be tested for
	 * @param slots		The Slots that shall be iterated over
	 * @return			Whether the slots have space for the {@link ItemStack}
	 */
	public static boolean fits(@NonNull Inventory inv, @NonNull ItemStack item, int... slots) {
		if (slots.length == 0) slots = IntStream.range(0, inv.getSize()).toArray();
		
		for (int slot: slots) {
			ItemStack stack = inv.getItem(slot);
			
			if (stack == null || stack.getType() == Material.AIR) return true;
			else if (stack.getAmount() + item.getAmount() <= stack.getMaxStackSize() && ItemUtils.canStack(stack, item)) {
				return true;
			}
		}
		
		return false;
	}

}
