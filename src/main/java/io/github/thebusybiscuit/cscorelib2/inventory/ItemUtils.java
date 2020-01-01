package io.github.thebusybiscuit.cscorelib2.inventory;

import java.lang.reflect.Method;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;
import lombok.NonNull;

public final class ItemUtils {
	
	private ItemUtils() {}
	
	private static Method copy;
	private static Method getName;
	private static Method toString;
	
	static {
		try {
			copy = ReflectionUtils.getOBCClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class);
			getName = ReflectionUtils.getMethod(ReflectionUtils.getNMSClass("ItemStack"), "getName");
			toString = ReflectionUtils.getMethod(ReflectionUtils.getNMSClass("IChatBaseComponent"), "getString");
		}
		catch(Exception x) {
			System.err.println("Perhaps you forgot to shade CS-CoreLib's \"reflection\" package?");
			x.printStackTrace();
		}
	}
	
	/**
	 * This method returns a human-readable version of this item's name.
	 * If the specified {@link ItemStack} has a Custom Display Name, it will return that.
	 * Otherwise it will return the english name of it's {@link Material}
	 * 
	 * @param item	The Item to format
	 * @return		The formatted Item Name
	 */
	public static String getItemName(ItemStack item) {
		if (item == null) return "null";
		
		if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
			return item.getItemMeta().getDisplayName();
		}
		
		try {
			Object instance = copy.invoke(null, item);
			return (String) toString.invoke(getName.invoke(instance));
		} catch (Exception e) {
			e.printStackTrace();
			return "ERROR";
		}
	}
	
	/**
	 * This method compares two instances of {@link ItemStack} and checks
	 * whether their {@link Material} and {@link ItemMeta} match.
	 * 
	 * @param a	{@link ItemStack} One
	 * @param b {@link ItemStack} Two
	 * @return	Whether the two instances of {@link ItemStack} are similiar and can be stacked.
	 */
	public static boolean canStack(ItemStack a, ItemStack b) {
		if (a == null || b == null) return false;
		
		if (a.getType() != b.getType()) return false;
		if (a.hasItemMeta() != b.hasItemMeta()) return false;
		
		if (a.hasItemMeta()) {
			ItemMeta aMeta = a.getItemMeta();
			ItemMeta bMeta = b.getItemMeta();
			
			// Item Damage
			if (aMeta instanceof Damageable != bMeta instanceof Damageable) return false;
			if (aMeta instanceof Damageable) {
				if (((Damageable) aMeta).getDamage() != ((Damageable) bMeta).getDamage()) return false;
			}
			
			// Leather Armor Color
			if (aMeta instanceof LeatherArmorMeta != bMeta instanceof LeatherArmorMeta) return false;
			if (aMeta instanceof LeatherArmorMeta) {
				if (!((LeatherArmorMeta) aMeta).getColor().equals(((LeatherArmorMeta) bMeta).getColor())) return false;
			}
			
			// Custom Model Data
			if (aMeta.hasCustomModelData() != bMeta.hasCustomModelData()) return false;
			if (aMeta.hasCustomModelData()) {
				if (aMeta.getCustomModelData() != bMeta.getCustomModelData()) return false;
			}
			
			// Enchantments
			if (!aMeta.getEnchants().equals(bMeta.getEnchants())) return false;
			
			// Display Name
			if (aMeta.hasDisplayName() != bMeta.hasDisplayName()) return false;
			if (aMeta.hasDisplayName()) {
				if (!aMeta.getDisplayName().equals(bMeta.getDisplayName())) return false;
			}

			// Lore
			if (aMeta.hasLore() != bMeta.hasLore()) return false;
			if (aMeta.hasLore()) {
				if (aMeta.getLore().size() != bMeta.getLore().size()) return false;
				
				for (int i = 0; i < aMeta.getLore().size(); i++) {
					if (!aMeta.getLore().get(i).equals(bMeta.getLore().get(i))) return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * This method damages the specified Item by 1.
	 * If ignoredEnchantments is set to false, it will factor in the "Unbreaking" Enchantment.
	 * 
	 * @param item					The Item to damage
	 * @param ignoreEnchantments	Whether the Unbreaking Enchantment should be ignored
	 */
	public static void damageItem(@NonNull ItemStack item, boolean ignoreEnchantments) {
		damageItem(item, 1, ignoreEnchantments);
	}
	
	/**
	 * This method damages the specified Item by the given amount.
	 * If ignoredEnchantments is set to false, it will factor in the "Unbreaking" Enchantment.
	 * 
	 * @param item					The Item to damage
	 * @param damage				The amount of damage to apply
	 * @param ignoreEnchantments	Whether the Unbreaking Enchantment should be ignored
	 */
	public static void damageItem(@NonNull ItemStack item, int damage, boolean ignoreEnchantments) {
		if (item.getType() != Material.AIR && item.getAmount() > 0) {
			int remove = damage;
			
			if (!ignoreEnchantments && item.getEnchantments().containsKey(Enchantment.DURABILITY)) {
				int level = item.getEnchantmentLevel(Enchantment.DURABILITY);
				
				for (int i = 0; i < damage; i++) {
					if (Math.random() * 100 <= (60 + Math.floorDiv(40, (level + 1)))) {
						remove--;
					}
				}
			}
			
			ItemMeta meta = item.getItemMeta();
			Damageable damageable = (Damageable) meta;
			
			if (damageable.getDamage() + remove > item.getType().getMaxDurability()) {
				item.setAmount(0);
			}
			else {
				damageable.setDamage(damageable.getDamage() + remove);
				item.setItemMeta(meta);
			}
		}
	}
	
	/**
	 * This Method will consume the Item in the specified slot.
	 * See {@link ItemUtils#consumeItem(ItemStack, int, boolean)} for further details.
	 * 
	 * @param item 					The Item to consume
	 * @param replaceConsumables 	Whether Consumable Items should be replaced with their "empty" version, see {@link ItemUtils#consumeItem(ItemStack, int, boolean)}
	 */
	public static void consumeItem(@NonNull ItemStack item, boolean replaceConsumables) {
		consumeItem(item, 1, replaceConsumables);
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
	 * {@code Buckets -> new ItemStack(Material.BUCKET)}
	 * {@code Potions -> new ItemStack(Material.GLASS_BOTTLE)}
	 * 
	 * @param item					The Item to consume
	 * @param amount				How many Items should be removed
	 * @param replaceConsumables	Whether Items should be replaced with their "empty" version
	 */
	public static void consumeItem(@NonNull ItemStack item, int amount, boolean replaceConsumables) {
		if (item.getType() != Material.AIR && item.getAmount() > 0) {
			if (MaterialCollections.getAllFilledBuckets().contains(item.getType()) && replaceConsumables) {
				item.setType(Material.BUCKET);
				item.setAmount(1);
			}
			else if (item.getType() == Material.POTION && replaceConsumables) {
				item.setType(Material.GLASS_BOTTLE);
				item.setAmount(1);
			}
			else if (item.getAmount() <= amount) {
				item.setAmount(0);
			}
			else {
				item.setAmount(item.getAmount() - amount);
			}
		}
	}

}
