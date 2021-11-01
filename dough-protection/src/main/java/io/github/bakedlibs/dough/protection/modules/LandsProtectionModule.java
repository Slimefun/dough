package io.github.bakedlibs.dough.protection.modules;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;

import me.angeschossen.lands.api.flags.Flags;
import me.angeschossen.lands.api.flags.types.RoleFlag;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Area;
import me.angeschossen.lands.api.land.LandWorld;

public class LandsProtectionModule implements ProtectionModule {

    private LandsIntegration landsIntegration;
    private final Plugin lands, plugin;

    public LandsProtectionModule(@Nonnull Plugin lands, @Nonnull Plugin plugin) {
        this.lands = lands;
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return lands;
    }

    @Override
    public void load() {
        this.landsIntegration = new LandsIntegration(plugin);
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, Interaction action) {
        LandWorld landWorld = landsIntegration.getLandWorld(l.getWorld());

        if (landWorld == null) {
            return true;
        }

        Area area = landWorld.getArea(l);
        return area == null || area.hasFlag(p.getUniqueId(), convert(action));
    }

    private @Nonnull RoleFlag convert(@Nonnull Interaction protectableAction) {
        switch (protectableAction) {
            case PLACE_BLOCK:
                return Flags.BLOCK_PLACE;
            case ATTACK_PLAYER:
                return Flags.ATTACK_PLAYER;
            case INTERACT_BLOCK:
                return Flags.INTERACT_CONTAINER;
            case INTERACT_ENTITY:
                return Flags.INTERACT_VILLAGER;
            case ATTACK_ENTITY:
                return Flags.ATTACK_ANIMAL;
            default:
                return Flags.BLOCK_BREAK;
        }
    }
}
