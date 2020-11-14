package io.github.thebusybiscuit.cscorelib2.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.griefcraft.lwc.LWC;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;

public class LWCProtectionModule implements ProtectionModule {

    private LWC lwc;
    private final Plugin plugin;

    public LWCProtectionModule(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void load() {
        lwc = LWC.getInstance();
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, ProtectableAction action) {
        if (!action.isBlockAction()) {
            return true;
        } else if (!lwc.isProtectable(l.getBlock())) {
            return true;
        } else if (lwc.getProtectionCache().getProtection(l.getBlock()) == null) {
            return true;
        }

        return p instanceof Player && lwc.canAccessProtection((Player) p, l.getBlock());
    }
}
