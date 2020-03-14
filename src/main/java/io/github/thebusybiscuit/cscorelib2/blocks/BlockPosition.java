package io.github.thebusybiscuit.cscorelib2.blocks;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

import java.lang.ref.WeakReference;
import java.util.Objects;

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

    public World getWorld() {
        final World world = this.world.get();
        if (world == null) {
            throw new IllegalStateException("The reference of this BlockPositions world has been cleared");
        }

        return world;
    }

    public long getPosition() {
        return position;
    }

    public int getX() {
        return (int) (this.position >> 38);
    }

    public int getY() {
        return (int) (this.position & 0xFFF);
    }

    public int getZ() {
        return (int) (this.position << 26 >> 38);
    }

    public Block getBlock() {
        return getChunk().getBlock((getX() & 0xF), getY(), (getZ() & 0xF));
    }

    public Chunk getChunk() {
        final World world = this.world.get();
        if (world == null) {
            throw new IllegalStateException("Cannot get Chunk when the world isn't loaded");
        }

        return world.getChunkAt(getChunkX(), getChunkZ());
    }

    public int getChunkX() {
        return this.getX() >> 4;
    }

    public int getChunkZ() {
        return this.getZ() >> 4;
    }

    public Location toLocation() {
        return new Location(this.world.get(), getX(), getY(), getZ());
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof BlockPosition) {
            final BlockPosition pos = (BlockPosition) o;
            if (pos.world.get() == null) return false;
            
            return this.getWorld().getUID().equals(pos.getWorld().getUID())
                    && this.position == pos.position;
        }
    }
    
    @Override
    public int hashCode() {
        final prime = 31;
        final World ref = this.world.get();
    
        int result = 0;
        result = prime * (ref == null ? 0 : ref.hashCode());
        result = prime * Long.hashCode(position);
        
        return result;
    }

    @Override
    public String toString() {
        return String.format("BlockPosition(world=%s, x=%d, y=%d, z=%d, position=%d)",
            getWorld().getName(), getX(), getY(), getZ(), getPosition()
        );
    }
}
