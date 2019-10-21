package io.github.thebusybiscuit.cscorelib2.recipes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class RecipeSnapshot {
	
	private final Map<Class<? extends Recipe>, Set<Recipe>> recipes = new HashMap<>();
	
	public RecipeSnapshot(@NotNull Plugin plugin) {
		Iterator<Recipe> iterator = plugin.getServer().recipeIterator();
		
		plugin.getLogger().log(Level.INFO, "Collecting Snapshots of all Recipes...");
		
		while (iterator.hasNext()) {
			Recipe recipe = iterator.next();
			Set<Recipe> set = recipes.computeIfAbsent(recipe.getClass(), key -> new LinkedHashSet<>());
			set.add(recipe);
		}
		
		plugin.getLogger().log(Level.INFO, "Found {0} Recipes!", recipes.entrySet().stream().mapToInt(entry -> entry.getValue().size()).sum());
	}
	
	@NotNull
	public Stream<Recipe> streamAllRecipes() {
		return recipes.values().stream().flatMap(Set::stream);
	}
	
	@NotNull
	public <T extends Recipe> Set<T> getRecipes(@NotNull Class<T> recipeClass) {
		return stream(recipeClass).collect(Collectors.toSet());
	}
	
	@NotNull
	public <T extends Recipe> Stream<T> stream(@NotNull Class<T> recipeClass) {
		return recipes.entrySet().stream()
				.filter(entry -> recipeClass.isAssignableFrom(entry.getKey()))
				.flatMap(entry -> entry.getValue().stream())
				.map(recipeClass::cast);
	}
	
	@NotNull
	public <T extends Recipe> RecipeChoice[] getRecipeInput(@NotNull MinecraftRecipe<T> recipeType, @NotNull T recipe) {
		return recipeType.getInputs(recipe);
	}
	
	@NotNull
	public <T extends Recipe> Optional<ItemStack> getRecipeOutput(@NotNull MinecraftRecipe<T> recipeType, ItemStack... inputs) {
		if (recipeType.validate(inputs)) {
			return recipeType.getOutput(stream(recipeType.getRecipeClass()), inputs);
		}
		else {
			return Optional.empty();
		}
	}
	
	@NotNull
	public Set<Recipe> getRecipes(@NotNull Predicate<Recipe> predicate) {
		return streamAllRecipes().filter(predicate).collect(Collectors.toSet());
	}
	
	@NotNull
	public Set<Recipe> getRecipesFor(@NotNull Material type) {
		return getRecipes(recipe -> recipe.getResult().getType() == type);
	}
	
	@NotNull
	public Set<Recipe> getRecipesFor(@NotNull ItemStack item) {
		return getRecipes(recipe -> recipe.getResult().getType() == type);
	}

}
