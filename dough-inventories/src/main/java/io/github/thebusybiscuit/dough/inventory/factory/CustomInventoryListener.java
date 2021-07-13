package io.github.thebusybiscuit.dough.inventory.factory;

import javax.annotation.Nonnull;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import io.github.thebusybiscuit.dough.inventory.CustomInventory;
import io.github.thebusybiscuit.dough.inventory.SlotGroup;
import io.github.thebusybiscuit.dough.inventory.payloads.Payloads;

class CustomInventoryListener implements Listener {

    private final CustomInventoryFactory factory;

    CustomInventoryListener(@Nonnull CustomInventoryFactory factory) {
        this.factory = factory;
    }

    public @Nonnull CustomInventoryFactory getInventoryFactory() {
        return factory;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();

        if (holder instanceof CustomInventory) {
            CustomInventory inv = (CustomInventory) holder;

            if (!inv.getFactory().equals(factory)) {
                // Not one of our inventories - abort
                return;
            }

            // TODO: Check if the clicked slot is within that inventory
            SlotGroup slotGroup = inv.getLayout().getGroup(e.getSlot());

            if (!slotGroup.isInteractable()) {
                e.setCancelled(true);
            }

            slotGroup.getClickHandler().onClick(Payloads.create(inv, e));
        }
    }

}
