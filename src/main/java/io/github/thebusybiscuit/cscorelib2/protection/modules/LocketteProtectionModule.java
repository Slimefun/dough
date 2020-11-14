package io.github.thebusybiscuit.cscorelib2.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.plugin.Plugin;
import org.yi.acru.bukkit.Lockette.Lockette;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;

public class LocketteProtectionModule implements ProtectionModule {

    private final Plugin plugin;

    public LocketteProtectionModule(Plugin plugin) {
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
    public boolean hasPermission(OfflinePlayer p, Location l, ProtectableAction action) {
        if (!action.isBlockAction()) {
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
