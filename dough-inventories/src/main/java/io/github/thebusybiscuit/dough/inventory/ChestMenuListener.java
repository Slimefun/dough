package io.github.thebusybiscuit.dough.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

class ChestMenuListener implements Listener {

    private final Plugin plugin;
    protected final Map<UUID, ChestMenu> menus;

    protected ChestMenuListener(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.plugin = plugin;
        menus = new HashMap<>();
    }

    @EventHandler
    public void onDisable(PluginDisableEvent e) {
        if (e.getPlugin() == plugin) {
            ChestMenu.listener = null;
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (menus.containsKey(e.getWhoClicked().getUniqueId())) {
            ChestMenu menu = menus.get(e.getWhoClicked().getUniqueId());

            if (menu.preventsItems(e.getCurrentItem()) || menu.preventsItems(e.getCursor()) || (e.getHotbarButton() != -1 && menu.preventsItems(e.getWhoClicked().getInventory().getItem(e.getHotbarButton())))) {
                e.setCancelled(true);
            } else {
                MenuClickHandler handler = null;

                if (e.getRawSlot() < e.getInventory().getSize()) {
                    handler = menu.getMenuClickHandler(e.getSlot());

                    if (handler == null) {
                        e.setCancelled(!menu.areEmptySlotsClickable() && (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR));
                    } else {
                        e.setCancelled(!handler.onClick((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getCursor(), getClickAction(e)));
                    }
                } else {
                    e.setCancelled(!menu.getPlayerInventoryClickHandler().onClick((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getCursor(), getClickAction(e)));
                }

                if (handler == null) {
                    boolean clicked = !(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR);
                    boolean cursor = !(e.getCursor() == null || e.getCursor().getType() == Material.AIR);

                    if (clicked || cursor)
                        menu.markDirty();
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        ChestMenu menu = menus.get(e.getPlayer().getUniqueId());

        if (menu != null) {
            menu.onClose((Player) e.getPlayer());
            menus.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        menus.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        menus.remove(e.getPlayer().getUniqueId());
    }

    private ClickAction getClickAction(InventoryClickEvent e) {
        if (e.isRightClick()) {
            return e.isShiftClick() ? ClickAction.SHIFT_RIGHT_CLICK : ClickAction.RIGHT_CLICK;
        } else {
            return e.isShiftClick() ? ClickAction.SHIFT_LEFT_CLICK : ClickAction.LEFT_CLICK;
        }
    }

}
