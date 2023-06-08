package io.github.bakedlibs.dough.inventory.payloads;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import io.github.bakedlibs.dough.inventory.Menu;
import io.github.bakedlibs.dough.inventory.handlers.MenuClickHandler;

/**
 * Utility class for constructing an event payload for menu handlers.
 * 
 * @author TheBusyBiscuit
 *
 */
public class MenuPayloads {

    private MenuPayloads() {}

    /**
     * This creates our payload for an {@link InventoryClickEvent}.
     * 
     * @param menu
     *            The {@link Menu} involved in this event.
     * @param e
     *            The {@link InventoryClickEvent}
     * 
     * @return A {@link MenuClickPayload} to pass onto the {@link MenuClickHandler}.
     */
    @ParametersAreNonnullByDefault
    public static @Nonnull MenuClickPayload create(Menu menu, InventoryClickEvent e) {
        Validate.notNull(menu, "The menu cannot be null");
        Validate.notNull(e, "Cannot create a payload for an event that is null");

        Player player = (Player) e.getWhoClicked();
        int slot = e.getSlot();

        return new MenuClickPayload(menu, player, slot);
    }

}
