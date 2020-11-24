package io.github.thebusybiscuit.cscorelib2.blocks;

import java.lang.ref.WeakReference;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.World;

import lombok.NonNull;

/**
 * This is a memory efficient version of a {@link Chunk}.
 * It can be used to optimize {@link Map} usage or other storage means.
 * 
 * @author TheBusyBiscuit
 * @author Walshy
 * 
 * @see BlockPosition
 *
 */
public final class ChunkPosition {

    private final WeakReference<World> world;
    private final long position;

    public ChunkPosition(@NonNull World world, long position) {
        this.world = new WeakReference<>(world);
        this.position = position;
    }

    public ChunkPosition(@NonNull World world, int x, int z) {
        this.world = new WeakReference<>(world);
        this.position = (((long) x) << 32) | (z & 0xFFFFFFFFL);
    }

    public ChunkPosition(@NonNull Chunk chunk) {
        this(chunk.getWorld(), chunk.getX(), chunk.getZ());
    }

    /**
     * Gets the {@link World} this block is in. If this {@link World} has been unloaded it will throw
     * an {@link IllegalStateException}. This should be getting handled properly by yourself! <br>
     * <b>Note: This is held as a weak reference!</b>
     *
     * @return The {@link World} for this block.
     */
    public World getWorld() {
        final World ref = this.world.get();

        if (ref == null) {
            throw new IllegalStateException("The reference of this BlockPositions world has been cleared");
        }

        return ref;
    }

    /**
     * Gets the long position of this block. This is constructed of the x and z. <br>
     * This is encoded as follows: {@code (x << 32) | (z & 0xFFFFFFFFL)}
     *
     * @return The position of this block.
     */
    public long getPosition() {
        return position;
    }

    /**
     * Gets the x for this {@link Chunk}.
     *
     * @return This chunks x coordinate.
     */
    public int getX() {
        return (int) (this.position >> 32);
    }

    /**
     * Gets the y for this {@link Chunk}.
     *
     * @return This chunks z coordinate.
     */
    public int getZ() {
        return (int) this.position;
    }

    /**
     * Gets the {@link Chunk} where this block is located.
     *
     * @return This blocks {@link Chunk}.
     */
    public Chunk getChunk() {
        final World ref = this.world.get();

        if (ref == null) {
            throw new IllegalStateException("Cannot get Chunk when the world isn't loaded");
        }

        return ref.getChunkAt(getX(), getZ());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof ChunkPosition) {
            final ChunkPosition pos = (ChunkPosition) o;

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
        World w = this.world.get();
        final String worldName = w != null ? w.getName() : "<no reference>";

        return String.format("ChunkPosition(world=%s, x=%d, z=%d, position=%d)", worldName, getX(), getZ(), getPosition());
    }
}
