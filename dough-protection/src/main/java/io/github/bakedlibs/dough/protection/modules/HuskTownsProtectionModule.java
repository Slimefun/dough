package io.github.bakedlibs.dough.protection.modules;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;
import net.william278.husktowns.api.HuskTownsAPI;
import net.william278.husktowns.claim.Position;
import net.william278.husktowns.claim.World;
import net.william278.husktowns.listener.Operation;
import net.william278.husktowns.user.OnlineUser;
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
        // Offline player have no permissions
        if (!p.isOnline()) {
            return false;
        }

        // Convert the dough interaction to HuskTowns' ActionType and check via the API
        return huskTownsAPI.isOperationAllowed(Operation.of(
                getOnlineUser(p), getHuskTownsAction(action), getPosition(l)));
    }

    private OnlineUser getOnlineUser(OfflinePlayer p) {
        return huskTownsAPI.getOnlineUser(p.getPlayer());
    }

    private Position getPosition(Location l) {
        org.bukkit.World w = l.getWorld();
        return Position.at(l.getX(), l.getY(), l.getZ(), World.of(w.getUID(), w.getName(), w.getEnvironment().name()));
    }

    /**
     * Returns the corresponding HuskTowns {@link Operation.Type} from the dough {@link Interaction}
     *
     * @param doughAction The dough {@link Interaction}
     * @return The corresponding HuskTowns {@link Operation.Type}
     */
    public @Nonnull Operation.Type getHuskTownsAction(@Nonnull Interaction doughAction) {
        switch (doughAction) {
            case BREAK_BLOCK:
                return Operation.Type.BLOCK_BREAK;
            case PLACE_BLOCK:
                return Operation.Type.BLOCK_PLACE;
            case ATTACK_ENTITY:
                return Operation.Type.PLAYER_DAMAGE_ENTITY;
            case ATTACK_PLAYER:
                return Operation.Type.PLAYER_DAMAGE_PLAYER;
            case INTERACT_BLOCK:
                return Operation.Type.BLOCK_INTERACT;
            default:
                return Operation.Type.ENTITY_INTERACT;
        }
    }
}
