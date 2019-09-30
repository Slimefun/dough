package io.github.thebusybiscuit.cscorelib2.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

class ChatInputListener implements Listener {
	
	private Plugin plugin;
	protected Map<UUID, IChatInput> handlers;
	
	protected ChatInputListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
		handlers = new HashMap<>();
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onChat(final AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		IChatInput handler = handlers.get(p.getUniqueId());
		String msg = e.getMessage().replace(ChatColor.COLOR_CHAR, '&');
		
		if (handler != null && handler.test(msg)) {
			handlers.remove(p.getUniqueId());
			plugin.getServer().getScheduler().runTask(plugin, () -> handler.onChat(p, msg));
			
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onComamnd(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		IChatInput handler = handlers.get(p.getUniqueId());
		if (handler != null && handler.test(e.getMessage())) {
			handlers.remove(p.getUniqueId());
			handler.onChat(p, e.getMessage());
			
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		handlers.remove(e.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		handlers.remove(e.getPlayer().getUniqueId());
	}

}
