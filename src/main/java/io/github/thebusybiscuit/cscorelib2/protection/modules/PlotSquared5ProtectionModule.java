package io.github.thebusybiscuit.cscorelib2.protection.modules;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;

import com.plotsquared.core.configuration.Captions;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.util.Permissions;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;

public class PlotSquared5ProtectionModule implements ProtectionModule {

    @Override
    public void load() {
        // We don't need to load any APIs, everything is static
    }

    @Override
    public String getName() {
        return "PlotSquared v5";
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, org.bukkit.Location l, ProtectableAction action) {
        Block b = l.getBlock();

        Plot plot = new Location(b.getWorld().getName(), b.getX(), b.getY(), b.getZ()).getOwnedPlot();
        return plot == null || plot.isAdded(p.getUniqueId()) || check(p, action);
    }

    private boolean check(OfflinePlayer p, ProtectableAction action) {
        switch (action) {
        case ACCESS_INVENTORIES:
            return Permissions.hasPermission(PlotPlayer.wrap(p), Captions.PERMISSION_ADMIN_INTERACT_UNOWNED);
        case PVP:
            return Permissions.hasPermission(PlotPlayer.wrap(p), Captions.FLAG_PVP);
        case BREAK_BLOCK:
        case PLACE_BLOCK:
        default:
            return Permissions.hasPermission(PlotPlayer.wrap(p), Captions.PERMISSION_ADMIN_BUILD_UNOWNED);
        }
    }
}
