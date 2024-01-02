package io.github.bakedlibs.dough.protection.modules;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.PlayerCache;
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

        Player player = (Player) p;
        ActionType townyAction = convert(action);
        PlayerCache cache = PlayerCacheUtil.getCache(player);
        boolean allowed = isInteractionAllowed(player, townyAction, l);

        // Update Towny's permission cache, or else the Towny ProtectionModule may override.
        switch (townyAction) {
            case BUILD:
                cache.setBuildPermission(l.getBlock().getType(), allowed);
                break;
            case SWITCH:
                cache.setSwitchPermission(l.getBlock().getType(), allowed);
                break;
            case DESTROY:
                cache.setDestroyPermission(l.getBlock().getType(), allowed);
                break;
            case ITEM_USE:
                cache.setItemUsePermission(l.getBlock().getType(), allowed);
                break;
        }

        return allowed;
    }

    private boolean isInteractionAllowed(Player player, ActionType type, Location l) {
        Quarter quarter = QuartersAPI.getInstance().getQuarter(l);
        if (quarter == null) {
            return false;
        }

        Resident resident = TownyAPI.getInstance().getResident(player.getUniqueId());
        if (resident == null) {
            return false;
        }

        if (quarter.getOwnerResident().equals(resident) || quarter.getTrustedResidents().contains(resident)) {
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
