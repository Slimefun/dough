package io.github.thebusybiscuit.cscorelib2.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class GriefPreventionProtectionModule implements ProtectionModule {

	@Override
	public String getName() {
		return "GriefPrevention";
	}
	
	@Override
	public boolean hasPermission(OfflinePlayer p, Location l, Action action) {
		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(l, true, null);
		
		if (claim == null) return true;
		if (p.getUniqueId().equals(claim.ownerID)) return true;
		
		if (!(p instanceof Player)) return false;
		
		switch (action) {
			case ACCESS_INVENTORIES:
				return claim.allowContainers((Player) p) == null;
			case BREAK_BLOCK:
				return claim.allowBreak((Player) p, l.getBlock().getType()) == null;
			case PLACE_BLOCK:
			default:
				return claim.allowBuild((Player) p, l.getBlock().getType()) == null;
		}
	}

}
