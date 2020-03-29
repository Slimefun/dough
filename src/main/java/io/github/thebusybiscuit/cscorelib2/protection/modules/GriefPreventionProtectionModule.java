package io.github.thebusybiscuit.cscorelib2.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class GriefPreventionProtectionModule implements ProtectionModule {

    private DataStore dataStore;

    @Override
    public void load() {
        dataStore = GriefPrevention.instance.dataStore;
    }

    @Override
    public String getName() {
        return "GriefPrevention";
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, ProtectableAction action) {
        Claim claim = dataStore.getClaimAt(l, true, null);

        if (claim == null) return true;
        if (p.getUniqueId().equals(claim.ownerID)) return true;

        if (!(p instanceof Player)) return false;

        switch (action) {
        case ACCESS_INVENTORIES:
            return claim.allowContainers((Player) p) == null;
        case PVP:
            return claim.siegeData.attacker == null;
        case BREAK_BLOCK:
            return claim.allowBreak((Player) p, l.getBlock().getType()) == null;
        case PLACE_BLOCK:
        default:
            return claim.allowBuild((Player) p, l.getBlock().getType()) == null;
        }
    }

}
