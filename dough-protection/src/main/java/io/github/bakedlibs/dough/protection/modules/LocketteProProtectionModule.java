package io.github.bakedlibs.dough.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.plugin.Plugin;
import me.crafter.mc.lockettepro.LocketteProAPI;

import io.github.bakedlibs.dough.protection.ActionType;
import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;

import javax.annotation.Nonnull;

public class LocketteProProtectionModule implements ProtectionModule {

    private final Plugin plugin;

    public LocketteProProtectionModule(@Nonnull Plugin plugin) {
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
        if (action.getType() != ActionType.BLOCK) {
            return true;
        }

        Block b = l.getBlock();
        if (LocketteProAPI.isProtected(b)) {
            BlockState state = b.getState();
            if (state instanceof Sign) {
                return LocketteProAPI.isOwner((Sign) state, p);
            } else {
                return LocketteProAPI.isOwner(b, p);
            }
        }

        return true;
    }

}
