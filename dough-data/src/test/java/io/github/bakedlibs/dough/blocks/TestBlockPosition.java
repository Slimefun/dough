package io.github.bakedlibs.dough.blocks;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.WorldMock;
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.*;

class TestBlockPosition {

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
        int y = 1000;
        int z = 50;
        World world = new WorldMock();
        BlockPosition blockPosition = new BlockPosition(world, x, y, z);
        Assertions.assertEquals(x, blockPosition.getX());
        Assertions.assertEquals(y, blockPosition.getY());
        Assertions.assertEquals(z, blockPosition.getZ());
    }

    @Test
    void testCloning() {
        int x = 10;
        int y = 1000;
        int z = 50;
        World world = new WorldMock();
        BlockPosition blockPosition = new BlockPosition(world, x, y, z);
        Location location = blockPosition.toLocation();
        Assertions.assertEquals(blockPosition.getPosition(), BlockPosition.getAsLong(location));
        Assertions.assertEquals(blockPosition, new BlockPosition(world, blockPosition.getPosition()));
        Assertions.assertEquals(x, location.getBlockX());
        Assertions.assertEquals(y, location.getBlockY());
        Assertions.assertEquals(z, location.getBlockZ());
        Assertions.assertEquals(blockPosition, new BlockPosition(location));
        Assertions.assertEquals(blockPosition.hashCode(), new BlockPosition(location).hashCode());
        Assertions.assertEquals(x >> 4, blockPosition.getChunkX());
        Assertions.assertEquals(z >> 4, blockPosition.getChunkZ());
    }

    @Test
    void testInvalidWorld() {
        World world = new WorldMock();
        BlockPosition position = new BlockPosition(world, 1, 1, 1);
        BlockPosition copy = new BlockPosition(world, 1, 1, 1);
        world = null;
        // Force garbage collection
        System.gc();
        String msg = "The reference of this BlockPositions world has been cleared";
        Assertions.assertThrows(IllegalStateException.class, position::getWorld, msg);
        Assertions.assertThrows(IllegalStateException.class, position::getBlock, msg);
        Assertions.assertThrows(IllegalStateException.class, position::getChunk, msg);
        // If the world is un-loaded the equals method should always return false.
        Assertions.assertNotEquals(position, copy);
    }

    @Test
    void testInvalidEquality() {
        World world = new WorldMock();
        Assertions.assertNotEquals(1, new BlockPosition(world, 1, 1, 1));
    }

    @Test
    void testToString() {
        String name = "world";
        WorldMock world = new WorldMock();
        world.setName(name);
        BlockPosition position = new BlockPosition(world, 1, 1, 1);
        String expected = "BlockPosition(world=world, x=1, y=1, z=1, position=" + position.getPosition() + ")";
        Assertions.assertEquals(expected, position.toString());
        String expectedNoRef = "BlockPosition(world=<no reference>, x=1, y=1, z=1, position=" + position.getPosition() + ")";
        world = null;
        // Force GC
        System.gc();
        Assertions.assertEquals(expectedNoRef, position.toString());
    }


}
