package io.github.thebusybiscuit.dough.inventory.factory;

import javax.annotation.Nonnull;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import io.github.thebusybiscuit.dough.inventory.Menu;
import io.github.thebusybiscuit.dough.inventory.SlotGroup;
import io.github.thebusybiscuit.dough.inventory.payloads.MenuEventPayloads;

class MenuListener implements Listener {

    private final MenuFactory factory;

    MenuListener(@Nonnull MenuFactory factory) {
        this.factory = factory;
    }

    public @Nonnull MenuFactory getInventoryFactory() {
        return factory;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();

        if (holder instanceof Menu) {
            Menu inv = (Menu) holder;

            if (!inv.getFactory().equals(factory)) {
                // Not one of our inventories - abort
                return;
            }

            // TODO: Check if the clicked slot is within that inventory
            SlotGroup slotGroup = inv.getLayout().getGroup(e.getSlot());

            if (!slotGroup.isInteractable()) {
                e.setCancelled(true);
            }

            slotGroup.getClickHandler().onClick(MenuEventPayloads.create(inv, e));
        }
    }

}
