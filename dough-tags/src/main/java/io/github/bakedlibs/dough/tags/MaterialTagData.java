package io.github.bakedlibs.dough.tags;

import java.util.Set;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.Tag;

public final class MaterialTagData {

    private final Set<Material> materials;
    private final Set<Tag<Material>> childTags;

    MaterialTagData(Set<Material> materials, Set<Tag<Material>> childTags) {
        this.materials = materials;
        this.childTags = childTags;
    }

    public @Nonnull Set<Material> getMaterials() {
        return materials;
    }

    public @Nonnull Set<Tag<Material>> getChildTags() {
        return childTags;
    }

}
