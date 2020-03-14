package io.github.thebusybiscuit.cscorelib2.blocks;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * This is the position of a block in a World. Using this class as opposed to {@link Location} is much better because it
 * has a lower memory footprint and doesn't contain useless data such as yaw and pitch.<br />
 * This is 12 bytes in memory whereas {@link Location} is 36 bytes.
 * (Note, this is not accounting for things like object headers)
 */
public final class BlockPosition {

    private final WeakReference<World> world;
    private final long position;

    public BlockPosition(World world, long position) {
        Objects.requireNonNull(world, "World cannot be null!");

        this.world = new WeakReference<>(world);
        this.position = position;
    }

    public BlockPosition(World world, int x, int y, int z) {
        Objects.requireNonNull(world, "World cannot be null!");

        this.world = new WeakReference<>(world);
        this.position = ((long) (x & 0x3FFFFFF) << 38) | ((long) (z & 0x3FFFFFF) << 12) | (long) (y & 0xFFF);
    }

    /**
     * Gets the {@link World} this block is in. If this {@link World} has been unloaded it will throw
     * an {@link IllegalStateException}. This should be getting handled properly by yourself! <br />
     * <b>Note: This is held as a weak reference!</b>
     *
     * @return The {@link World} for this block.
     */
    public World getWorld() {
        final World world = this.world.get();
        if (world == null) {
            throw new IllegalStateException("The reference of this BlockPositions world has been cleared");
        }

        return world;
    }

    /**
     * Gets the long position of this block. This is constructed of the x, y and z. <br />
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
        return (int) (this.position & 0xFFF);
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
    public Block getBlock() {
        return getChunk().getBlock((getX() & 0xF), getY(), (getZ() & 0xF));
    }

    /**
     * Gets the {@link Chunk} where this block is located.
     *
     * @return This blocks {@link Chunk}.
     */
    public Chunk getChunk() {
        final World world = this.world.get();
        if (world == null) {
            throw new IllegalStateException("Cannot get Chunk when the world isn't loaded");
        }

        return world.getChunkAt(getChunkX(), getChunkZ());
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
    public Location toLocation() {
        return new Location(this.world.get(), getX(), getY(), getZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof BlockPosition) {
            final BlockPosition pos = (BlockPosition) o;
            if (pos.world.get() == null) return false;
            
            return this.getWorld().getUID().equals(pos.getWorld().getUID())
                    && this.position == pos.position;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        final World ref = this.world.get();
    
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
        return String.format("BlockPosition(world=%s, x=%d, y=%d, z=%d, position=%d)",
            getWorld().getName(), getX(), getY(), getZ(), getPosition()
        );
    }
}
