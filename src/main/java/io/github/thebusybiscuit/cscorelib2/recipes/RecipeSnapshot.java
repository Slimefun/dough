package io.github.thebusybiscuit.cscorelib2.recipes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.plugin.Plugin;

public class RecipeSnapshot {
	
	private final Map<Class<? extends Recipe>, Set<Recipe>> recipes = new HashMap<>();
	
	public RecipeSnapshot(Plugin plugin) {
		Iterator<Recipe> iterator = plugin.getServer().recipeIterator();
		
		plugin.getLogger().log(Level.INFO, "Collecting Snapshots of all Recipes...");
		
		while (iterator.hasNext()) {
			Recipe recipe = iterator.next();
			Set<Recipe> set = recipes.computeIfAbsent(recipe.getClass(), key -> new LinkedHashSet<>());
			set.add(recipe);
		}
		
		plugin.getLogger().log(Level.INFO, "Found {0} Recipes!", recipes.entrySet().stream().mapToInt(entry -> entry.getValue().size()).sum());
	}
	
	public <T extends Recipe> Set<T> getRecipes(Class<T> recipeClass) {
		return stream(recipeClass).collect(Collectors.toSet());
	}
	
	public <T extends Recipe> Stream<T> stream(Class<T> recipeClass) {
		return recipes.entrySet().stream()
				.filter(entry -> recipeClass.isAssignableFrom(entry.getKey()))
				.flatMap(entry -> entry.getValue().stream())
				.map(recipeClass::cast);
	}
	
	public <T extends Recipe> RecipeChoice[] getRecipeInput(MinecraftRecipe<T> recipeType, T recipe) {
		return recipeType.getInputs(recipe);
	}
	
	public <T extends Recipe> Optional<ItemStack> getRecipeOutput(MinecraftRecipe<T> recipeType, ItemStack... inputs) {
		if (recipeType.validate(inputs)) {
			return recipeType.getOutput(stream(recipeType.getRecipeClass()), inputs);
		}
		else {
			return Optional.empty();
		}
	}

}
