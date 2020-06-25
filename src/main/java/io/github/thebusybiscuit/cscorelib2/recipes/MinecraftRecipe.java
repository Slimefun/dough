package io.github.thebusybiscuit.cscorelib2.recipes;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.inventory.StonecuttingRecipe;

import lombok.Getter;
import lombok.NonNull;

public class MinecraftRecipe<T extends Recipe> {

    private static final Set<MinecraftRecipe<?>> recipeTypes = new HashSet<>();

    public static final MinecraftRecipe<ShapedRecipe> SHAPED_CRAFTING = new MinecraftRecipe<>("CRAFTING_TABLE", () -> ShapedRecipe.class, recipe -> recipe.length > 0 && recipe.length < 10, recipe -> {
        List<RecipeChoice> choices = new LinkedList<>();

        for (String row : recipe.getShape()) {
            for (char key : row.toCharArray()) {
                choices.add(recipe.getChoiceMap().get(key));
            }
        }

        return choices.toArray(new RecipeChoice[0]);
    }, (input, stream) -> stream.filter(recipe -> {
        int i = 0;

        for (String row : recipe.getShape()) {
            for (char key : row.toCharArray()) {
                if (i > input.length) return false;

                RecipeChoice choice = recipe.getChoiceMap().get(key);
                if (choice != null && !choice.test(input[i])) {
                    return false;
                }

                i++;
            }
        }

        return true;
    }).findAny().map(ShapedRecipe::getResult));

    public static final MinecraftRecipe<ShapelessRecipe> SHAPELESS_CRAFTING = new MinecraftRecipe<>("CRAFTING_TABLE", () -> ShapelessRecipe.class, recipe -> recipe.length > 0 && recipe.length < 10, recipe -> recipe.getChoiceList().toArray(new RecipeChoice[0]), (input, stream) -> stream.filter(recipe -> {
        for (RecipeChoice ingredient : recipe.getChoiceList()) {
            boolean found = false;

            ItemStack[] inputs = input.clone();
            for (int i = 0; i < inputs.length; i++) {
                if (inputs[i] != null && ingredient.test(inputs[i])) {
                    inputs[i] = null;
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }
        }

        return true;
    }).findAny().map(ShapelessRecipe::getResult));

    public static final MinecraftRecipe<FurnaceRecipe> FURNACE = new MinecraftRecipe<>("FURNACE", () -> FurnaceRecipe.class, recipe -> recipe.length == 1, recipe -> new RecipeChoice[] { recipe.getInputChoice() }, (input, stream) -> stream.filter(recipe -> recipe.getInputChoice().test(input[0])).findAny().map(FurnaceRecipe::getResult));

    public static final MinecraftRecipe<BlastingRecipe> BLAST_FURNACE = new MinecraftRecipe<>("BLAST_FURNACE", () -> BlastingRecipe.class, recipe -> recipe.length == 1, recipe -> new RecipeChoice[] { recipe.getInputChoice() }, (input, stream) -> stream.filter(recipe -> recipe.getInputChoice().test(input[0])).findAny().map(CookingRecipe::getResult));

    public static final MinecraftRecipe<SmokingRecipe> SMOKER = new MinecraftRecipe<>("SMOKER", () -> SmokingRecipe.class, recipe -> recipe.length == 1, recipe -> new RecipeChoice[] { recipe.getInputChoice() }, (input, stream) -> stream.filter(recipe -> recipe.getInputChoice().test(input[0])).findAny().map(CookingRecipe::getResult));

    public static final MinecraftRecipe<CampfireRecipe> CAMPFIRE = new MinecraftRecipe<>("CAMPFIRE", () -> CampfireRecipe.class, recipe -> recipe.length == 1, recipe -> new RecipeChoice[] { recipe.getInputChoice() }, (input, stream) -> stream.filter(recipe -> recipe.getInputChoice().test(input[0])).findAny().map(CookingRecipe::getResult));

    public static final MinecraftRecipe<StonecuttingRecipe> STONECUTTER = new MinecraftRecipe<>("STONECUTTER", () -> StonecuttingRecipe.class, recipe -> recipe.length == 1, recipe -> new RecipeChoice[] { recipe.getInputChoice() }, (input, stream) -> stream.filter(recipe -> recipe.getInputChoice().test(input[0])).findAny().map(StonecuttingRecipe::getResult));

    public static final MinecraftRecipe<SmithingRecipe> SMITHING = new MinecraftRecipe<>("STONECUTTER", () -> SmithingRecipe.class, recipe -> recipe.length == 2, recipe -> new RecipeChoice[] { recipe.getBase(), recipe.getAddition() }, (input, stream) -> stream.filter(recipe -> recipe.getBase().test(input[0]) && recipe.getAddition().test(input[1])).findAny().map(SmithingRecipe::getResult));

    @Getter
    private Material machine;

    @Getter
    private Class<T> recipeClass;

    private Predicate<ItemStack[]> predicate;
    private Function<T, RecipeChoice[]> inputFunction;
    private BiFunction<ItemStack[], Stream<T>, Optional<ItemStack>> outputFunction;

    private MinecraftRecipe(String material, Supplier<Class<T>> recipeClass, Predicate<ItemStack[]> predicate, Function<T, RecipeChoice[]> inputFunction, BiFunction<ItemStack[], Stream<T>, Optional<ItemStack>> outputFunction) {
        try {
            this.machine = Material.valueOf(material);
            this.recipeClass = recipeClass.get();
            this.predicate = predicate;
            this.inputFunction = inputFunction;
            this.outputFunction = outputFunction;

            recipeTypes.add(this);
        }
        catch (Exception | LinkageError x) {
            System.err.println("Unable to load a Minecraft Recipe Type: " + material);
        }
    }

    protected boolean validate(ItemStack[] inputs) {
        return predicate.test(inputs);
    }

    public RecipeChoice[] getInputs(T recipe) {
        return inputFunction.apply(recipe);
    }

    public Optional<ItemStack> getOutput(Stream<T> stream, ItemStack[] inputs) {
        return outputFunction.apply(inputs, stream);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Recipe> Optional<MinecraftRecipe<? super T>> of(@NonNull T recipe) {
        Class<?> recipeClass = recipe.getClass();

        return recipeTypes.stream().filter(type -> type.getRecipeClass().isAssignableFrom(recipeClass)).findAny().map(type -> (MinecraftRecipe<? super T>) type);
    }

    public static Stream<MinecraftRecipe<?>> stream() {
        return recipeTypes.stream();
    }

}
