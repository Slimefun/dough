package io.github.bakedlibs.dough.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.palmergames.bukkit.towny.object.TownyPermission.ActionType;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;

import javax.annotation.Nonnull;

public class TownyProtectionModule implements ProtectionModule {

    private final Plugin plugin;

    public TownyProtectionModule(@Nonnull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void load() {
        // We don't need to load any APIs, everything is static
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, Interaction action) {
        if (!(p instanceof Player)) {
            return false;
        }

        Player player = (Player) p;
        return PlayerCacheUtil.getCachePermission(player, l, l.getBlock().getType(), convert(action));
    }

    private ActionType convert(Interaction action) {
        switch (action) {
            case INTERACT_BLOCK:
            case INTERACT_ENTITY:
            case ATTACK_PLAYER:
            case ATTACK_ENTITY:
                return ActionType.ITEM_USE;
            case BREAK_BLOCK:
                return ActionType.DESTROY;
            case PLACE_BLOCK:
            default:
                return ActionType.BUILD;
        }
    }

}
