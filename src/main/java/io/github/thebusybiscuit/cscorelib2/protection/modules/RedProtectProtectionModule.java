package io.github.thebusybiscuit.cscorelib2.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import br.net.fabiozumbi12.RedProtect.Bukkit.Region;
import br.net.fabiozumbi12.RedProtect.Bukkit.API.RedProtectAPI;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;

public class RedProtectProtectionModule implements ProtectionModule {

    private RedProtectAPI api;

    @Override
    public void load() {
        api = RedProtect.get().getAPI();
    }

    @Override
    public String getName() {
        return "RedProtect";
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, ProtectableAction action) {
        Region region = api.getRegion(l);

        if (region == null) return true;
        if (!(p instanceof Player)) return false;
        Player player = (Player) p;

        switch (action) {
        case ACCESS_INVENTORIES:
            return region.canChest(player);
        case PVP:
            return region.canPVP(player, player);
        case BREAK_BLOCK:
        case PLACE_BLOCK:
        default:
            return region.canBuild(player);
        }
    }
}
