package io.github.bakedlibs.dough.blocks;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.WorldMock;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.*;

class TestChunkPosition {

    @BeforeAll
    static void init() {
        MockBukkit.mock();
    }

    @AfterAll
    static void teardown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test the coordinate getters")
    void testCoordinateGetters() {
        int x = 10;
        int z = 50;
        World world = new WorldMock();
        ChunkPosition chunkPosition = new ChunkPosition(world, x, z);
        Assertions.assertEquals(x, chunkPosition.getX());
        Assertions.assertEquals(z, chunkPosition.getZ());
    }

    @Test
    void testConstructors() {
        int x = 10;
        int z = 50;
        World world = new WorldMock();
        ChunkPosition chunkPosition = new ChunkPosition(world, x, z);
        ChunkPosition fromLocation = new ChunkPosition(new Location(world, x << 4, 1, z << 4));
        Assertions.assertEquals(chunkPosition, fromLocation);
        Assertions.assertEquals(chunkPosition.hashCode(), fromLocation.hashCode());
        Assertions.assertEquals(chunkPosition, new ChunkPosition(world.getChunkAt(x, z)));
        Assertions.assertEquals(chunkPosition.getPosition(), ChunkPosition.getAsLong(x, z));
        Assertions.assertEquals(chunkPosition, new ChunkPosition(world, chunkPosition.getPosition()));
    }

    @Test
    void testInvalidWorld() {
        World world = new WorldMock();
        ChunkPosition position = new ChunkPosition(world, 1, 1);
        ChunkPosition copy = new ChunkPosition(world, 1, 1);
        world = null;
        // Force garbage collection
        System.gc();
        String msg = "The reference of this ChunkPositions world has been cleared";
        Assertions.assertThrows(IllegalStateException.class, position::getWorld, msg);
        Assertions.assertThrows(IllegalStateException.class, position::getChunk, msg);
        // If the world is un-loaded the equals method should always return false.
        Assertions.assertNotEquals(position, copy);
    }

    @Test
    void testInvalidEquality() {
        World world = new WorldMock();
        Assertions.assertNotEquals(1, new ChunkPosition(world, 1, 1));
    }

    @Test
    void testToString() {
        String name = "world";
        WorldMock world = new WorldMock();
        world.setName(name);
        ChunkPosition position = new ChunkPosition(world, 1, 1);
        String expected = "ChunkPosition(world=world, x=1, z=1, position=" + position.getPosition() + ")";
        Assertions.assertEquals(expected, position.toString());
        String expectedNoRef = "ChunkPosition(world=<no reference>, x=1, z=1, position=" + position.getPosition() + ")";
        world = null;
        // Force GC
        System.gc();
        Assertions.assertEquals(expectedNoRef, position.toString());
    }

    @Test
    void testIsLoaded() {
        World world = new WorldMock();
        Chunk chunk = world.getChunkAt(1, 1);
        ChunkPosition position = new ChunkPosition(chunk);
        Assertions.assertEquals(chunk.isLoaded(), position.isLoaded());
        chunk = null;
        world = null;
        // Force GC
        System.gc();
        Assertions.assertFalse(position.isLoaded());
    }


}
