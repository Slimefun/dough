package io.github.thebusybiscuit.cscorelib2.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.yi.acru.bukkit.Lockette.Lockette;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;

public class LocketteProtectionModule implements ProtectionModule{
	
	@Override
	public boolean hasPermission(OfflinePlayer p, Location l, Action action) {
		Block b = l.getBlock();
		if(Lockette.isProtected(b)) {
			if (b.getState() instanceof Sign) {
				return !Lockette.isOwner((Sign) b.getState(), p);
			}
			else {
				return !Lockette.isOwner(b, p);
			}
		}
		else return false;
	}

	@Override
	public String getName() {
		return "Lockette";
	}

}
