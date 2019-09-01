package io.github.thebusybiscuit.cscorelib2.inventory;

import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class InvUtils {
	
	private InvUtils() {}
	
	/**
	 * Returns whether the Inventory has at least one empty slot
	 * 
	 * @param inv	The Inventory to check
	 * @return		Whether an empty slot exists
	 */
	public static boolean hasEmptySlot(Inventory inv) {
		return inv.firstEmpty() != 1;
	}
	
	/**
	 * This Method will consume the Item in the specified slot.
	 * See {@link InvUtils#consumeItem(Inventory, int, int, boolean)} for further details.
	 * 
	 * @param inv					The Inventory to check
	 * @param slot 					The Slot in which the Item should be consumed
	 * @param replaceConsumables 	Whether Consumable Items should be replaced with their "empty" version, see {@link InvUtils#consumeItem(Inventory, int, int, boolean)}
	 */
	public static void consumeItem(Inventory inv, int slot, boolean replaceConsumables) {
		consumeItem(inv, slot, 1, replaceConsumables);
	}
	
	/**
	 * This Method consumes a specified amount of items from the
	 * specified slot.
	 * 
	 * The items will be removed from the slot, if the slot does not hold enough items,
	 * it will be replaced with null.
	 * Note that this does not check whether there are enough Items present,
	 * if you specify a bigger amount than present, it will simply set the Item to null.
	 * 
	 * If replaceConsumables is true, the following things will not be replaced with 'null':
	 * Buckets -> new ItemStack(Material.BUCKET)
	 * Potions -> new ItemStack(Material.GLASS_BOTTLE)
	 * 
	 * @param inv					The Inventory to check
	 * @param slot					The Slot in which to remove the Item
	 * @param amount				How many Items should be removed
	 * @param replaceConsumables	Whether Items should be replaced with their "empty" version
	 */
	public static void consumeItem(Inventory inv, int slot, int amount, boolean replaceConsumables) {
		ItemStack item = inv.getItem(slot);
		
		if (item != null && !item.getType().equals(Material.AIR)) {
			if (item.getType().name().endsWith("_BUCKET") && replaceConsumables) {
				inv.setItem(slot, new ItemStack(Material.BUCKET));
			}
			else if (item.getType().equals(Material.POTION) && replaceConsumables) {
				inv.setItem(slot, new ItemStack(Material.GLASS_BOTTLE));
			}
			else {
				if (item.getAmount() <= amount) item.setAmount(0);
				else item.setAmount(item.getAmount() - amount);
				
				inv.setItem(slot, item);
			}
		}
	}
	
	/**
	 * This method checks if an Item can fit into the specified slots.
	 * Note that this also checks {@link ItemStack#getAmount()}
	 * 
	 * If you do not specify any Slots, all Slots of the Inventory will be checked.
	 * 
	 * @param item		The Item that shall be tested for
	 * @param slots		The Slots that shall be iterated over
	 * @return			Whether the slots have space for the {@link ItemStack}
	 */
	public static boolean fits(Inventory inv, ItemStack item, int... slots) {
		if (slots.length == 0) slots = IntStream.range(0, inv.getSize()).toArray();
		
		for (int slot: slots) {
			ItemStack stack = inv.getItem(slot);
			
			if (stack == null || stack.getType().equals(Material.AIR)) return true;
			else if (stack.getAmount() + item.getAmount() <= stack.getMaxStackSize() && ItemUtils.canStack(stack, item)) {
				return true;
			}
		}
		
		return false;
	}

}
