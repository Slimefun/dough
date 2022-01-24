package io.github.bakedlibs.dough.protection.modules;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;
import me.william278.husktowns.HuskTownsAPI;
import me.william278.husktowns.listener.ActionType;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Protection handling module for HuskTowns
 *
 * @author William278
 */
public class HuskTownsProtectionModule implements ProtectionModule {

    private HuskTownsAPI huskTownsAPI;
    private final Plugin plugin;

    public HuskTownsProtectionModule(@Nonnull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        huskTownsAPI = HuskTownsAPI.getInstance();
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, Interaction action) {
        // Get UUID of online/offline player
        final UUID playerUUID = p.getUniqueId();

        // Convert the dough interaction to HuskTowns' ActionType and check via the API
        return huskTownsAPI.canPerformAction(playerUUID, l, getHuskTownsAction(action));
    }

    /**
     * Returns the corresponding HuskTowns {@link ActionType} from the dough {@link Interaction}
     *
     * @param doughAction
     *            The dough {@link Interaction}
     * 
     * @return The corresponding HuskTowns {@link ActionType}
     */
    public @Nonnull ActionType getHuskTownsAction(@Nonnull Interaction doughAction) {
        switch (doughAction) {
            case BREAK_BLOCK:
                return ActionType.BREAK_BLOCK;
            case PLACE_BLOCK:
                return ActionType.PLACE_BLOCK;
            case ATTACK_ENTITY:
                return ActionType.PVE;
            case ATTACK_PLAYER:
                return ActionType.PVP;
            case INTERACT_BLOCK:
                return ActionType.INTERACT_BLOCKS;
            default:
                return ActionType.ENTITY_INTERACTION;
        }
    }
}
