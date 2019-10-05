package io.github.thebusybiscuit.cscorelib2.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;
import net.sacredlabyrinth.Phaed.PreciousStones.PreciousStones;
import net.sacredlabyrinth.Phaed.PreciousStones.api.IApi;
import net.sacredlabyrinth.Phaed.PreciousStones.field.FieldFlag;

public class PreciousStonesProtectionModule implements ProtectionModule {

	private IApi api;
	
	@Override
	public void load() {
		api = PreciousStones.API();
	}

    @Override
    public String getName() {
        return "PreciousStones";
    }
	
	@Override
	public boolean hasPermission(OfflinePlayer p, Location l, ProtectableAction action) {
		if (!(p instanceof Player)) return false;
		
        switch (action) {
			case PVP:
				return !api.flagAppliesToPlayer((Player) p, FieldFlag.PREVENT_PVP, l);
			case BREAK_BLOCK:
				return api.canBreak((Player) p, l);
			case ACCESS_INVENTORIES:
			case PLACE_BLOCK:
				return api.canPlace((Player) p, l);
			default:
				return false;
        }
	}
}
