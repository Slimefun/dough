package io.github.thebusybiscuit.cscorelib2.materials;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Tag;

import lombok.Getter;

/**
 * This utility class can be used to get {@link Material} Arrays of some Material groups.
 * Mainly separated by the class {@link Tag}
 * 
 * @author TheBusyBiscuit
 *
 */
public final class MaterialCollections {
	
	// This is a pure Utility class, we do not want any instantiation to happen!
	private MaterialCollections() {}

	@Getter private static final MaterialCollection allLeaves;
	@Getter private static final MaterialCollection allSaplings;
	@Getter private static final MaterialCollection allLogs;
	@Getter private static final MaterialCollection allPlanks;
	@Getter private static final MaterialCollection allCarpets;
	@Getter private static final MaterialCollection allFishItems;
	@Getter private static final MaterialCollection allOres;
	@Getter private static final MaterialCollection allHeads;
	@Getter private static final MaterialCollection allFilledBuckets;
	@Getter private static final MaterialCollection allPressurePlates;
		
	static {
		Set<Material> leaves = new HashSet<>();
		Set<Material> saplings = new HashSet<>();
		Set<Material> logs = new HashSet<>();
		Set<Material> planks = new HashSet<>();
		Set<Material> carpets = new HashSet<>();
		
		Set<Material> fishes = new HashSet<>();
		Set<Material> ores = new HashSet<>();
		Set<Material> heads = new HashSet<>();
		Set<Material> buckets = new HashSet<>();
		Set<Material> plates = new HashSet<>();
			
		for (Material mat : Material.values()) {
			if (mat.name().startsWith("LEGACY_")) continue;
			
			if (Tag.LEAVES.isTagged(mat)) leaves.add(mat);
			if (Tag.SAPLINGS.isTagged(mat)) saplings.add(mat);
			if (Tag.LOGS.isTagged(mat)) logs.add(mat);
			if (Tag.PLANKS.isTagged(mat)) planks.add(mat);
			if (Tag.CARPETS.isTagged(mat)) carpets.add(mat);
			
			if (Tag.ITEMS_FISHES.isTagged(mat)) fishes.add(mat);
			if (mat.name().endsWith("_ORE")) ores.add(mat);
			if (mat.name().endsWith("_HEAD") || mat.name().endsWith("_SKULL")) heads.add(mat);
			if (mat.name().endsWith("_BUCKET")) buckets.add(mat);
			if (mat.name().endsWith("_PRESSURE_PLATE")) plates.add(mat);
		}
			
		allLeaves = new MaterialCollection(leaves);
		allSaplings = new MaterialCollection(saplings);
		allLogs = new MaterialCollection(logs);
		allPlanks = new MaterialCollection(planks);
		allCarpets = new MaterialCollection(carpets);
		
		allFishItems = new MaterialCollection(fishes);
		allOres = new MaterialCollection(ores);
		allHeads = new MaterialCollection(heads);
		allFilledBuckets = new MaterialCollection(buckets);
		allPressurePlates = new MaterialCollection(plates);
	}

	@Getter
	private static final MaterialCollection allWoolColors = new MaterialCollection(
            Material.WHITE_WOOL,
            Material.ORANGE_WOOL,
            Material.MAGENTA_WOOL,
            Material.LIGHT_BLUE_WOOL,
            Material.YELLOW_WOOL,
            Material.LIME_WOOL,
            Material.PINK_WOOL,
            Material.GRAY_WOOL,
            Material.LIGHT_GRAY_WOOL,
            Material.CYAN_WOOL,
            Material.PURPLE_WOOL,
            Material.BLUE_WOOL,
            Material.BROWN_WOOL,
            Material.GREEN_WOOL,
            Material.RED_WOOL,
            Material.BLACK_WOOL
    );

	@Getter
	private static final MaterialCollection allStainedGlassColors = new MaterialCollection(
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
    );

	@Getter
	private static final MaterialCollection allStainedGlassPaneColors = new MaterialCollection(
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
    );

	@Getter
	private static final MaterialCollection allTerracottaColors = new MaterialCollection(
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
    );

	@Getter
	private static final MaterialCollection allGlazedTerracottaColors = new MaterialCollection(
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
    );

	@Getter
	private static final MaterialCollection allUnbreakableBlocks = new MaterialCollection(
            Material.BEDROCK,
            Material.BARRIER,
            
            Material.NETHER_PORTAL,
            Material.END_PORTAL,
            Material.END_PORTAL_FRAME,
            Material.END_GATEWAY,
            
            Material.COMMAND_BLOCK,
            Material.CHAIN_COMMAND_BLOCK,
            Material.REPEATING_COMMAND_BLOCK,
            
            Material.STRUCTURE_BLOCK,
            Material.STRUCTURE_VOID
    );

	@Getter
	private static final MaterialCollection allAirBlocks = new MaterialCollection(
            Material.AIR,
            Material.CAVE_AIR,
            Material.VOID_AIR
    );

}
