package io.github.thebusybiscuit.cscorelib2.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.griefcraft.lwc.LWC;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;

public class LWCProtectionModule implements ProtectionModule {

	private LWC lwc = LWC.getInstance();
	
    @Override
    public String getName() {
        return "LWC";
    }
    
    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, Action action) {
        if (action == Action.PVP) return true;
        if (!lwc.isProtectable(l.getBlock())) return true;
    	if (lwc.getProtectionCache().getProtection(l.getBlock()) == null) return true;

        return p instanceof Player && LWC.getInstance().canAccessProtection((Player) p, l.getBlock());
    }
}
