package io.github.bakedlibs.dough.recipes;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.inventory.StonecuttingRecipe;

import io.github.bakedlibs.dough.common.DoughLogger;

public class MinecraftRecipe<T extends Recipe> {

    private static final Set<MinecraftRecipe<?>> recipeTypes = new HashSet<>();

    public static final MinecraftRecipe<ShapedRecipe> SHAPED_CRAFTING;
    public static final MinecraftRecipe<ShapelessRecipe> SHAPELESS_CRAFTING;
    public static final MinecraftRecipe<FurnaceRecipe> FURNACE;
    public static final MinecraftRecipe<BlastingRecipe> BLAST_FURNACE;
    public static final MinecraftRecipe<SmokingRecipe> SMOKER;
    public static final MinecraftRecipe<CampfireRecipe> CAMPFIRE;
    public static final MinecraftRecipe<StonecuttingRecipe> STONECUTTER;
    public static final MinecraftRecipe<SmithingRecipe> SMITHING;

    static {
        DoughLogger logger = new DoughLogger("recipes");

        SHAPED_CRAFTING = findRecipeType(logger, "CRAFTING_TABLE", type -> new MinecraftRecipe<>(type, ShapedRecipe.class, recipe -> recipe.length > 0 && recipe.length < 10, recipe -> {
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
                    if (i > input.length)
                        return false;

                    RecipeChoice choice = recipe.getChoiceMap().get(key);
                    if (choice != null && !choice.test(input[i])) {
                        return false;
                    }

                    i++;
                }
            }

            return true;
        }).findAny().map(ShapedRecipe::getResult)));

        SHAPELESS_CRAFTING = findRecipeType(logger, "CRAFTING_TABLE", type -> new MinecraftRecipe<>(type, ShapelessRecipe.class, recipe -> recipe.length > 0 && recipe.length < 10, recipe -> recipe.getChoiceList().toArray(new RecipeChoice[0]), (input, stream) -> stream.filter(recipe -> {
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
        }).findAny().map(ShapelessRecipe::getResult)));

        FURNACE = findRecipeType(logger, "FURNACE", type -> new MinecraftRecipe<>(type, FurnaceRecipe.class, recipe -> recipe.length == 1, recipe -> new RecipeChoice[] { recipe.getInputChoice() }, (input, stream) -> stream.filter(recipe -> recipe.getInputChoice().test(input[0])).findAny().map(recipe -> recipe.getResult())));

        // 1.14+
        BLAST_FURNACE = findRecipeType(logger, "BLAST_FURNACE", type -> new MinecraftRecipe<>(type, BlastingRecipe.class, recipe -> recipe.length == 1, recipe -> new RecipeChoice[] { recipe.getInputChoice() }, (input, stream) -> stream.filter(recipe -> recipe.getInputChoice().test(input[0])).findAny().map(BlastingRecipe::getResult)));
        SMOKER = findRecipeType(logger, "SMOKER", type -> new MinecraftRecipe<>(type, SmokingRecipe.class, recipe -> recipe.length == 1, recipe -> new RecipeChoice[] { recipe.getInputChoice() }, (input, stream) -> stream.filter(recipe -> recipe.getInputChoice().test(input[0])).findAny().map(SmokingRecipe::getResult)));
        CAMPFIRE = findRecipeType(logger, "CAMPFIRE", type -> new MinecraftRecipe<>(type, CampfireRecipe.class, recipe -> recipe.length == 1, recipe -> new RecipeChoice[] { recipe.getInputChoice() }, (input, stream) -> stream.filter(recipe -> recipe.getInputChoice().test(input[0])).findAny().map(CampfireRecipe::getResult)));
        STONECUTTER = findRecipeType(logger, "STONECUTTER", type -> new MinecraftRecipe<>(type, StonecuttingRecipe.class, recipe -> recipe.length == 1, recipe -> new RecipeChoice[] { recipe.getInputChoice() }, (input, stream) -> stream.filter(recipe -> recipe.getInputChoice().test(input[0])).findAny().map(StonecuttingRecipe::getResult)));
        SMITHING = findRecipeType(logger, "SMITHING_TABLE", type -> new MinecraftRecipe<>(type, SmithingRecipe.class, recipe -> recipe.length == 2, recipe -> new RecipeChoice[] { recipe.getBase(), recipe.getAddition() }, (input, stream) -> stream.filter(recipe -> recipe.getBase().test(input[0]) && recipe.getAddition().test(input[1])).findAny().map(SmithingRecipe::getResult)));
    }

    @ParametersAreNonnullByDefault
    private static <T extends Recipe> @Nullable MinecraftRecipe<T> findRecipeType(Logger logger, String type, Function<String, MinecraftRecipe<T>> supplier) {
        try {
            return supplier.apply(type);
        } catch (Exception | LinkageError x) {
            logger.log(Level.WARNING, "{0} was thrown while trying to access the {1} type. Maybe added in future Minecraft versions?", new Object[] { x.getClass().getSimpleName(), type });
            return null;
        }
    }

    private Material machine;
    private Class<T> recipeClass;

    private Predicate<ItemStack[]> predicate;
    private Function<T, RecipeChoice[]> inputFunction;
    private BiFunction<ItemStack[], Stream<T>, Optional<ItemStack>> outputFunction;

    @ParametersAreNonnullByDefault
    private MinecraftRecipe(String material, Class<T> recipeClass, Predicate<ItemStack[]> predicate, Function<T, RecipeChoice[]> inputFunction, BiFunction<ItemStack[], Stream<T>, Optional<ItemStack>> outputFunction) {
        try {
            this.machine = Material.valueOf(material);
            this.recipeClass = recipeClass;
            this.predicate = predicate;
            this.inputFunction = inputFunction;
            this.outputFunction = outputFunction;

            recipeTypes.add(this);
        } catch (Exception | LinkageError x) {
            System.err.println("Unable to load a Minecraft Recipe Type: " + material);
        }
    }

    protected boolean validate(@Nonnull ItemStack[] inputs) {
        return predicate.test(inputs);
    }

    public @Nonnull Material getMachine() {
        return machine;
    }

    public @Nonnull Class<T> getRecipeClass() {
        return recipeClass;
    }

    public @Nonnull RecipeChoice[] getInputs(@Nonnull T recipe) {
        return inputFunction.apply(recipe);
    }

    public @Nonnull Optional<ItemStack> getOutput(@Nonnull Stream<T> stream, @Nonnull ItemStack[] inputs) {
        return outputFunction.apply(inputs, stream);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Recipe> Optional<MinecraftRecipe<? super T>> of(@Nonnull T recipe) {
        Class<?> recipeClass = recipe.getClass();

        return recipeTypes.stream().filter(type -> type.getRecipeClass().isAssignableFrom(recipeClass)).findAny().map(type -> (MinecraftRecipe<? super T>) type);
    }

    public static @Nonnull Stream<MinecraftRecipe<?>> stream() {
        return recipeTypes.stream();
    }

}
