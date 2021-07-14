package io.github.bakedlibs.dough.protection;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

/**
 * This Class represents a Protection Module that is used as a bridge
 * to for your Plugin's API.
 * 
 * @author TheBusyBiscuit
 *
 */
public interface ProtectionModule {

    /**
     * Use this method to load instances of your API or other utilites you need
     */
    void load();

    /**
     * This returns the {@link Plugin} for this {@link ProtectionModule}.
     * 
     * @return The associated {@link Plugin}
     */
    Plugin getPlugin();

    /**
     * This returns the name of the Protection {@link Plugin} you are integrating.
     * It must be unique.
     * 
     * @return The Name of your {@link Plugin}
     */
    default String getName() {
        return getPlugin().getName();
    }

    /**
     * This returns the version of the {@link Plugin} this represents.
     * 
     * @return The version of your {@link Plugin}
     */
    default String getVersion() {
        return getPlugin().getDescription().getVersion();
    }

    /**
     * This method implements the functionality of this module.
     * Use it to allow or deny an Action based on the rules of your Protection {@link Plugin}
     * 
     * @param p
     *            The Player that is being queried, can be offline
     * @param l
     *            The {@link Location} of the event that is happening
     * @param action
     *            The {@link Interaction} that is taking place.
     * @return Whether the action was allowed by your {@link Plugin}
     */
    boolean hasPermission(OfflinePlayer p, Location l, Interaction action);

}
