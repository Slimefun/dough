package io.github.bakedlibs.dough.blocks;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

// TODO: Refactor this
public final class Vein {

    private static final BlockFace[] faces = new BlockFace[] { BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST };

    private Vein() {}

    /**
     * This method gives you a List of all Blocks
     * that are directly or indirectly connected to the given Block
     * and share the same Material as the given Block.
     * 
     * @param b
     *            The Block to start with
     * @param limit
     *            The max amount of Blocks to expand into
     * 
     * @return A List of all Blocks
     */
    public static List<Block> find(Block b, int limit) {
        return find(b, limit, block -> block.getType() == b.getType());
    }

    /**
     * This method gives you a List of all Blocks
     * that are directly or indirectly connected to the given Block
     * and pass the given Predicate.
     * 
     * @param b
     *            The Block to start with
     * @param limit
     *            The max amount of Blocks to expand into
     * @param predicate
     *            A Predicate describing what Blocks to count
     * 
     * @return A List of all Blocks
     */
    public static List<Block> find(Block b, int limit, Predicate<Block> predicate) {
        List<Block> list = new LinkedList<>();
        expand(b, list, limit, predicate);
        return list;
    }

    private static void expand(Block anchor, List<Block> list, int limit, Predicate<Block> predicate) {
        if (list.size() >= limit) {
            return;
        }

        list.add(anchor);

        for (BlockFace face : faces) {
            Block next = anchor.getRelative(face);

            if (!list.contains(next) && predicate.test(next)) {
                expand(next, list, limit, predicate);
            }
        }
    }

}
