package io.github.bakedlibs.dough.recipes;

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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.plugin.Plugin;

/**
 * This class represents a Snapshot of the Server's registered Recipes.
 * 
 * @author TheBusyBiscuit
 *
 */
public class RecipeSnapshot {

    private final Map<Class<? extends Recipe>, Set<Recipe>> recipes = new HashMap<>();

    private final Map<NamespacedKey, Recipe> keyedRecipes = new HashMap<>();

    /**
     * This will create a Snapshot of all Recipes on the plugin's Server.
     * 
     * @param plugin
     *            The Plugin running on the Server that serves as the Snapshot's source.
     */
    public RecipeSnapshot(@Nonnull Plugin plugin) {
        Iterator<Recipe> iterator = plugin.getServer().recipeIterator();

        plugin.getLogger().log(Level.INFO, "Collecting Snapshots of all Recipes...");

        while (iterator.hasNext()) {
            Recipe recipe = null;
            try {
                recipe = iterator.next();
                Set<Recipe> set = recipes.computeIfAbsent(recipe.getClass(), key -> new LinkedHashSet<>());
                set.add(recipe);

                if (recipe instanceof Keyed) {
                    keyedRecipes.put(((Keyed) recipe).getKey(), recipe);
                }
            } catch (Exception x) {
                plugin.getLogger().log(Level.WARNING, "Skipped a faulty recipe of unknown source ({0}): {1}", new Object[] { x.getClass().getSimpleName(), x.getMessage() });
            }
        }

        plugin.getLogger().log(Level.INFO, "Found {0} Recipes!", recipes.entrySet().stream().mapToInt(entry -> entry.getValue().size()).sum());
    }

    /**
     * This will stream all Recipes stored in this Snapshot.
     * 
     * @return A Stream of all Recipes in this Snapshot
     */
    @Nonnull
    public Stream<Recipe> streamAllRecipes() {
        return recipes.values().stream().flatMap(Set::stream);
    }

    /**
     * This method will return a {@link Set} of Recipes of the given Type
     * contained in this {@link RecipeSnapshot}.
     * 
     * @param <T>
     *            The Type of recipeClass
     * @param recipeClass
     *            A child-class of {@link Recipe}.
     * 
     * @return A {@link Set} of Recipes of the given Type.
     */
    @Nonnull
    public <T extends Recipe> Set<T> getRecipes(@Nonnull Class<T> recipeClass) {
        return stream(recipeClass).collect(Collectors.toSet());
    }

    /**
     * This method will return a {@link Stream} of Recipes of the given Type
     * contained in this {@link RecipeSnapshot}.
     * 
     * @param <T>
     *            The Type of recipeClass
     * @param recipeClass
     *            A child-class of {@link Recipe}.
     * 
     * @return A {@link Stream} of Recipes of the given Type.
     */
    @Nonnull
    public <T extends Recipe> Stream<T> stream(@Nonnull Class<T> recipeClass) {
        return recipes.entrySet().stream().filter(entry -> recipeClass.isAssignableFrom(entry.getKey())).flatMap(entry -> entry.getValue().stream()).map(recipeClass::cast);
    }

    /**
     * This method will return an Array of {@link RecipeChoice} representing
     * the given Recipe's input choices.
     * 
     * @param <T>
     *            The Type of recipe
     * @param recipeType
     *            The Type of the given Recipe
     * @param recipe
     *            The Recipe to get the inputs from
     * 
     * @return The Inputs for the given Recipe
     */
    @Nonnull
    public <T extends Recipe> RecipeChoice[] getRecipeInput(@Nonnull MinecraftRecipe<? super T> recipeType, @Nonnull T recipe) {
        return recipeType.getInputs(recipe);
    }

    /**
     * This method will return an Array of {@link RecipeChoice} representing
     * the given Recipe's input choices.
     * 
     * This will perform a call to {@link MinecraftRecipe#of(Recipe)} to find
     * the given Recipe's Recipe Type.
     * It is advised to prefer the usage of {@link RecipeSnapshot#getRecipeInput(MinecraftRecipe, Recipe)}.
     * 
     * @param <T>
     *            The Type of recipe
     * @param recipe
     *            The Recipe to get the inputs from
     * 
     * @return The Inputs for the given Recipe
     */
    @Nonnull
    public <T extends Recipe> RecipeChoice[] getRecipeInput(@Nonnull T recipe) {
        Optional<MinecraftRecipe<? super T>> type = MinecraftRecipe.of(recipe);

        if (type.isPresent()) {
            return getRecipeInput(type.get(), recipe);
        } else {
            return new RecipeChoice[0];
        }
    }

    /**
     * This method will return an {@link Optional} describing the output of a Recipe
     * with the given type and given inputs.
     * 
     * If no matching recipe was found, an empty {@link Optional} will be returned.
     * 
     * @param <T>
     *            The Type of recipe
     * @param recipeType
     *            The Recipe Type you are looking for
     * @param inputs
     *            The Inputs to the Recipe you are looking for
     * 
     * @return An {@link Optional} describing the output of the Recipe matching your type and inputs
     */
    @Nonnull
    public <T extends Recipe> Optional<ItemStack> getRecipeOutput(@Nonnull MinecraftRecipe<T> recipeType, ItemStack... inputs) {
        if (recipeType.validate(inputs)) {
            return recipeType.getOutput(stream(recipeType.getRecipeClass()), inputs);
        } else {
            return Optional.empty();
        }
    }

    /**
     * This method will return all Recipes matching the given {@link Predicate}.
     * 
     * @param predicate
     *            The {@link Predicate} to filter recipes.
     * 
     * @return A Set of Recipes matching your filter.
     */
    @Nonnull
    public Set<Recipe> getRecipes(@Nonnull Predicate<Recipe> predicate) {
        return streamAllRecipes().filter(predicate).collect(Collectors.toSet());
    }

    /**
     * This method will return a {@link Set} of Recipes that result in an {@link ItemStack}
     * with the given {@link Material}.
     * 
     * @param type
     *            The {@link Material} of your Recipes' outputs.
     * 
     * @return A {@link Set} of Recipes resulting in an {@link ItemStack} with the given {@link Material}
     */
    @Nonnull
    public Set<Recipe> getRecipesFor(@Nonnull Material type) {
        return getRecipes(recipe -> recipe.getResult().getType() == type);
    }

    /**
     * This method will return a {@link Set} of Recipes that result in the given {@link ItemStack}.
     * 
     * @param item
     *            The Result of the Recipes you are looking for
     * 
     * @return A {@link Set} of Recipes resulting in the given {@link ItemStack}
     */
    @Nonnull
    public Set<Recipe> getRecipesFor(@Nonnull ItemStack item) {
        return getRecipes(recipe -> recipe.getResult().isSimilar(item));
    }

    /**
     * This method will return a {@link Set} of Recipes that take in the given {@link ItemStack}.
     * Precisely: A {@link Recipe} will be included in this {@link Set} if one of the {@link RecipeChoice} inputs
     * matches the given {@link ItemStack}
     * 
     * @param item
     *            The ItemStack input for the Recipes you are looking for.
     * 
     * @return A {@link Set} of Recipes that include the given {@link ItemStack} as an input.
     */
    @Nonnull
    public Set<Recipe> getRecipesWith(@Nonnull ItemStack item) {
        return getRecipes(recipe -> Arrays.stream(getRecipeInput(recipe)).anyMatch(choice -> choice.test(item)));
    }

    /**
     * This method will return a {@link Recipe} based on the provided {@link NamespacedKey}
     * (if that {@link Recipe} is of type {@link Keyed})
     * The method works similar to {@code Bukkit.getRecipe(NamespacedKey)}, though it is significantly
     * faster since we operate on a cached {@link HashMap} and don't have to perform any data
     * conversion.
     * 
     * @param key
     *            The {@link NamespacedKey}
     * 
     * @return The corresponding {@link Recipe} or null
     */
    @Nullable
    public Recipe getRecipe(@Nonnull NamespacedKey key) {
        return keyedRecipes.get(key);
    }

}
