package io.github.thebusybiscuit.cscorelib2.recipes;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.inventory.StonecuttingRecipe;

import lombok.Getter;

public class MinecraftRecipe<T extends Recipe> {
	
	public static final MinecraftRecipe<ShapedRecipe> SHAPED_CRAFTING = new MinecraftRecipe<>(ShapedRecipe.class, recipe -> recipe.length > 0 && recipe.length < 10, 
		recipe -> {
			// TODO: Implement SHAPED_CRAFTING Recipe (Input Function)
			return null;
		}, (input, stream) -> 
		stream.filter(recipe -> {
			// TODO: Implement SHAPED_CRAFTING Recipe (Output Function)
			return false;
		}).findAny().map(ShapedRecipe::getResult)
	);
	
	public static final MinecraftRecipe<ShapelessRecipe> SHAPELESS_CRAFTING = new MinecraftRecipe<>(ShapelessRecipe.class, recipe -> recipe.length > 0 && recipe.length < 10,
		recipe -> recipe.getChoiceList().toArray(new RecipeChoice[0]), 
		(input, stream) -> stream.filter(recipe -> {
			for (RecipeChoice ingredient: recipe.getChoiceList()) {
				boolean found = false;
				
				for (ItemStack item: input) {
					if (ingredient.test(item)) {
						found = true;
						break;
					}
				}
				
				if (!found) return false;
			}
			
			return true;
		}).findAny().map(ShapelessRecipe::getResult)
	);
	
	public static final MinecraftRecipe<CookingRecipe<?>> ALL_SMELTING = new MinecraftRecipe<>(CookingRecipe.class, recipe -> recipe.length == 1, 
		recipe -> new RecipeChoice[] {recipe.getInputChoice()}, (input, stream) ->
		stream.filter(recipe -> recipe.getInputChoice().test(input[0])).findAny().map(recipe -> recipe.getResult())
	);
	
	public static final MinecraftRecipe<FurnaceRecipe> FURNACE = new MinecraftRecipe<>(FurnaceRecipe.class, recipe -> recipe.length == 1, 
		recipe -> new RecipeChoice[] {recipe.getInputChoice()}, (input, stream) ->
		stream.filter(recipe -> recipe.getInputChoice().test(input[0])).findAny().map(CookingRecipe::getResult)
	);
	
	public static final MinecraftRecipe<BlastingRecipe> BLAST_FURNACE = new MinecraftRecipe<>(BlastingRecipe.class, recipe -> recipe.length == 1, 
		recipe -> new RecipeChoice[] {recipe.getInputChoice()}, (input, stream) ->
		stream.filter(recipe -> recipe.getInputChoice().test(input[0])).findAny().map(CookingRecipe::getResult)
	);
	
	public static final MinecraftRecipe<SmokingRecipe> SMOKER = new MinecraftRecipe<>(SmokingRecipe.class, recipe -> recipe.length == 1, 
		recipe -> new RecipeChoice[] {recipe.getInputChoice()}, (input, stream) ->
		stream.filter(recipe -> recipe.getInputChoice().test(input[0])).findAny().map(CookingRecipe::getResult)
	);
	
	public static final MinecraftRecipe<CampfireRecipe> CAMPFIRE = new MinecraftRecipe<>(CampfireRecipe.class, recipe -> recipe.length == 1, 
		recipe -> new RecipeChoice[] {recipe.getInputChoice()}, (input, stream) ->
		stream.filter(recipe -> recipe.getInputChoice().test(input[0])).findAny().map(CookingRecipe::getResult)
	);
	
	public static final MinecraftRecipe<StonecuttingRecipe> STONECUTTER = new MinecraftRecipe<>(StonecuttingRecipe.class, recipe -> recipe.length == 1, 
		recipe -> new RecipeChoice[] {recipe.getInputChoice()}, (input, stream) ->
		stream.filter(recipe -> recipe.getInputChoice().test(input[0])).findAny().map(StonecuttingRecipe::getResult)
	);

	@Getter
	private final Class<T> recipeClass;
	
	private final Predicate<ItemStack[]> predicate;
	private final Function<T, RecipeChoice[]> inputFunction;
	private final BiFunction<ItemStack[], Stream<T>, Optional<ItemStack>> outputFunction;

	private MinecraftRecipe(Class<T> recipeClass, Predicate<ItemStack[]> predicate, Function<T, RecipeChoice[]> inputFunction, BiFunction<ItemStack[], Stream<T>, Optional<ItemStack>> outputFunction) {
		this.recipeClass = recipeClass;
		this.predicate = predicate;
		this.inputFunction = inputFunction;
		this.outputFunction = outputFunction;
	}
	
	protected boolean validate(ItemStack[] inputs) {
		return predicate.test(inputs);
	}
	
	protected RecipeChoice[] getInputs(T recipe) {
		return inputFunction.apply(recipe);
	}

	protected Optional<ItemStack> getOutput(Stream<T> stream, ItemStack[] inputs) {
		return outputFunction.apply(inputs, stream);
	}

}