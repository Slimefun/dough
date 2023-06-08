package io.github.bakedlibs.dough.inventory.payloads;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;

import io.github.bakedlibs.dough.inventory.Menu;

/**
 * An abstract super class for our menu event payloads.
 * 
 * @author TheBusyBiscuit
 *
 */
abstract class AbstractMenuPayload {

    private final Menu menu;
    private final Player player;

    /**
     * This constructs a new {@link AbstractMenuPayload} for the given {@link Menu}
     * and {@link Player}.
     * 
     * @param menu
     *            The {@link Menu}
     * @param player
     *            The {@link Player}
     */
    @ParametersAreNonnullByDefault
    AbstractMenuPayload(Menu menu, Player player) {
        this.menu = menu;
        this.player = player;
    }

    /**
     * This returns the {@link Menu} from this payload.
     * 
     * @return The {@link Menu}
     */
    public @Nonnull Menu getMenu() {
        return menu;
    }

    /**
     * This returns the {@link Player} who triggered this payload.
     * 
     * @return The {@link Player}
     */
    public @Nonnull Player getPlayer() {
        return player;
    }

}
