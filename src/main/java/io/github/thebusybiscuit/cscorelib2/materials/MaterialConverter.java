package io.github.thebusybiscuit.cscorelib2.materials;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.bukkit.Material;
import org.bukkit.Tag;

import lombok.NonNull;

/**
 * This is a static utility class that contains some methods for converting similar materials.
 * You can use it to get the Sapling from a Leaves {@link Material} or Planks from their Logs and much more.
 * 
 * @author TheBusyBiscuit
 *
 */
public final class MaterialConverter {

    private MaterialConverter() {}

    public static Optional<Material> getSaplingFromLog(@NonNull Material log) {
        if (log.name().equals("CRIMSON_STEM") || log.name().equals("STRIPPED_CRIMSON_STEM")) {
            return Optional.of(Material.CRIMSON_FUNGUS);
        }

        if (log.name().equals("WARPED_STEM") || log.name().equals("STRIPPED_WARPED_STEM")) {
            return Optional.of(Material.WARPED_FUNGUS);
        }

        return convert(log, Tag.LOGS::isTagged, type -> type.substring(0, type.lastIndexOf('_')).replace("STRIPPED_", "") + "_SAPLING");
    }

    public static Optional<Material> getSaplingFromLeaves(@NonNull Material leaves) {
        return convert(leaves, Tag.LEAVES::isTagged, type -> type.replace("_LEAVES", "_SAPLING"));
    }

    public static Optional<Material> getPlanksFromLog(@NonNull Material log) {
        return convert(log, Tag.LOGS::isTagged, type -> type.substring(0, type.lastIndexOf('_')).replace("STRIPPED_", "") + "_PLANKS");
    }

    public static Optional<Material> getStrippedFromLog(@NonNull Material log) {
        return convert(log, type -> Tag.LOGS.isTagged(type) && !type.name().startsWith("STRIPPED_"), type -> "STRIPPED_" + type);
    }

    private static Optional<Material> convert(Material type, Predicate<Material> predicate, UnaryOperator<String> converter) {
        if (!predicate.test(type)) {
            return Optional.empty();
        }

        return Optional.ofNullable(Material.getMaterial(converter.apply(type.name())));
    }

}
