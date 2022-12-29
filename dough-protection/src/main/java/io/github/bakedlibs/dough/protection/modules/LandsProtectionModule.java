package io.github.bakedlibs.dough.protection.modules;

import javax.annotation.Nonnull;

import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.flags.type.Flags;
import me.angeschossen.lands.api.flags.type.RoleFlag;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;

import me.angeschossen.lands.api.land.Area;
import me.angeschossen.lands.api.land.LandWorld;

public class LandsProtectionModule implements ProtectionModule {

    private LandsIntegration landsIntegration;
    private final Plugin lands;
    private final Plugin plugin;

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
        this.landsIntegration = LandsIntegration.of(plugin);
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, Interaction action) {
        LandWorld landWorld = landsIntegration.getWorld(l.getWorld());

        if (landWorld == null) {
            return true;
        }

        Area area = landWorld.getArea(l);
        return area == null || area.hasRoleFlag(p.getUniqueId(), convert(action));
    }

    private @Nonnull RoleFlag convert(@Nonnull Interaction protectableAction) {
        switch (protectableAction) {
            case PLACE_BLOCK:
                return Flags.BLOCK_PLACE;
            case BREAK_BLOCK:
                return Flags.BLOCK_BREAK;
            case INTERACT_BLOCK:
                return Flags.INTERACT_CONTAINER;
            case ATTACK_PLAYER:
                return Flags.ATTACK_PLAYER;
            case ATTACK_ENTITY:
                return Flags.ATTACK_ANIMAL;
            default:
                return Flags.INTERACT_VILLAGER;
        }
    }
}
