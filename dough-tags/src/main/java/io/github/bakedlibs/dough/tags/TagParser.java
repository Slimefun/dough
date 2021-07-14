package io.github.bakedlibs.dough.tags;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import io.github.bakedlibs.dough.common.CommonPatterns;

/**
 * The {@link TagParser} is responsible for parsing a JSON input into a {@link CustomMaterialTag}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see CustomMaterialTag
 *
 */
public class TagParser implements Keyed {

    /**
     * Every {@link Tag} has a {@link NamespacedKey}.
     * This is the {@link NamespacedKey} for the resulting {@link Tag}.
     */
    private final NamespacedKey key;

    /**
     * This constructs a new {@link TagParser}.
     * 
     * @param key
     *            The {@link NamespacedKey} of the resulting {@link CustomMaterialTag}
     */
    public TagParser(@Nonnull NamespacedKey key) {
        this.key = key;
    }

    @ParametersAreNonnullByDefault
    public @Nonnull CompletableFuture<MaterialTagData> parse(InputStream inputStream) throws TagMisconfigurationException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return parse(reader.lines().collect(Collectors.joining("")));
        } catch (IOException x) {
            throw new TagMisconfigurationException(key, x);
        }
    }

    /**
     * This will parse the given JSON {@link String} and run the provided callback with {@link Set Sets} of
     * matched {@link Material Materials} and {@link Tag Tags}.
     * 
     * @param json
     *            The JSON {@link String} to parse
     * @param callback
     *            A callback to run after successfully parsing the input
     * 
     * @throws TagMisconfigurationException
     *             This is thrown whenever the given input is malformed or no adequate
     *             {@link Material} or {@link Tag} could be found
     */
    @ParametersAreNonnullByDefault
    public @Nonnull CompletableFuture<MaterialTagData> parse(String json) throws TagMisconfigurationException {
        Validate.notNull(json, "Cannot parse a null String");

        try {
            JsonParser parser = new JsonParser();
            JsonObject root = parser.parse(json).getAsJsonObject();
            return parse(root);
        } catch (IllegalStateException | JsonParseException x) {
            throw new TagMisconfigurationException(key, x);
        }
    }

    @ParametersAreNonnullByDefault
    public @Nonnull CompletableFuture<MaterialTagData> parse(JsonObject json) throws TagMisconfigurationException {
        Validate.notNull(json, "Cannot parse a null JsonObject");

        JsonElement child = json.get("values");

        if (child instanceof JsonArray) {
            JsonArray values = child.getAsJsonArray();

            Set<Material> materials = EnumSet.noneOf(Material.class);
            Set<Tag<Material>> tags = new HashSet<>();

            for (JsonElement element : values) {
                if (element instanceof JsonPrimitive && ((JsonPrimitive) element).isString()) {
                    // Strings will be parsed directly
                    parsePrimitiveValue(element.getAsString(), materials, tags, true);
                } else if (element instanceof JsonObject) {
                    /*
                     * JSONObjects can have a "required" property which can
                     * make it optional to resolve the underlying value
                     */
                    parseComplexValue(element.getAsJsonObject(), materials, tags);
                } else {
                    throw new TagMisconfigurationException(key, "Unexpected value format: " + element.getClass().getSimpleName() + " - " + element.toString());
                }
            }

            // Run the callback with the filled-in materials and tags
            return CompletableFuture.completedFuture(new MaterialTagData(materials, tags));
        } else {
            // The JSON seems to be empty yet valid
            throw new TagMisconfigurationException(key, "No values array specified");
        }
    }

    @ParametersAreNonnullByDefault
    private void parsePrimitiveValue(String value, Set<Material> materials, Set<Tag<Material>> tags, boolean throwException) throws TagMisconfigurationException {
        if (CommonPatterns.MINECRAFT_MATERIAL.matcher(value).matches()) {
            // Match the NamespacedKey against Materials
            Material material = Material.matchMaterial(value);

            if (material != null) {
                // If the Material could be matched, simply add it to our Set
                materials.add(material);
            } else if (throwException) {
                throw new TagMisconfigurationException(key, "Minecraft Material '" + value + "' seems to not exist!");
            }
        } else if (CommonPatterns.MINECRAFT_TAG.matcher(value).matches()) {
            // Get the actual Key portion and match it to item and block tags.
            String keyValue = CommonPatterns.COLON.split(value)[1];
            NamespacedKey namespacedKey = NamespacedKey.minecraft(keyValue);
            Tag<Material> itemsTag = Bukkit.getTag(Tag.REGISTRY_ITEMS, namespacedKey, Material.class);
            Tag<Material> blocksTag = Bukkit.getTag(Tag.REGISTRY_BLOCKS, namespacedKey, Material.class);

            if (itemsTag != null) {
                // We will prioritize the item tag
                tags.add(itemsTag);
            } else if (blocksTag != null) {
                // If no item tag exists, fall back to the block tag
                tags.add(blocksTag);
            } else if (throwException) {
                // If both fail, then the tag does not exist.
                throw new TagMisconfigurationException(key, "There is no '" + value + "' tag in Minecraft.");
            }
        } else if (throwException) {
            // If no RegEx pattern matched, it's malformed.
            throw new TagMisconfigurationException(key, "Could not recognize value '" + value + "'");
        }
    }

    @ParametersAreNonnullByDefault
    private void parseComplexValue(JsonObject entry, Set<Material> materials, Set<Tag<Material>> tags) throws TagMisconfigurationException {
        JsonElement id = entry.get("id");
        JsonElement required = entry.get("required");

        // Check if the entry contains elements of the correct type
        if (id instanceof JsonPrimitive && ((JsonPrimitive) id).isString() && required instanceof JsonPrimitive && ((JsonPrimitive) required).isBoolean()) {
            boolean isRequired = required.getAsBoolean();

            /*
             * If the Tag is required, an exception may be thrown.
             * Otherwise it will just ignore the value
             */
            parsePrimitiveValue(id.getAsString(), materials, tags, isRequired);
        } else {
            throw new TagMisconfigurationException(key, "Found a JSON Object value without an id!");
        }
    }

    @Override
    public @Nonnull NamespacedKey getKey() {
        return key;
    }

}
