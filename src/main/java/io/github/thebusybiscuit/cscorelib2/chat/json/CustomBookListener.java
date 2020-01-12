package io.github.thebusybiscuit.cscorelib2.chat.json;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.Plugin;

public class CustomBookListener implements Listener {
	
	private final Set<UUID> players = new HashSet<>();

	public CustomBookListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public Set<UUID> getPlayers() {
		return players;
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (players.contains(e.getPlayer().getUniqueId())) e.setCancelled(true);
	}
	
	@EventHandler
	public void onSwap(PlayerSwapHandItemsEvent e) {
		if (players.contains(e.getPlayer().getUniqueId())) e.setCancelled(true);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (players.contains(e.getWhoClicked().getUniqueId())) e.setCancelled(true);
	}

}
