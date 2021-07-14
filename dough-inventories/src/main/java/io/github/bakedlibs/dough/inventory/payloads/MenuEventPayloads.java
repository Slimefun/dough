package io.github.bakedlibs.dough.inventory.payloads;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import io.github.bakedlibs.dough.inventory.Menu;

/**
 * Utility class for constructing an event payload for menu handlers.
 * 
 * @author TheBusyBiscuit
 *
 */
public class MenuEventPayloads {

    private MenuEventPayloads() {}

    @ParametersAreNonnullByDefault
    public static @Nonnull MenuClickPayload create(Menu inv, InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        int slot = e.getSlot();

        return new MenuClickPayload(inv, player, slot);
    }

}
