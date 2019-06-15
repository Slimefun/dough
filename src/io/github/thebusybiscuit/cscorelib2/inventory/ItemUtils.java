package io.github.thebusybiscuit.cscorelib2.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public final class ItemUtils {
	
	private ItemUtils() {}
	
	/**
	 * This method compares two instances of {@link ItemStack} and checks
	 * whether their {@link Material} and {@link ItemMeta} match.
	 * 
	 * @param a	{@link ItemStack} One
	 * @param b {@link ItemStack} Two
	 * @return
	 */
	public static boolean canStack(ItemStack a, ItemStack b) {
		if (a == null || b == null) return false;
		
		if (!a.getType().equals(b.getType())) return false;
		if (a.hasItemMeta() != b.hasItemMeta()) return false;
		
		if (a.hasItemMeta()) {
			ItemMeta aMeta = a.getItemMeta(), bMeta = b.getItemMeta();
			
			if (aMeta instanceof Damageable != bMeta instanceof Damageable) return false;
			if (aMeta instanceof Damageable) {
				if (((Damageable) aMeta).getDamage() != ((Damageable) bMeta).getDamage()) return false;
			}

			if (aMeta instanceof LeatherArmorMeta != bMeta instanceof LeatherArmorMeta) return false;
			if (aMeta instanceof LeatherArmorMeta) {
				if (!((LeatherArmorMeta) aMeta).getColor().equals(((LeatherArmorMeta) bMeta).getColor())) return false;
			}
			
			if (aMeta.getCustomModelData() != bMeta.getCustomModelData()) return false;
			if (!aMeta.getEnchants().equals(bMeta.getEnchants())) return false;
			
			if (aMeta.hasDisplayName() != bMeta.hasDisplayName()) return false;
			if (aMeta.hasDisplayName()) {
				if (!aMeta.getDisplayName().equals(bMeta.getDisplayName())) return false;
			}

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

}
