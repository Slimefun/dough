package io.github.thebusybiscuit.dough.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.plugin.Plugin;
import org.yi.acru.bukkit.Lockette.Lockette;

import io.github.thebusybiscuit.dough.protection.ActionType;
import io.github.thebusybiscuit.dough.protection.Interaction;
import io.github.thebusybiscuit.dough.protection.ProtectionModule;

import lombok.NonNull;

public class LocketteProtectionModule implements ProtectionModule {

    private final Plugin plugin;

    public LocketteProtectionModule(@NonNull Plugin plugin) {
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

        if (Lockette.isProtected(b)) {
            BlockState state = b.getState();

            if (state instanceof Sign) {
                return !Lockette.isOwner((Sign) state, p);
            } else {
                return !Lockette.isOwner(b, p);
            }
        } else
            return false;
    }

}
