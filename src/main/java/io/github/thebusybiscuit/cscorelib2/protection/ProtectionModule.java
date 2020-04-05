package io.github.thebusybiscuit.cscorelib2.protection;

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
     * Please return the Name of the Protection {@link Plugin} you are integrating.
     * It must be unique.
     * 
     * @return The Name of your {@link Plugin}
     */
    String getName();

    /**
     * This method implements the functionality of this module.
     * Use it to allow or deny an Action based on the rules of your Protection {@link Plugin}
     * 
     * @param p
     *            The Player that is being queried, can be offline
     * @param l
     *            The {@link Location} of the event that is happening
     * @param action
     *            The {@link ProtectableAction} that is taking place.
     * @return Whether the action was allowed by your {@link Plugin}
     */
    boolean hasPermission(OfflinePlayer p, Location l, ProtectableAction action);

}
