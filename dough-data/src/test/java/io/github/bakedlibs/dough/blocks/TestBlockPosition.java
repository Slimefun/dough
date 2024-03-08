package io.github.bakedlibs.dough.blocks;

import be.seeseemelk.mockbukkit.WorldMock;
import org.bukkit.World;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestBlockPosition {

    @Test
    void testBlockPositions() {
        World world = new WorldMock();
        int x = 57123, y = 286, z = 862;
        BlockPosition bp = new BlockPosition(world, x, y, z);

        Assertions.assertEquals(world, bp.getWorld());
        Assertions.assertEquals(x, bp.getX());
        Assertions.assertEquals(y, bp.getY());
        Assertions.assertEquals(z, bp.getZ());
    }

    @Test
    void testNegativeBlockPositions() {
        World world = new WorldMock();
        int x = -57123, y = -38, z = -862;
        BlockPosition bp = new BlockPosition(world, x, y, z);

        Assertions.assertEquals(x, bp.getX());
        Assertions.assertEquals(y, bp.getY());
        Assertions.assertEquals(z, bp.getZ());
    }

}
