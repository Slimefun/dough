package io.github.thebusybiscuit.cscorelib2.materials;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Tag;

import lombok.Getter;

/**
 * This utility class can be used to get {@link Material} Arrays that a tool is best at.
 *
 * @author LinoxGH/ajan_12
 * @author TheBusyBiscuit
 *
 */
public final class MaterialTools {

    // This is a pure Utility class, we do not want any instantiation to happen!
    private MaterialTools() {}

    @Getter private static final MaterialCollection breakableByShovel;

    static {
        Set<Material> shovel = new HashSet<>();
        shovel.addAll(Arrays.asList(
        	Material.SNOW, Material.SNOW_BLOCK, Material.FARMLAND, 
        	Material.SOUL_SAND, Material.CLAY, Material.GRAVEL, 
        	Material.GRASS_PATH, Material.DIRT, Material.COARSE_DIRT,
        	Material.GRASS_BLOCK, Material.MYCELIUM
        ));

        for (Material mat : Material.values()) {
            if (Tag.SAND.isTagged(mat)) shovel.add(mat);
        }

        breakableByShovel = new MaterialCollection(shovel);
    }

}
