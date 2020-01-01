package io.github.thebusybiscuit.cscorelib2.protection.modules;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.LandWorld;
import me.angeschossen.lands.api.role.enums.RoleSetting;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class LandsProtectionModule implements ProtectionModule {

    private LandsIntegration landsIntegration;

    @Override
    public void load() {
        this.landsIntegration = new LandsIntegration("CS-CoreLib2", false);
    }

    @Override
    public String getName() {
        return "Lands";
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, ProtectableAction action) {
        LandWorld landWorld = landsIntegration.getLandWorld(l.getWorld().getName());
        if (!(p instanceof Player)) return false;
        if (landWorld == null) return true;

        return landWorld.canAction((Player) p, l, convert(action));
    }

    private RoleSetting convert(ProtectableAction protectableAction) {
        switch (protectableAction) {
            case PLACE_BLOCK:
                return RoleSetting.BLOCK_PLACE;

            case PVP:
                return RoleSetting.ATTACK_PLAYER;


            case ACCESS_INVENTORIES:
                return RoleSetting.INTERACT_CONTAINER;

            default:
                return RoleSetting.BLOCK_BREAK;
        }
    }
}
