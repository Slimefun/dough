package io.github.bakedlibs.dough.inventory.factory;

import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import io.github.bakedlibs.dough.inventory.Menu;
import io.github.bakedlibs.dough.inventory.SlotGroup;
import io.github.bakedlibs.dough.inventory.handlers.MenuClickHandler;
import io.github.bakedlibs.dough.inventory.payloads.MenuPayloads;

/**
 * The {@link MenuListener} is responsible for handling any
 * {@link Event} related to our {@link Menu}s.
 * <p>
 * It is registered by a {@link MenuFactory} and there should only
 * be one {@link MenuListener} per {@link MenuFactory}.
 * 
 * @author TheBusyBiscuit
 *
 */
class MenuListener implements Listener {

    /**
     * Our {@link MenuFactory} instance.
     */
    private final MenuFactory factory;

    /**
     * This constructs a new {@link MenuListener} for the given
     * {@link MenuFactory}.
     * 
     * @param factory
     *            Our {@link MenuFactory} instance
     */
    MenuListener(@Nonnull MenuFactory factory) {
        this.factory = factory;
    }

    /**
     * This returns the {@link MenuFactory} which instantiated and
     * registered this {@link MenuListener}.
     * 
     * @return The {@link MenuFactory}
     */
    public @Nonnull MenuFactory getFactory() {
        return factory;
    }

    /**
     * Here we listen for the {@link InventoryClickEvent}.
     * This event is fired whenever a {@link Player} clicks a slot
     * in an {@link Inventory}. We only care for events which happen
     * within our {@link Menu} though.
     * 
     * @param e
     *            The {@link InventoryClickEvent} which was fired
     */
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();

        // Check if the Inventory is a Menu
        if (holder instanceof Menu) {
            Menu inv = (Menu) holder;

            try {
                // Check if this was created by our factory and the clicked slot is within the upper inventory
                if (inv.getFactory().equals(factory) && e.getRawSlot() < e.getInventory().getSize()) {
                    SlotGroup slotGroup = inv.getLayout().getGroup(e.getSlot());

                    // Cancel the interaction if that slot is not interactable
                    if (!slotGroup.isInteractable()) {
                        e.setCancelled(true);
                    }

                    // Fire the click handler
                    MenuClickHandler clickHandler = slotGroup.getClickHandler();

                    if (clickHandler != null) {
                        clickHandler.onClick(MenuPayloads.create(inv, e));
                    }
                }
            } catch (Exception | LinkageError x) {
                factory.getLogger().log(Level.SEVERE, x, () -> "Could not pass click event for " + inv + " (slot: " + e.getSlot() + ", player:" + e.getWhoClicked().getName() + ")");
            }
        }
    }

}
