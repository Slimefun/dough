package io.github.bakedlibs.dough.protection.modules;

import net.dzikoysk.funnyguilds.feature.protection.ProtectionSystem;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;

import javax.annotation.Nonnull;

/**
 * Provides protection handling for FunnyGuilds
 *
 * @author barpec12 on 05.04.2021
 */
public class FunnyGuildsProtectionModule implements ProtectionModule {

    private final Plugin plugin;

    public FunnyGuildsProtectionModule(@Nonnull Plugin plugin) {
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
        return p instanceof Player && ProtectionSystem.isProtected((Player) p, l, convert(action)).isEmpty();
    }

    private boolean convert(Interaction protectableAction) {
        return protectableAction == Interaction.PLACE_BLOCK;
    }

}
