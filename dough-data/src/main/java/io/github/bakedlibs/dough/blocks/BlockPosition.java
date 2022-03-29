package io.github.bakedlibs.dough.blocks;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * This is the position of a block in a World. Using this class as opposed to {@link Location} is much better because it
 * has a lower memory footprint and doesn't contain useless data such as yaw and pitch.<br>
 * This is 12 bytes in memory whereas {@link Location} is 36 bytes.
 * (Note, this is not accounting for things like object headers)
 * 
 * @author Walshy
 */
public final class BlockPosition {

    /**
     * A {@link WeakReference} to our {@link World}.
     * This allows the Java GC to clear the {@link World} object from
     * memory when the {@link World} is unloaded, we do not want to keep
     * this in memory unnecessarily.
     */
    private final WeakReference<World> world;

    /**
     * The encoded coordinates of this {@link BlockPosition}.
     */
    private final long position;

    /**
     * This creates a new {@link BlockPosition} from the given position and {@link World}.
     * 
     * @param world
     *            The {@link World}
     * @param position
     *            The {@link BlockPosition} (as a long)
     */
    public BlockPosition(@Nonnull World world, long position) {
        this.world = new WeakReference<>(world);
        this.position = position;
    }

    /**
     * This creates a new {@link BlockPosition} for the given position.
     * 
     * @param world
     *            The {@link World}
     * @param x
     *            The x coordinate
     * @param y
     *            The y coordinate
     * @param z
     *            The z coordinate
     */
    public BlockPosition(@Nonnull World world, int x, int y, int z) {
        this.world = new WeakReference<>(world);
        this.position = getAsLong(x, y, z);
    }

    /**
     * This creates a new {@link BlockPosition} for the given {@link Block}s position.
     * 
     * @param b
     *            The {@link Block}
     */
    public BlockPosition(@Nonnull Block b) {
        this(b.getWorld(), b.getX(), b.getY(), b.getZ());
    }

    /**
     * This creates a new {@link BlockPosition} for the given {@link Location}
     * 
     * @param l
     *            The {@link Location}
     */
    public BlockPosition(@Nonnull Location l) {
        this(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    /**
     * Gets the {@link World} this block is in. If this {@link World} has been unloaded it will throw
     * an {@link IllegalStateException}. This should be getting handled properly by yourself! <br>
     * <b>Note: This is held as a weak reference!</b>
     *
     * @return The {@link World} for this block.
     */
    public @Nonnull World getWorld() {
        World ref = this.world.get();

        if (ref == null) {
            throw new IllegalStateException("The reference of this BlockPositions world has been cleared");
        }

        return ref;
    }

    /**
     * Gets the long position of this block. This is constructed of the x, y and z. <br>
     * This is encoded as follows: {@code ((x & 0x3FFFFFF) << 38) | ((z & 0x3FFFFFF) << 12) | (y & 0xFFF)}
     *
     * @return The position of this block.
     */
    public long getPosition() {
        return position;
    }

    /**
     * Gets the x for this block.
     *
     * @return This blocks x coordinate.
     */
    public int getX() {
        return (int) (this.position >> 38);
    }

    /**
     * Gets the y for this block.
     *
     * @return This blocks y coordinate.
     */
    public int getY() {
        return (int) (this.position << 52 >> 52);
    }

    /**
     * Gets the z for this block.
     *
     * @return This blocks z coordinate.
     */
    public int getZ() {
        return (int) (this.position << 26 >> 38);
    }

    /**
     * Gets the {@link Block} at this position. Note, Bukkit will create a new instance so if you can avoid doing this
     * then do as it is a bit costly.
     *
     * @return The {@link Block} at this location.
     */
    public @Nonnull Block getBlock() {
        return getChunk().getBlock((getX() & 0xF), getY(), (getZ() & 0xF));
    }

    /**
     * Gets the {@link Chunk} where this block is located.
     *
     * @return This blocks {@link Chunk}.
     */
    public @Nonnull Chunk getChunk() {
        World ref = this.world.get();

        if (ref == null) {
            throw new IllegalStateException("Cannot get Chunk when the world isn't loaded");
        }

        return ref.getChunkAt(getChunkX(), getChunkZ());
    }

    /**
     * Gets the chunks x coordinate for this block.
     *
     * @return The blocks chunks x coordinate.
     */
    public int getChunkX() {
        return this.getX() >> 4;
    }

    /**
     * Gets the chunks z coordinate for this block.
     *
     * @return The blocks chunks z coordinate.
     */
    public int getChunkZ() {
        return this.getZ() >> 4;
    }

    /**
     * Transform this BlockPosition into a standard Bukkit {@link Location}.
     *
     * @return A Bukkit {@link Location}.
     */
    public @Nonnull Location toLocation() {
        return new Location(this.world.get(), getX(), getY(), getZ());
    }

    /**
     * This compacts the three provided integers into one {@link Long}.
     * This allows us to save a lot memory-wise.
     * 
     * @param x
     *            The x component
     * @param y
     *            The y component
     * @param z
     *            The z component
     * 
     * @return The compacted {@link Long}
     */
    public static long getAsLong(int x, int y, int z) {
        return ((long) (x & 0x3FFFFFF) << 38) | ((long) (z & 0x3FFFFFF) << 12) | (long) (y & 0xFFF);
    }

    /**
     * This compacts the three provided integers into one {@link Long}.
     * This allows us to save a lot memory-wise.
     * 
     * @param loc
     *            The {@link Location} to simplify.
     * 
     * @return The compacted {@link Long}, ignoring the {@link World}
     */
    public static long getAsLong(@Nonnull Location loc) {
        return getAsLong(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof BlockPosition) {
            BlockPosition pos = (BlockPosition) o;

            if (pos.world.get() == null) {
                return false;
            }

            return this.getWorld().getUID().equals(pos.getWorld().getUID()) && this.position == pos.position;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int prime = 31;
        World ref = this.world.get();

        int result = 0;
        result += prime * (ref == null ? 0 : ref.hashCode());
        result += prime * Long.hashCode(position);

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        World w = this.world.get();
        String worldName = w != null ? w.getName() : "<no reference>";

        return String.format("BlockPosition(world=%s, x=%d, y=%d, z=%d, position=%d)", worldName, getX(), getY(), getZ(), getPosition());
    }

}
