package io.github.thebusybiscuit.cscorelib2.protection;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

public interface ProtectionModule {
	
	public String getName();
	public boolean hasPermission(OfflinePlayer p, Location l, Action action);
	
	public static enum Action {
		
		BREAK_BLOCK,
		PLACE_BLOCK,
		ACCESS_INVENTORIES
		
	}

}
