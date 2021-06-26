package io.github.thebusybiscuit.dough.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface MenuClickHandler {

    public boolean onClick(Player p, int slot, ItemStack item, ItemStack cursor, ClickAction action);
}
