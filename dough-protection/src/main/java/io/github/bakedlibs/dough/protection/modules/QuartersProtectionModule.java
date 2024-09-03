package io.github.bakedlibs.dough.protection.modules;

import au.lupine.quarters.api.manager.QuarterManager;
import au.lupine.quarters.object.entity.Quarter;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyPermission.ActionType;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;


/**
 * Protection handling module for Quarters, a Towny add-on.
 * If Quarters is installed on a server, this module must be registered
 * instead of the Towny module.
 *
 * @author galacticwarrior9
 */
public class QuartersProtectionModule implements ProtectionModule {

    private final Plugin plugin;

    public QuartersProtectionModule(@Nonnull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void load() {
        // We don't need to load any APIs, everything is static
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, Interaction action) {
        if (!(p instanceof Player)) {
            return false;
        }
        return isInteractionAllowed((Player) p, convert(action), l);
    }

    private boolean isInteractionAllowed(Player player, ActionType type, Location l) {
        boolean allowedInUnderlyingPlot = PlayerCacheUtil.getCachePermission(player, l, l.getBlock().getType(), type);
        if (allowedInUnderlyingPlot) {
            return true;
        }

        Quarter quarter = QuarterManager.getInstance().getQuarter(l);
        if (quarter == null) {
            return false;
        }

        Resident resident = TownyAPI.getInstance().getResident(player.getUniqueId());
        if (resident == null) {
            return true;
        }

        return quarter.testPermission(convertToQuartersAction(type), resident);
    }

    /**
     * Returns the corresponding Towny {@link ActionType} from the dough {@link Interaction}
     *
     * @param action The dough {@link Interaction}
     * @return The corresponding Towny {@link ActionType}
     */
    private ActionType convert(Interaction action) {
        switch (action) {
            case INTERACT_BLOCK:
                return ActionType.SWITCH;
            case INTERACT_ENTITY:
            case ATTACK_PLAYER:
            case ATTACK_ENTITY:
                return ActionType.ITEM_USE;
            case BREAK_BLOCK:
                return ActionType.DESTROY;
            case PLACE_BLOCK:
            default:
                return ActionType.BUILD;
        }
    }

    /**
     * Returns the corresponding Quarters {@link au.lupine.quarters.object.state.ActionType} from the Towny {@link ActionType}
     *
     * @param action The Towny {@link ActionType}
     * @return The corresponding Quarters {@link au.lupine.quarters.object.state.ActionType}
     */
    private au.lupine.quarters.object.state.ActionType convertToQuartersAction(ActionType action) {
        switch (action) {
            case DESTROY:
                return au.lupine.quarters.object.state.ActionType.DESTROY;
            case ITEM_USE:
                return au.lupine.quarters.object.state.ActionType.ITEM_USE;
            case SWITCH:
                return au.lupine.quarters.object.state.ActionType.SWITCH;
            default:
                return au.lupine.quarters.object.state.ActionType.BUILD;
        }
    }
}
