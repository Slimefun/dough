package io.github.thebusybiscuit.cscorelib2.recipes;

import java.util.Arrays;
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

import lombok.NonNull;

public class RecipeSnapshot {
	
	private final Map<Class<? extends Recipe>, Set<Recipe>> recipes = new HashMap<>();
	
	public RecipeSnapshot(@NonNull Plugin plugin) {
		Iterator<Recipe> iterator = plugin.getServer().recipeIterator();
		
		plugin.getLogger().log(Level.INFO, "Collecting Snapshots of all Recipes...");
		
		while (iterator.hasNext()) {
			Recipe recipe = iterator.next();
			Set<Recipe> set = recipes.computeIfAbsent(recipe.getClass(), key -> new LinkedHashSet<>());
			set.add(recipe);
		}
		
		plugin.getLogger().log(Level.INFO, "Found {0} Recipes!", recipes.entrySet().stream().mapToInt(entry -> entry.getValue().size()).sum());
	}
	
	public Stream<Recipe> streamAllRecipes() {
		return recipes.values().stream().flatMap(Set::stream);
	}
	
	public <T extends Recipe> Set<T> getRecipes(@NonNull Class<T> recipeClass) {
		return stream(recipeClass).collect(Collectors.toSet());
	}
	
	public <T extends Recipe> Stream<T> stream(@NonNull Class<T> recipeClass) {
		return recipes.entrySet().stream()
				.filter(entry -> recipeClass.isAssignableFrom(entry.getKey()))
				.flatMap(entry -> entry.getValue().stream())
				.map(recipeClass::cast);
	}
	
	public <T extends Recipe> RecipeChoice[] getRecipeInput(@NonNull MinecraftRecipe<? super T> recipeType, @NonNull T recipe) {
		return recipeType.getInputs(recipe);
	}
	
	public <T extends Recipe> RecipeChoice[] getRecipeInput(@NonNull T recipe) {
		Optional<MinecraftRecipe<? super T>> type = MinecraftRecipe.of(recipe);
		
		if (type.isPresent()) {
			return getRecipeInput(type.get(), recipe);
		}
		else {
			return new RecipeChoice[0];
		}
	}
	
	public <T extends Recipe> Optional<ItemStack> getRecipeOutput(@NonNull MinecraftRecipe<T> recipeType, ItemStack... inputs) {
		if (recipeType.validate(inputs)) {
			return recipeType.getOutput(stream(recipeType.getRecipeClass()), inputs);
		}
		else {
			return Optional.empty();
		}
	}
	
	public Set<Recipe> getRecipes(@NonNull Predicate<Recipe> predicate) {
		return streamAllRecipes().filter(predicate).collect(Collectors.toSet());
	}
	
	public Set<Recipe> getRecipesFor(@NonNull Material type) {
		return getRecipes(recipe -> recipe.getResult().getType() == type);
	}
	
	public Set<Recipe> getRecipesFor(@NonNull ItemStack item) {
		return getRecipes(recipe -> recipe.getResult().isSimilar(item));
	}
	
	public Set<Recipe> getRecipesWith(@NonNull ItemStack item) {
		return getRecipes(recipe -> Arrays.stream(getRecipeInput(recipe))
				.anyMatch(choice -> choice.test(item)));
	}

}
