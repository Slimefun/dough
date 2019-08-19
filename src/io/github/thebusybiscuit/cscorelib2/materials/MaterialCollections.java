package io.github.thebusybiscuit.cscorelib2.materials;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Tag;

import lombok.Getter;

/**
 * This utility class can be used to get {@link Material} Arrays of some Material groups.
 * Mainly seperated by the class {@link Tag}
 * 
 * @author TheBusyBiscuit
 *
 */
public final class MaterialCollections {
	
	// This is a pure Utility class, we do not want any instantiation to happen!
	private MaterialCollections() {}

	@Getter
	private static final Material[] 
		allLeaves, 
		allSaplings, 
		allLogs, 
		allPlanks, 
		allWools, 
		allCarpets,
		allFishItems
	;
		
	static {
		Set<Material> leaves = new HashSet<>();
		Set<Material> saplings = new HashSet<>();
		Set<Material> logs = new HashSet<>();
		Set<Material> planks = new HashSet<>();
		Set<Material> wools = new HashSet<>();
		Set<Material> carpets = new HashSet<>();
		
		Set<Material> fishes = new HashSet<>();
			
		for (Material mat: Material.values()) {
			if (Tag.LEAVES.isTagged(mat)) leaves.add(mat);
			if (Tag.SAPLINGS.isTagged(mat)) saplings.add(mat);
			if (Tag.LOGS.isTagged(mat)) logs.add(mat);
			if (Tag.PLANKS.isTagged(mat)) planks.add(mat);
			if (Tag.WOOL.isTagged(mat)) wools.add(mat);
			if (Tag.CARPETS.isTagged(mat)) carpets.add(mat);
			
			if (Tag.ITEMS_FISHES.isTagged(mat)) fishes.add(mat);
		}
			
		allLeaves = leaves.toArray(new Material[leaves.size()]);
		allSaplings = leaves.toArray(new Material[saplings.size()]);
		allLogs = leaves.toArray(new Material[logs.size()]);
		allPlanks = leaves.toArray(new Material[planks.size()]);
		allWools = leaves.toArray(new Material[wools.size()]);
		allCarpets = leaves.toArray(new Material[carpets.size()]);
		
		allFishItems = fishes.toArray(new Material[fishes.size()]);
	}

	@Getter
	private static final Material[] allStainedGlassColors = {
            Material.WHITE_STAINED_GLASS,
            Material.ORANGE_STAINED_GLASS,
            Material.MAGENTA_STAINED_GLASS,
            Material.LIGHT_BLUE_STAINED_GLASS,
            Material.YELLOW_STAINED_GLASS,
            Material.LIME_STAINED_GLASS,
            Material.PINK_STAINED_GLASS,
            Material.GRAY_STAINED_GLASS,
            Material.LIGHT_GRAY_STAINED_GLASS,
            Material.CYAN_STAINED_GLASS,
            Material.PURPLE_STAINED_GLASS,
            Material.BLUE_STAINED_GLASS,
            Material.BROWN_STAINED_GLASS,
            Material.GREEN_STAINED_GLASS,
            Material.RED_STAINED_GLASS,
            Material.BLACK_STAINED_GLASS
    };

	@Getter
	private static final Material[] allStainedGlassPaneColors = {
            Material.WHITE_STAINED_GLASS_PANE,
            Material.ORANGE_STAINED_GLASS_PANE,
            Material.MAGENTA_STAINED_GLASS_PANE,
            Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            Material.YELLOW_STAINED_GLASS_PANE,
            Material.LIME_STAINED_GLASS_PANE,
            Material.PINK_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE,
            Material.LIGHT_GRAY_STAINED_GLASS_PANE,
            Material.CYAN_STAINED_GLASS_PANE,
            Material.PURPLE_STAINED_GLASS_PANE,
            Material.BLUE_STAINED_GLASS_PANE,
            Material.BROWN_STAINED_GLASS_PANE,
            Material.GREEN_STAINED_GLASS_PANE,
            Material.RED_STAINED_GLASS_PANE,
            Material.BLACK_STAINED_GLASS_PANE
    };

	@Getter
	private static final Material[] allTerracottaColors = {
            Material.WHITE_TERRACOTTA,
            Material.ORANGE_TERRACOTTA,
            Material.MAGENTA_TERRACOTTA,
            Material.LIGHT_BLUE_TERRACOTTA,
            Material.YELLOW_TERRACOTTA,
            Material.LIME_TERRACOTTA,
            Material.PINK_TERRACOTTA,
            Material.GRAY_TERRACOTTA,
            Material.LIGHT_GRAY_TERRACOTTA,
            Material.CYAN_TERRACOTTA,
            Material.PURPLE_TERRACOTTA,
            Material.BLUE_TERRACOTTA,
            Material.BROWN_TERRACOTTA,
            Material.GREEN_TERRACOTTA,
            Material.RED_TERRACOTTA,
            Material.BLACK_TERRACOTTA
    };

	@Getter
	private static final Material[] allGlazedTerracottaColors = {
            Material.WHITE_GLAZED_TERRACOTTA,
            Material.ORANGE_GLAZED_TERRACOTTA,
            Material.MAGENTA_GLAZED_TERRACOTTA,
            Material.LIGHT_BLUE_GLAZED_TERRACOTTA,
            Material.YELLOW_GLAZED_TERRACOTTA,
            Material.LIME_GLAZED_TERRACOTTA,
            Material.PINK_GLAZED_TERRACOTTA,
            Material.GRAY_GLAZED_TERRACOTTA,
            Material.LIGHT_GRAY_GLAZED_TERRACOTTA,
            Material.CYAN_GLAZED_TERRACOTTA,
            Material.PURPLE_GLAZED_TERRACOTTA,
            Material.BLUE_GLAZED_TERRACOTTA,
            Material.BROWN_GLAZED_TERRACOTTA,
            Material.GREEN_GLAZED_TERRACOTTA,
            Material.RED_GLAZED_TERRACOTTA,
            Material.BLACK_GLAZED_TERRACOTTA
    };

}
