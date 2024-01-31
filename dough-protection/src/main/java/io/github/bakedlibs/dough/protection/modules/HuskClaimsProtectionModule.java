package io.github.bakedlibs.dough.protection.modules;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;
import net.william278.huskclaims.api.BukkitHuskClaimsAPI;
import net.william278.huskclaims.libraries.cloplib.operation.Operation;
import net.william278.huskclaims.libraries.cloplib.operation.OperationType;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;

/**
 * Protection handling module for HuskClaims
 *
 * @author William278
 */
public class HuskClaimsProtectionModule implements ProtectionModule {

    private BukkitHuskClaimsAPI huskClaimsAPI;
    private final Plugin plugin;

    public HuskClaimsProtectionModule(@Nonnull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        huskClaimsAPI = BukkitHuskClaimsAPI.getInstance();
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

        // Convert the dough interaction to HuskClaims' ActionType and check via the API
        return huskClaimsAPI.isOperationAllowed(Operation.of(
                huskClaimsAPI.getOnlineUser(p.getPlayer()),
                getHuskClaimsAction(action),
                huskClaimsAPI.getPosition(l)
        ));
    }

    /**
     * Returns the corresponding HuskClaims {@link OperationType} from the dough {@link Interaction}
     *
     * @param doughAction The dough {@link Interaction}
     * @return The corresponding HuskClaims {@link OperationType}
     */
    public @Nonnull OperationType getHuskClaimsAction(@Nonnull Interaction doughAction) {
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
