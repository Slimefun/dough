package io.github.bakedlibs.dough.protection.modules;

import javax.annotation.Nonnull;

import me.angeschossen.chestprotect.api.ChestProtectAPI;
import me.angeschossen.chestprotect.api.protection.block.BlockProtection;
import me.angeschossen.chestprotect.api.protection.world.ProtectionWorld;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import io.github.bakedlibs.dough.protection.ActionType;
import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;


public class ChestProtectProtectionModule implements ProtectionModule {

    private final Plugin plugin;
    private ChestProtectAPI api;

    public ChestProtectProtectionModule(@Nonnull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void load() {
        this.api = ChestProtectAPI.getInstance();
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, Interaction action) {
        if (action.getType() != ActionType.BLOCK || !api.getProtectionManager().isProtectableBlock(l.getBlock().getType())) {
            return true;
        }

        ProtectionWorld world = api.getProtectionWorld(l.getWorld());
        if (world == null) {
            return true;
        }

        BlockProtection protection = world.getBlockProtection(l.getBlockX(), l.getBlockY(), l.getBlockZ());
        return protection == null || protection.isTrusted(p.getUniqueId());
    }
}
