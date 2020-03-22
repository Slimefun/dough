package io.github.thebusybiscuit.cscorelib2.materials;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Tag;

import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;
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
	@Getter private static final MaterialCollection allFishItems;
	
	@Getter private static final MaterialCollection allLogs;
	@Getter private static final MaterialCollection allPlanks;
	@Getter private static final MaterialCollection allWoodenSlabs;
	@Getter private static final MaterialCollection allWoodenFences;
	@Getter private static final MaterialCollection allWoodenDoors;
	@Getter private static final MaterialCollection allWoodenTrapdoors;
	@Getter private static final MaterialCollection allWoodenStairs;
	@Getter private static final MaterialCollection allWoodenButtons;
	@Getter private static final MaterialCollection allWoodenPressurePlates;
	
	@Getter private static final MaterialCollection allOres;
	@Getter private static final MaterialCollection allHeads;
	@Getter private static final MaterialCollection allFilledBuckets;
	@Getter private static final MaterialCollection allPressurePlates;
	@Getter private static final MaterialCollection allFuelItems;
		
	static {
		Set<Material> ores = new HashSet<>();
		Set<Material> heads = new HashSet<>();
		Set<Material> buckets = new HashSet<>();
		Set<Material> plates = new HashSet<>();
		Set<Material> fuel = new HashSet<>();
			
		for (Material mat : Material.values()) {
			if (mat.name().startsWith("LEGACY_")) continue;
			
			if (mat.name().endsWith("_ORE")) ores.add(mat);
			if (mat.name().endsWith("_HEAD") || mat.name().endsWith("_SKULL")) heads.add(mat);
			if (mat.name().endsWith("_BUCKET")) buckets.add(mat);
			if (mat.name().endsWith("_PRESSURE_PLATE")) plates.add(mat);
			
			if (mat.isFuel()) fuel.add(mat);
		}
			
		allLeaves = new MaterialCollection(Tag.LEAVES);
		allSaplings = new MaterialCollection(Tag.SAPLINGS);
		allFishItems = new MaterialCollection(Tag.ITEMS_FISHES);
		
		allLogs = new MaterialCollection(Tag.LOGS);
		allPlanks = new MaterialCollection(Tag.PLANKS);
		allWoodenSlabs = new MaterialCollection(Tag.WOODEN_SLABS);
		
		if (!ReflectionUtils.isVersion("v1_13_")) {
	        allWoodenFences = new MaterialCollection(Tag.WOODEN_FENCES);
		}
		else {
		    allWoodenFences = new MaterialCollection(Material.OAK_FENCE);
		}
		
		allWoodenDoors = new MaterialCollection(Tag.WOODEN_DOORS);
		allWoodenTrapdoors = new MaterialCollection(Tag.WOODEN_TRAPDOORS);
		allWoodenStairs = new MaterialCollection(Tag.WOODEN_STAIRS);
		allWoodenButtons = new MaterialCollection(Tag.WOODEN_BUTTONS);
		allWoodenPressurePlates = new MaterialCollection(Tag.WOODEN_PRESSURE_PLATES);
		
		allOres = new MaterialCollection(ores);
		allHeads = new MaterialCollection(heads);
		allFilledBuckets = new MaterialCollection(buckets);
		allPressurePlates = new MaterialCollection(plates);
		allFuelItems = new MaterialCollection(fuel);
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
	private static final MaterialCollection allCarpetColors = new MaterialCollection(
            Material.WHITE_CARPET,
            Material.ORANGE_CARPET,
            Material.MAGENTA_CARPET,
            Material.LIGHT_BLUE_CARPET,
            Material.YELLOW_CARPET,
            Material.LIME_CARPET,
            Material.PINK_CARPET,
            Material.GRAY_CARPET,
            Material.LIGHT_GRAY_CARPET,
            Material.CYAN_CARPET,
            Material.PURPLE_CARPET,
            Material.BLUE_CARPET,
            Material.BROWN_CARPET,
            Material.GREEN_CARPET,
            Material.RED_CARPET,
            Material.BLACK_CARPET
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
	private static final MaterialCollection allDyeColors = new MaterialCollection(
            ReflectionUtils.isVersion("v1_13_") ? Material.BONE_MEAL : Material.WHITE_DYE,
            Material.ORANGE_DYE,
            Material.MAGENTA_DYE,
            Material.LIGHT_BLUE_DYE,
            ReflectionUtils.isVersion("v1_13_") ? Material.valueOf("DANDELION_YELLOW") : Material.YELLOW_DYE,
            Material.LIME_DYE,
            Material.PINK_DYE,
            Material.GRAY_DYE,
            Material.LIGHT_GRAY_DYE,
            Material.CYAN_DYE,
            Material.PURPLE_DYE,
            ReflectionUtils.isVersion("v1_13_") ? Material.LAPIS_LAZULI : Material.BLUE_DYE,
            ReflectionUtils.isVersion("v1_13_") ? Material.COCOA_BEANS : Material.BROWN_DYE,
            ReflectionUtils.isVersion("v1_13_") ? Material.valueOf("CACTUS_GREEN") : Material.GREEN_DYE,
            ReflectionUtils.isVersion("v1_13_") ? Material.valueOf("ROSE_RED") : Material.RED_DYE,
            ReflectionUtils.isVersion("v1_13_") ? Material.INK_SAC : Material.BLACK_DYE
    );

	@Getter
	private static final MaterialCollection allConcreteColors = new MaterialCollection(
            Material.WHITE_CONCRETE,
            Material.ORANGE_CONCRETE,
            Material.MAGENTA_CONCRETE,
            Material.LIGHT_BLUE_CONCRETE,
            Material.YELLOW_CONCRETE,
            Material.LIME_CONCRETE,
            Material.PINK_CONCRETE,
            Material.GRAY_CONCRETE,
            Material.LIGHT_GRAY_CONCRETE,
            Material.CYAN_CONCRETE,
            Material.PURPLE_CONCRETE,
            Material.BLUE_CONCRETE,
            Material.BROWN_CONCRETE,
            Material.GREEN_CONCRETE,
            Material.RED_CONCRETE,
            Material.BLACK_CONCRETE
    );

	@Getter
	private static final MaterialCollection allConcretePowderColors = new MaterialCollection(
            Material.WHITE_CONCRETE_POWDER,
            Material.ORANGE_CONCRETE_POWDER,
            Material.MAGENTA_CONCRETE_POWDER,
            Material.LIGHT_BLUE_CONCRETE_POWDER,
            Material.YELLOW_CONCRETE_POWDER,
            Material.LIME_CONCRETE_POWDER,
            Material.PINK_CONCRETE_POWDER,
            Material.GRAY_CONCRETE_POWDER,
            Material.LIGHT_GRAY_CONCRETE_POWDER,
            Material.CYAN_CONCRETE_POWDER,
            Material.PURPLE_CONCRETE_POWDER,
            Material.BLUE_CONCRETE_POWDER,
            Material.BROWN_CONCRETE_POWDER,
            Material.GREEN_CONCRETE_POWDER,
            Material.RED_CONCRETE_POWDER,
            Material.BLACK_CONCRETE_POWDER
    );

	@Getter
	private static final MaterialCollection allShulkerBoxColors = new MaterialCollection(
            Material.WHITE_SHULKER_BOX,
            Material.ORANGE_SHULKER_BOX,
            Material.MAGENTA_SHULKER_BOX,
            Material.LIGHT_BLUE_SHULKER_BOX,
            Material.YELLOW_SHULKER_BOX,
            Material.LIME_SHULKER_BOX,
            Material.PINK_SHULKER_BOX,
            Material.GRAY_SHULKER_BOX,
            Material.LIGHT_GRAY_SHULKER_BOX,
            Material.CYAN_SHULKER_BOX,
            Material.PURPLE_SHULKER_BOX,
            Material.BLUE_SHULKER_BOX,
            Material.BROWN_SHULKER_BOX,
            Material.GREEN_SHULKER_BOX,
            Material.RED_SHULKER_BOX,
            Material.BLACK_SHULKER_BOX
    );

	@Getter
	private static final MaterialCollection allBedColors = new MaterialCollection(
            Material.WHITE_BED,
            Material.ORANGE_BED,
            Material.MAGENTA_BED,
            Material.LIGHT_BLUE_BED,
            Material.YELLOW_BED,
            Material.LIME_BED,
            Material.PINK_BED,
            Material.GRAY_BED,
            Material.LIGHT_GRAY_BED,
            Material.CYAN_BED,
            Material.PURPLE_BED,
            Material.BLUE_BED,
            Material.BROWN_BED,
            Material.GREEN_BED,
            Material.RED_BED,
            Material.BLACK_BED
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

	@Getter
	private static final MaterialCollection allIceBlocks = new MaterialCollection(
            Material.ICE,
            Material.PACKED_ICE,
            Material.FROSTED_ICE,
            Material.BLUE_ICE
    );

}
