package io.github.thebusybiscuit.cscorelib2.materials;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.bukkit.Material;

import lombok.NonNull;

public final class MaterialConverter {

	private MaterialConverter() {}
	
	public static Optional<Material> getSaplingFromLog(@NonNull Material log) {
		return convert(log, 
			MaterialCollections.getAllLogs()::contains,
			type -> type.substring(0, type.lastIndexOf('_')).replace("STRIPPED_", "") + "_SAPLING"
		);
	}
	
	public static Optional<Material> getPlanksFromLog(@NonNull Material log) {
		return convert(log, 
			MaterialCollections.getAllLogs()::contains,
			type -> type.substring(0, type.lastIndexOf('_')).replace("STRIPPED_", "") + "_PLANKS"
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
	
	private static Optional<Material> convert(Material type, Predicate<Material> predicate, UnaryOperator<String> converter) {
		if (!predicate.test(type)) {
			return Optional.empty();
		}
		
		return Optional.ofNullable(Material.getMaterial(converter.apply(type.name())));
	}
	
}
