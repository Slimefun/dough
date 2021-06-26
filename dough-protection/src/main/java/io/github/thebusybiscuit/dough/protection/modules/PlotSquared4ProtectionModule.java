package io.github.thebusybiscuit.dough.protection.modules;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import com.github.intellectualsites.plotsquared.plot.config.Captions;
import com.github.intellectualsites.plotsquared.plot.object.Location;
import com.github.intellectualsites.plotsquared.plot.object.Plot;
import com.github.intellectualsites.plotsquared.plot.object.PlotPlayer;
import com.github.intellectualsites.plotsquared.plot.util.Permissions;

import io.github.thebusybiscuit.dough.protection.Interaction;
import io.github.thebusybiscuit.dough.protection.ProtectionModule;

import lombok.NonNull;

public class PlotSquared4ProtectionModule implements ProtectionModule {

    private final Plugin plugin;

    public PlotSquared4ProtectionModule(@NonNull Plugin plugin) {
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
