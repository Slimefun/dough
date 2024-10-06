package io.github.bakedlibs.dough.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Container;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import br.net.fabiozumbi12.RedProtect.Bukkit.Region;
import br.net.fabiozumbi12.RedProtect.Bukkit.API.RedProtectAPI;

import javax.annotation.Nonnull;

public class RedProtectProtectionModule implements ProtectionModule {

    private RedProtectAPI api;
    private final Plugin plugin;

    public RedProtectProtectionModule(@Nonnull Plugin plugin) {
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
    public boolean hasPermission(OfflinePlayer p, Location l, Interaction action) {
        Region region = api.getRegion(l);

        if (region == null) {
            return true;
        } else if (!(p instanceof Player)) {
            return false;
        }

        Player player = (Player) p;
        switch (action) {
            case INTERACT_BLOCK:
                if (l.getBlock() instanceof Container){
                    return region.canChest(player);
                }
                if (l.getBlock().getBlockData() instanceof Powerable){
                    return region.canButton(player) || region.canLever(player);
                }
                if (l.getBlock().getBlockData() instanceof Openable){
                    return region.canDoor(player);
                }
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
