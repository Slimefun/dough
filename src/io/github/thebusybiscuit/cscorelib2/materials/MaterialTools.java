package io.github.thebusybiscuit.cscorelib2.materials;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Tag;

import lombok.Getter;

/**
 * This utility class can be used to get {@link Material} Arrays that a tool is best at.
 *
 * @author LinoxGH/ajan_12
 *
 */
public final class MaterialTools {

    // This is a pure Utility class, we do not want any instantiation to happen!
    private MaterialTools() {}

    @Getter
    private static final Material[]
            shovelItems
    ;

    static {
        Set<Material> shovel = new HashSet<>();

        for (Material mat: Material.values()) {
            if (Tag.DIRT_LIKE.isTagged(mat) || Tag.SAND.isTagged(mat)) shovel.add(mat);
            // Checking for the materials not included in tags.
            if (mat.name().contains("SNOW") || mat == Material.FARMLAND || mat == Material.SOUL_SAND ||
                    mat == Material.CLAY || mat == Material.GRAVEL || mat == Material.GRASS_PATH) shovel.add(mat);
        }

        shovelItems = shovel.toArray(new Material[0]);
    }

}
