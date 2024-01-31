package io.github.bakedlibs.dough.protection.modules;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;
import net.william278.husktowns.api.BukkitHuskTownsAPI;
import net.william278.husktowns.libraries.cloplib.operation.Operation;
import net.william278.husktowns.libraries.cloplib.operation.OperationType;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;

/**
 * Protection handling module for HuskTowns
 *
 * @author William278
 */
public class HuskTownsProtectionModule implements ProtectionModule {

    private BukkitHuskTownsAPI huskTownsAPI;
    private final Plugin plugin;

    public HuskTownsProtectionModule(@Nonnull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        huskTownsAPI = BukkitHuskTownsAPI.getInstance();
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, Interaction action) {
        // Offline player have no permissions
        if (!p.isOnline()) {
            return false;
        }

        // Convert the dough interaction to HuskTowns' ActionType and check via the API
        return huskTownsAPI.isOperationAllowed(Operation.of(
                huskTownsAPI.getOnlineUser(p.getPlayer()),
                getHuskTownsAction(action),
                huskTownsAPI.getPosition(l)
        ));
    }

    /**
     * Returns the corresponding HuskTowns {@link OperationType} from the dough {@link Interaction}
     *
     * @param doughAction The dough {@link Interaction}
     * @return The corresponding HuskTowns {@link OperationType}
     */
    public @Nonnull OperationType getHuskTownsAction(@Nonnull Interaction doughAction) {
        switch (doughAction) {
            case BREAK_BLOCK:
                return OperationType.BLOCK_BREAK;
            case PLACE_BLOCK:
                return OperationType.BLOCK_PLACE;
            case ATTACK_ENTITY:
                return OperationType.PLAYER_DAMAGE_ENTITY;
            case ATTACK_PLAYER:
                return OperationType.PLAYER_DAMAGE_PLAYER;
            case INTERACT_BLOCK:
                return OperationType.BLOCK_INTERACT;
            default:
                return OperationType.ENTITY_INTERACT;
        }
    }
}
