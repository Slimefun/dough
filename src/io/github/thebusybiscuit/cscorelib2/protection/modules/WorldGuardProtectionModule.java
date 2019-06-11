package io.github.thebusybiscuit.cscorelib2.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;

public class WorldGuardProtectionModule implements ProtectionModule {

	private WorldGuardPlugin worldguard = WorldGuardPlugin.inst();
	private RegionContainer manager = WorldGuard.getInstance().getPlatform().getRegionContainer();

	@Override
	public String getName() {
		return "WorldGuard";
	}
	
	@Override
	public boolean hasPermission(OfflinePlayer p, Location l, Action action) {
		com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(l);
		return manager.createQuery().testState(loc, worldguard.wrapOfflinePlayer(p), convert(action));
	}

	private StateFlag convert(Action action) {
		switch(action) {
			case BREAK_BLOCK:
				return Flags.BLOCK_BREAK;
			case PLACE_BLOCK:
				return Flags.BLOCK_PLACE;
			case ACCESS_INVENTORIES:
				return Flags.USE;
			default:
				return Flags.BUILD;
		}
	}
}