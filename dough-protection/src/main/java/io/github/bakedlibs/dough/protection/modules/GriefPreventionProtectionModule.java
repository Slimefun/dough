package io.github.bakedlibs.dough.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

import javax.annotation.Nonnull;

public class GriefPreventionProtectionModule implements ProtectionModule {

    private DataStore dataStore;
    private final Plugin plugin;

    public GriefPreventionProtectionModule(@Nonnull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void load() {
        dataStore = GriefPrevention.instance.dataStore;
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, Interaction action) {
        Claim claim = dataStore.getClaimAt(l, true, null);

        if (claim == null) {
            return true;
        } else if (p.getUniqueId().equals(claim.ownerID)) {
            return true;
        } else if (!(p instanceof Player)) {
            return false;
        }

        switch (action) {
            case INTERACT_BLOCK:
                return claim.allowContainers((Player) p) == null;
            case ATTACK_PLAYER:
                return claim.siegeData == null || claim.siegeData.attacker == null;
            case BREAK_BLOCK:
                return claim.allowBreak((Player) p, l.getBlock().getType()) == null;
            case PLACE_BLOCK:
                return claim.allowBuild((Player) p, l.getBlock().getType()) == null;
            default:
                return claim.allowAccess((Player) p) == null;
        }
    }

}
