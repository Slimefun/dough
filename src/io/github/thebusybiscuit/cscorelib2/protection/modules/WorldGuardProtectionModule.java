package io.github.thebusybiscuit.cscorelib2.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;

public class WorldGuardProtectionModule implements ProtectionModule {

	private WorldGuardPlugin worldguard = WorldGuardPlugin.inst();
	private WorldGuardPlatform platform = WorldGuard.getInstance().getPlatform();
	private RegionContainer manager = platform.getRegionContainer();

	@Override
	public String getName() {
		return "WorldGuard";
	}
	
	@Override
	public boolean hasPermission(OfflinePlayer p, Location l, Action action) {
		com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(l);
		com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(l.getWorld());
		LocalPlayer player = worldguard.wrapOfflinePlayer(p);
		
		if (platform.getSessionManager().hasBypass(player, world)) {
			return true;
		}
		
		return manager.createQuery().testBuild(loc, player, convert(action));
	}

	private StateFlag convert(Action action) {
		switch(action) {
			case PVP:
				return Flags.PVP;
			case ACCESS_INVENTORIES:
				return Flags.USE;
			case BREAK_BLOCK:
				return Flags.BLOCK_BREAK;
			case PLACE_BLOCK:
				return Flags.BLOCK_PLACE;
			default:
				return Flags.BUILD;
		}
	}
}