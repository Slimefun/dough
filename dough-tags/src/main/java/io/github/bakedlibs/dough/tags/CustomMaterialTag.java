package io.github.bakedlibs.dough.tags;

import java.io.InputStream;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;

import com.google.gson.JsonObject;

/**
 * This enum contains various implementations of the {@link Tag} interface.
 * Most of them serve some purpose within Slimefun's implementation, some are just pure
 * extensions of the default Minecraft tags.
 * The actual tag files are located in the {@literal /src/main/resources/tags} directory
 * and follow Minecraft's tags.json format.
 *
 * @author TheBusyBiscuit
 *
 * @see TagParser
 *
 */
public class CustomMaterialTag implements Tag<Material> {

    private final NamespacedKey key;

    private final Set<Material> includedMaterials = EnumSet.noneOf(Material.class);
    private final Set<Tag<Material>> additionalTags = new HashSet<>();

    /**
     * This constructs a new {@link CustomMaterialTag}.
     * 
     * @param key
     *            The {@link NamespacedKey}
     */
    public CustomMaterialTag(@Nonnull NamespacedKey key) {
        this.key = key;
    }

    public void loadFromStream(@Nonnull InputStream inputStream) throws TagMisconfigurationException {
        load(new TagParser(key).parse(inputStream));
    }

    public void loadFromJson(@Nonnull JsonObject json) throws TagMisconfigurationException {
        load(new TagParser(key).parse(json));
    }

    public void loadFromString(@Nonnull String json) throws TagMisconfigurationException {
        load(new TagParser(key).parse(json));
    }

    private void load(@Nonnull CompletableFuture<MaterialTagData> future) {
        future.thenAccept(data -> {
            this.includedMaterials.clear();
            this.includedMaterials.addAll(data.getMaterials());

            this.additionalTags.clear();
            this.additionalTags.addAll(data.getChildTags());
        });
    }

    @Override
    public @Nonnull NamespacedKey getKey() {
        return key;
    }

    @Override
    public boolean isTagged(@Nonnull Material item) {
        if (includedMaterials.contains(item)) {
            return true;
        } else {
            // Check if any of our additional Tags contain this Materials
            for (Tag<Material> tag : additionalTags) {
                if (tag.isTagged(item)) {
                    return true;
                }
            }

            // Now we can be sure it isn't tagged in any way
            return false;
        }
    }

    @Override
    public @Nonnull Set<Material> getValues() {
        if (additionalTags.isEmpty()) {
            return Collections.unmodifiableSet(includedMaterials);
        } else {
            Set<Material> materials = EnumSet.noneOf(Material.class);
            materials.addAll(includedMaterials);

            for (Tag<Material> tag : additionalTags) {
                materials.addAll(tag.getValues());
            }

            return materials;
        }
    }

    public boolean isEmpty() {
        if (!includedMaterials.isEmpty()) {
            /*
             * Without even needing to generate a Set we can safely
             * return false if there are directly included Materials
             */
            return false;
        } else {
            return getValues().isEmpty();
        }
    }

    /**
     * This returns a {@link Set} of {@link Tag Tags} which are children of this {@link CustomMaterialTag},
     * these can be other {@link CustomMaterialTag SlimefunTags} or regular {@link Tag Tags}.
     *
     * <strong>The returned {@link Set} is immutable</strong>
     *
     * @return An immutable {@link Set} of all sub tags.
     */
    public @Nonnull Set<Tag<Material>> getSubTags() {
        return Collections.unmodifiableSet(additionalTags);
    }

    /**
     * This method returns an Array representation for this {@link CustomMaterialTag}.
     *
     * @return A {@link Material} array for this {@link Tag}
     */
    public @Nonnull Material[] toArray() {
        return getValues().toArray(new Material[0]);
    }

    /**
     * This returns a {@link Stream} of {@link Material Materials} for this {@link CustomMaterialTag}.
     *
     * @return A {@link Stream} of {@link Material Materials}
     */
    public @Nonnull Stream<Material> stream() {
        return getValues().stream();
    }

}
