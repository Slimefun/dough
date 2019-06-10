package io.github.thebusybiscuit.cscorelib2.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;
import net.sacredlabyrinth.Phaed.PreciousStones.PreciousStones;
import net.sacredlabyrinth.Phaed.PreciousStones.api.IApi;

public class PreciousStonesProtectionModule implements ProtectionModule {

	private IApi api = PreciousStones.API();
	
	@Override
	public boolean hasPermission(OfflinePlayer p, Location l, Action action) {
		if (!(p instanceof Player)) return false;
		
        switch (action) {
			case BREAK_BLOCK:
				return api.canBreak((Player) p, l);
			case ACCESS_INVENTORIES:
			case PLACE_BLOCK:
				return api.canPlace((Player) p, l);
			default:
				return false;
        }
	}

    @Override
    public String getName() {
        return "PreciousStones";
    }
}
