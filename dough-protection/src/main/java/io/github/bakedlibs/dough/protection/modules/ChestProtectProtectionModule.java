package io.github.bakedlibs.dough.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import io.github.bakedlibs.dough.protection.ActionType;
import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;

import me.angeschossen.chestprotect.api.addons.ChestProtectAddon;
import me.angeschossen.chestprotect.api.objects.BlockProtection;

import javax.annotation.Nonnull;

public class ChestProtectProtectionModule implements ProtectionModule {

    private final Plugin plugin;
    private ChestProtectAddon chestProtect;

    public ChestProtectProtectionModule(@Nonnull Plugin plugin) {
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
    public boolean hasPermission(OfflinePlayer p, Location l, Interaction action) {
        if (action.getType() != ActionType.BLOCK || !chestProtect.isProtectable(l.getBlock())) {
            return true;
        }

        BlockProtection protection = chestProtect.getProtection(l);
        return protection == null || protection.isTrusted(p.getUniqueId());
    }

}
