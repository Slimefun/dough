package io.github.thebusybiscuit.cscorelib2.protection.modules;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;

public class ASkyBlockProtectionModule implements ProtectionModule {
	
	private ASkyBlockAPI api = ASkyBlockAPI.getInstance();
	
	@Override
	public boolean hasPermission(OfflinePlayer p, Location l, Action action) {
		Island island = api.getIslandAt(l);
		if (island == null) return true;
		if (p.getUniqueId().equals(island.getOwner())) return true;
		
		for (UUID member: island.getMembers()) {
			if (p.getUniqueId().equals(member)) return true;
		}
		
		return false;
	}

	@Override
	public String getName() {
		return "ASkyBlock";
	}

}
