package io.github.thebusybiscuit.cscorelib2.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import br.net.fabiozumbi12.RedProtect.Bukkit.Region;
import br.net.fabiozumbi12.RedProtect.Bukkit.API.RedProtectAPI;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;

public class RedProtectProtectionModule implements ProtectionModule {

    private RedProtectAPI api;
    private final Plugin plugin;

    public RedProtectProtectionModule(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void load() {
        api = RedProtect.get().getAPI();
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, ProtectableAction action) {
        Region region = api.getRegion(l);

        if (region == null) {
            return true;
        } else if (!(p instanceof Player)) {
            return false;
        }

        Player player = (Player) p;
        switch (action) {
            case INTERACT_BLOCK:
                return region.canChest(player);
            case ATTACK_ENTITY:
            case INTERACT_ENTITY:
                return region.canInteractPassives(player);
            case ATTACK_PLAYER:
                return region.canPVP(player, player);
            case BREAK_BLOCK:
            case PLACE_BLOCK:
            default:
                return region.canBuild(player);
        }
    }
}
