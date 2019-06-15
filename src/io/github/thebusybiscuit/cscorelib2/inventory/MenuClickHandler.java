package io.github.thebusybiscuit.cscorelib2.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface MenuClickHandler {
	
	/**
	 * This field represents a static and final instance that prevents clicks.
	 */
	public static final MenuClickHandler NOTHING = (p, slot, item, cursor, action) -> false;
	
	public boolean onClick(Player p, int slot, ItemStack item, ItemStack cursor, ClickAction action);
}