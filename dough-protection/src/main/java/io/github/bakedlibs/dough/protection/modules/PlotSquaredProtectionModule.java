package io.github.bakedlibs.dough.protection.modules;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.plotsquared.bukkit.util.BukkitUtil;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.permissions.Permission;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;

public class PlotSquaredProtectionModule implements ProtectionModule {

    private final Plugin plugin;

    public PlotSquaredProtectionModule(@Nonnull Plugin plugin) {
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
        Location location = Location.at(b.getWorld().getName(), b.getX(), b.getY(), b.getZ());

        if (location.isPlotRoad()) {
            return check(p, action);
        }

        Plot plot = location.getOwnedPlot();
        return plot == null || plot.isAdded(p.getUniqueId()) || check(p, action);
    }

    @ParametersAreNonnullByDefault
    private boolean check(OfflinePlayer p, Interaction action) {
        PlotPlayer<OfflinePlayer> player = PlotPlayer.from(p);

        switch (action) {
            case INTERACT_BLOCK:
                return player.hasPermission(Permission.PERMISSION_ADMIN_INTERACT_UNOWNED);
            case ATTACK_PLAYER:
                return player.hasPermission(Permission.PERMISSION_ADMIN_PVP);
            case PLACE_BLOCK:
            default:
                return player.hasPermission(Permission.PERMISSION_ADMIN_BUILD_UNOWNED);
        }
    }
}
