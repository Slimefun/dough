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
        return "Landss";
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, ProtectableAction action) {
        LandWorld landWorld = landsIntegration.getLandWorld(l.getWorld().getName());
        if (landWorld == null) return true;
        if (!(p instanceof Player)) return false;

        RoleSetting roleSetting = RoleSetting.BLOCK_PLACE;
        switch (action) {
            case BREAK_BLOCK: {
                roleSetting = RoleSetting.BLOCK_BREAK;
                break;
            }

            case PVP: {
                roleSetting = RoleSetting.ATTACK_PLAYER;
                break;
            }

            case ACCESS_INVENTORIES: {
                roleSetting = RoleSetting.INTERACT_CONTAINER;
                break;
            }
        }

        return landWorld.canAction((Player) p, l, roleSetting);
    }
}
