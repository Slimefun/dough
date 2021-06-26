package io.github.thebusybiscuit.cscorelib2.world;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;

import lombok.NonNull;

/**
 * This class allows us to easily check if a {@link Location} is within
 * the boundaries of a {@link World}.
 * 
 * @author TheBusyBiscuit
 *
 */
public final class WorldBounds {

    private WorldBounds() {
        // Do not instantiate this class
    }

    public int getMinHeight(@NonNull World world) {
        /*
         * For MC 1.17 we will return the actual World
         * height, for older versions we fall back to zero.
         */
        return 0;
    }

    public int getMaxHeight(@NonNull World world) {
        return world.getMaxHeight();
    }

    public boolean contain(@NonNull World world, @NonNull Location loc) {
        // Check if the world matches
        if (!loc.getWorld().equals(world)) {
            return false;
        }

        int y = loc.getBlockY();

        if (y < getMinHeight(world) || y >= getMaxHeight(world)) {
            return false;
        } else {
            WorldBorder border = world.getWorldBorder();
            return border.isInside(loc);
        }
    }

}
