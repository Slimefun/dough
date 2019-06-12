package io.github.thebusybiscuit.cscorelib2.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.object.TownyPermission.ActionType;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;

public class TownyProtectionModule implements ProtectionModule {

	@Override
	public String getName() {
		return "Towny";
	}

	@Override
	public boolean hasPermission(OfflinePlayer p, Location l, Action action) {
		if (!(p instanceof Player)) return false;
		
		Player player = (Player) p;
		return PlayerCacheUtil.getCachePermission(player, l, l.getBlock().getType(), convert(action));
	}
	
	private ActionType convert(Action action) {
		switch (action) {
			case BREAK_BLOCK:
				return ActionType.DESTROY;
			case ACCESS_INVENTORIES:
				return ActionType.ITEM_USE;
			case PLACE_BLOCK:
			default:
				return ActionType.BUILD;
		}
	}

}
