package io.github.bakedlibs.dough.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;

public class KeyMap<T extends Keyed> extends OptionalMap<NamespacedKey, T> {

    public KeyMap(@Nonnull Supplier<? extends Map<NamespacedKey, T>> constructor) {
        super(constructor);
    }

    public KeyMap() {
        this(HashMap::new);
    }

    public void add(T value) {
        put(value.getKey(), value);
    }

}
