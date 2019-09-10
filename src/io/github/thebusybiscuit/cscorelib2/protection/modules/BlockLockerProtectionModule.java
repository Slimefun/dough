package io.github.thebusybiscuit.cscorelib2.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import com.google.common.base.Optional;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;
import nl.rutgerkok.blocklocker.BlockLockerAPI;
import nl.rutgerkok.blocklocker.BlockLockerPlugin;
import nl.rutgerkok.blocklocker.profile.Profile;
import nl.rutgerkok.blocklocker.protection.Protection;


public class BlockLockerProtectionModule implements ProtectionModule {
	
	private BlockLockerPlugin plugin;
	
	@Override
	public void load() {
		plugin = BlockLockerAPI.getPlugin();
	}

	@Override
	public String getName() {
		return "BlockLocker";
	}
	
	@Override
	public boolean hasPermission(OfflinePlayer p, Location l, Action action) {
		Optional<Protection> protection = plugin.getProtectionFinder().findProtection(l.getBlock());
		
		if (protection.isPresent()) {
			Profile profile = plugin.getProfileFactory().fromNameAndUniqueId(p.getName(), Optional.of(p.getUniqueId()));
			return protection.get().isAllowed(profile);
		}
		else {
			return true;
		}
		
	}
}
	
