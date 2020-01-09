package io.github.thebusybiscuit.cscorelib2.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;

import lombok.NonNull;

public class KeyMap<T extends Keyed> extends OptionalMap<NamespacedKey, T> {

	public KeyMap(@NonNull Supplier<? extends Map<NamespacedKey, T>> constructor) {
		super(constructor);
	}

	public KeyMap() {
		this(HashMap::new);
	}
	
	public void add(T value) {
		put(value.getKey(), value);
	}

}
