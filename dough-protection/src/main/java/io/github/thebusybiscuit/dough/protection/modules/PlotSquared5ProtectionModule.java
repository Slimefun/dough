package io.github.thebusybiscuit.dough.protection.modules;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import com.plotsquared.core.configuration.Captions;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.util.Permissions;

import io.github.thebusybiscuit.dough.protection.Interaction;
import io.github.thebusybiscuit.dough.protection.ProtectionModule;

import javax.annotation.Nonnull;

public class PlotSquared5ProtectionModule implements ProtectionModule {

    private final Plugin plugin;

    public PlotSquared5ProtectionModule(@Nonnull Plugin plugin) {
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
    public boolean hasPermission(OfflinePlayer p, org.bukkit.Location l, Interaction action) {
        Block b = l.getBlock();

        Location location = new Location(b.getWorld().getName(), b.getX(), b.getY(), b.getZ());

        if (location.isPlotRoad()) {
            return check(p, action);
        }

        Plot plot = location.getOwnedPlot();
        return plot == null || plot.isAdded(p.getUniqueId()) || check(p, action);
    }

    private boolean check(OfflinePlayer p, Interaction action) {
        switch (action) {
            case INTERACT_BLOCK:
                return Permissions.hasPermission(PlotPlayer.wrap(p), Captions.PERMISSION_ADMIN_INTERACT_UNOWNED);
            case INTERACT_ENTITY:
            case ATTACK_ENTITY:
                return Permissions.hasPermission(PlotPlayer.wrap(p), Captions.FLAG_ANIMAL_INTERACT);
            case ATTACK_PLAYER:
                return Permissions.hasPermission(PlotPlayer.wrap(p), Captions.FLAG_PVP);
            case PLACE_BLOCK:
            default:
                return Permissions.hasPermission(PlotPlayer.wrap(p), Captions.PERMISSION_ADMIN_BUILD_UNOWNED);
        }
    }
}
