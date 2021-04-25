package io.github.thebusybiscuit.cscorelib2.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;
import lombok.NonNull;
import me.angeschossen.chestprotect.api.addons.ChestProtectAddon;
import me.angeschossen.chestprotect.api.objects.BlockProtection;

public class ChestProtectProtectionModule implements ProtectionModule {

    private final Plugin plugin;
    private ChestProtectAddon chestProtect;

    public ChestProtectProtectionModule(@NonNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void load() {
        this.chestProtect = new ChestProtectAddon(plugin, false);
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, ProtectableAction action) {
        if (!action.isBlockAction() || !chestProtect.isProtectable(l.getBlock())) {
            return true;
        }

        BlockProtection protection = chestProtect.getProtection(l);
        return protection == null || protection.isTrusted(p.getUniqueId());
    }

}
