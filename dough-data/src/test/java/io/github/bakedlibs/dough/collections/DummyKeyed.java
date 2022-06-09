package io.github.bakedlibs.dough.collections;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;

import javax.annotation.Nonnull;

public class DummyKeyed implements Keyed {

    private final NamespacedKey key;

    public DummyKeyed(@Nonnull NamespacedKey key) {
        this.key = key;
    }

    @Override
    public @Nonnull NamespacedKey getKey() {
        return key;
    }

}
