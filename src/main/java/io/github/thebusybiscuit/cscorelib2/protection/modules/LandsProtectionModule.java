package io.github.thebusybiscuit.cscorelib2.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Area;
import me.angeschossen.lands.api.land.LandWorld;
import me.angeschossen.lands.api.role.enums.RoleSetting;

public class LandsProtectionModule implements ProtectionModule {

    private LandsIntegration landsIntegration;
    private final Plugin plugin;

    public LandsProtectionModule(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void load() {
        this.landsIntegration = new LandsIntegration(plugin);
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, ProtectableAction action) {
        LandWorld landWorld = landsIntegration.getLandWorld(l.getWorld());

        if (landWorld == null) {
            return true;
        }

        Area area = landWorld.getArea(l);
        return area == null || area.canSetting(p.getUniqueId(), convert(action));
    }

    private RoleSetting convert(ProtectableAction protectableAction) {
        switch (protectableAction) {
        case PLACE_BLOCK:
            return RoleSetting.BLOCK_PLACE;
        case BREAK_BLOCK:
            return RoleSetting.BLOCK_BREAK;
        case ATTACK_PLAYER:
            return RoleSetting.ATTACK_PLAYER;
        case INTERACT_BLOCK:
            return RoleSetting.INTERACT_CONTAINER;
        case INTERACT_ENTITY:
            return RoleSetting.INTERACT_VILLAGER;
        case ATTACK_ENTITY:
            return RoleSetting.ATTACK_ANIMAL;
        default:
            return RoleSetting.BLOCK_BREAK;
        }
    }
}
