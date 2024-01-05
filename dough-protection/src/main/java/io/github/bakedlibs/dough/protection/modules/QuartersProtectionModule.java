package io.github.bakedlibs.dough.protection.modules;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyPermission.ActionType;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;
import net.earthmc.quarters.api.QuartersAPI;
import net.earthmc.quarters.object.Quarter;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;


/**
 * Protection handling module for Quarters, a Towny add-on.
 * If Quarters is installed on a server, this module must be enabled
 * in lieu of the Towny module.
 *
 * @author Fruitloopins
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

        Quarter quarter = QuartersAPI.getInstance().getQuarter(l);
        if (quarter == null) {
            return false;
        }

        Resident resident = TownyAPI.getInstance().getResident(player.getUniqueId());
        if (resident == null) {
            return false;
        }

        if (resident.equals(quarter.getOwnerResident()) || quarter.getTrustedResidents().contains(resident)) {
            return true;
        }

        switch (quarter.getType()) {
            case COMMONS:
                if (type != ActionType.SWITCH && type != ActionType.ITEM_USE) {
                    return false;
                }
                return isAllowed(quarter, resident);
            case PUBLIC:
                return isAllowed(quarter, resident);
            case STATION:
                if (type != ActionType.ITEM_USE && type != ActionType.DESTROY && type != ActionType.SWITCH) {
                    return false;
                }
                return isAllowed(quarter, resident);
            case WORKSITE:
                if (type != ActionType.BUILD && type != ActionType.DESTROY) {
                    return false;
                }
                return isAllowed(quarter, resident);
            default:
                return false;
        }
    }

    private boolean isAllowed(Quarter quarter, Resident resident) {
        return quarter.isEmbassy() || (quarter.getTown() == resident.getTownOrNull());
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
}
