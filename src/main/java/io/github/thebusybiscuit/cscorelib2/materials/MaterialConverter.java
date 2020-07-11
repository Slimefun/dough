package io.github.thebusybiscuit.cscorelib2.materials;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.bukkit.Material;

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
	    
		return convert(log, 
			MaterialCollections.getAllLogs()::contains,
			type -> type.substring(0, type.lastIndexOf('_')).replace("STRIPPED_", "") + "_SAPLING"
		);
	}
	
	public static Optional<Material> getSaplingFromLeaves(@NonNull Material leaves) {
		return convert(leaves, 
			MaterialCollections.getAllLeaves()::contains,
			type -> type.replace("_LEAVES", "_SAPLING")
		);
	}
	
	public static Optional<Material> getPlanksFromLog(@NonNull Material log) {
		return convert(log, 
			MaterialCollections.getAllLogs()::contains,
			type -> type.substring(0, type.lastIndexOf('_')).replace("STRIPPED_", "") + "_PLANKS"
		);
	}
	
	public static Optional<Material> getStrippedFromLog(@NonNull Material log) {
		return convert(log, 
			type -> MaterialCollections.getAllLogs().contains(type) && !type.name().startsWith("STRIPPED_"),
			type -> "STRIPPED_" + type
		);
	}
	
	public static Optional<Material> getGlassFromPane(@NonNull Material pane) {
		return convert(pane, 
			MaterialCollections.getAllStainedGlassPaneColors()::contains,
			type -> type.substring(0, type.length() - "_PANE".length())
		);
	}
	
	public static Optional<Material> getPaneFromGlass(@NonNull Material glass) {
		return convert(glass, 
			MaterialCollections.getAllStainedGlassColors()::contains,
			type -> type + "_PANE"
		);
	}
	
	public static Optional<Material> getWoolFromDye(@NonNull Material dye) {
		return convert(dye, 
			MaterialCollections.getAllDyeColors()::contains,
			type -> type.replace("_DYE", "_WOOL")
		);
	}
	
	public static Optional<Material> getCarpetFromDye(@NonNull Material dye) {
		return convert(dye, 
			MaterialCollections.getAllDyeColors()::contains,
			type -> type.replace("_DYE", "_CARPET")
		);
	}
	
	public static Optional<Material> getStainedGlassFromDye(@NonNull Material dye) {
		return convert(dye, 
			MaterialCollections.getAllDyeColors()::contains,
			type -> type.replace("_DYE", "_STAINED_GLASS")
		);
	}
	
	public static Optional<Material> getStainedGlassPaneFromDye(@NonNull Material dye) {
		return convert(dye, 
			MaterialCollections.getAllDyeColors()::contains,
			type -> type.replace("_DYE", "_STAINED_GLASS_PANE")
		);
	}
	
	private static Optional<Material> convert(Material type, Predicate<Material> predicate, UnaryOperator<String> converter) {
		if (!predicate.test(type)) {
			return Optional.empty();
		}
		
		return Optional.ofNullable(Material.getMaterial(converter.apply(type.name())));
	}
	
}
