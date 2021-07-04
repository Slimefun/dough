package io.github.thebusybiscuit.dough.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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
    public void onDisable(PluginDisableEvent event) {
        if (event.getPlugin() == plugin) {
            ChestMenu.listener = null;
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        HumanEntity entity = event.getWhoClicked();
        if (!menus.containsKey(entity.getUniqueId())) {
            return;
        }
        Inventory inventory = entity.getInventory();
        ChestMenu menu = menus.get(entity.getUniqueId());
        ItemStack currentItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();

        if (menu.preventsItems(currentItem) || menu.preventsItems(cursorItem) || (event.getHotbarButton() != -1 && menu.preventsItems(inventory.getItem(event.getHotbarButton())))) {
            event.setCancelled(true);
            return;
        }
        MenuClickHandler handler = null;
        Inventory eventInventory = event.getInventory();
        if (event.getRawSlot() < eventInventory.getSize()) {
            handler = menu.getMenuClickHandler(event.getSlot());

            if (handler == null) {
                event.setCancelled(!menu.areEmptySlotsClickable() && (currentItem == null || currentItem.getType().isAir()));
            } else {
                event.setCancelled(!handler.onClick((Player) entity, event.getSlot(), currentItem, event.getCursor(), getClickAction(event)));
            }
        } else {
            event.setCancelled(!menu.getPlayerInventoryClickHandler().onClick((Player) entity, event.getSlot(), currentItem, event.getCursor(), getClickAction(event)));
        }

        if (handler == null) {
            boolean clicked = currentItem != null && currentItem.getType().isAir();
            boolean cursor = cursorItem != null && cursorItem.getType().isAir();

            if (clicked || cursor)
                menu.markDirty();
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        ChestMenu menu = menus.get(event.getPlayer().getUniqueId());

        if (menu != null) {
            menu.onClose((Player) event.getPlayer());
            menus.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        menus.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        menus.remove(event.getPlayer().getUniqueId());
    }

    private ClickAction getClickAction(InventoryClickEvent event) {
        if (event.isRightClick()) {
            return event.isShiftClick() ? ClickAction.SHIFT_RIGHT_CLICK : ClickAction.RIGHT_CLICK;
        } else {
            return event.isShiftClick() ? ClickAction.SHIFT_LEFT_CLICK : ClickAction.LEFT_CLICK;
        }
    }

}
