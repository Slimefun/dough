package io.github.thebusybiscuit.cscorelib2.protection.modules;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;
import nl.rutgerkok.blocklocker.BlockLockerAPIv2;
import nl.rutgerkok.blocklocker.BlockLockerPlugin;
import nl.rutgerkok.blocklocker.profile.Profile;
import nl.rutgerkok.blocklocker.protection.Protection;

public class BlockLockerProtectionModule implements ProtectionModule {

    private BlockLockerPlugin api;
    private final Plugin plugin;

    public BlockLockerProtectionModule(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void load() {
        api = BlockLockerAPIv2.getPlugin();
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, ProtectableAction action) {
        if (!action.isBlockAction()) return true;

        Optional<Protection> protection = api.getProtectionFinder().findProtection(l.getBlock());

        if (protection.isPresent()) {
            Profile profile = api.getProfileFactory().fromNameAndUniqueId(p.getName(), Optional.of(p.getUniqueId()));
            return protection.get().isAllowed(profile);
        }
        else {
            return true;
        }

    }
}
