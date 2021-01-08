package io.github.thebusybiscuit.cscorelib2.chat.json;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
/**
 * 
 * @deprecated Honestly, Kyori-adventure is infinite times better than this, please use that.
 *
 */
@Deprecated
public class CustomBookListener implements Listener {

    protected static final String TRIGGER = "written_book:open - ";

    @Getter
    private final Set<UUID> players = new HashSet<>();

    @Getter
    private final Map<UUID, CustomBookInterface> books = new HashMap<>();

    public CustomBookListener(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        CustomBookInterface book = books.get(e.getPlayer().getUniqueId());

        if (!e.getMessage().startsWith(TRIGGER)) {
            books.remove(e.getPlayer().getUniqueId());
        } else {
            e.setCancelled(true);

            if (book != null) {
                for (Map.Entry<NamespacedKey, Consumer<Player>> entry : book.getClickables().entrySet()) {
                    if (e.getMessage().equals(TRIGGER + entry.getKey().toString())) {
                        entry.getValue().accept(e.getPlayer());
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        players.remove(e.getPlayer().getUniqueId());
        books.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        players.remove(e.getPlayer().getUniqueId());
        books.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        books.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (players.contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        if (players.contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (players.contains(e.getWhoClicked().getUniqueId())) {
            e.setCancelled(true);
        }
    }

}
