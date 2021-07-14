package io.github.bakedlibs.dough.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;

/**
 * This Class functions similar to {@link Map} but returns an
 * Optional when calling {@link OptionalMap#get(Object)}.
 * This way you can save yourself some {@link Map#containsKey(Object)} calls
 * and also benefit from the methods that {@link Optional} implements.
 * 
 * @author TheBusyBiscuit
 *
 * @param <K>
 *            The type of keys for this Map
 * @param <V>
 *            The Type of values for this Maps
 */
public class OptionalMap<K, V> implements Iterable<Map.Entry<K, V>>, Streamable<Entry<K, V>> {

    private Map<K, V> internalMap;

    /**
     * An OptionalMap allows you to directly obtain Optionals from a Map.
     * The Map implementation is up to you, you can pass in the constructor
     * of any class that implements the Map interface.
     * 
     * <code>OptionalMap&gt;String, String&lt; map = new OptionalMap&gt;&lt;(HashMap::new);</code>
     * 
     * @param constructor
     *            A Constructor reference to an existing Map implementation
     */
    public OptionalMap(@Nonnull Supplier<? extends Map<K, V>> constructor) {
        internalMap = constructor.get();

        if (internalMap == null) {
            throw new IllegalStateException("Internal Map is not allowed to be null!");
        }
    }

    /**
     * This method returns the size of this Map.
     * 
     * @return The size of our Map
     */
    public int size() {
        return internalMap.size();
    }

    /**
     * This method returns whether our Map is empty.
     * 
     * @return Whether out Map is empty
     */
    public boolean isEmpty() {
        return internalMap.isEmpty();
    }

    /**
     * This method gives you an {@link Optional} describing the value mapped to the given key.
     * If no mapping was found then {@link Optional#empty()} will be returned.
     * 
     * @param key
     *            The key to our Value
     * @return An Optional describing the result, empty if no mapping was found
     */
    public Optional<V> get(K key) {
        return Optional.ofNullable(internalMap.get(key));
    }

    /**
     * {@link OptionalMap#get(Object)} should be preferred.
     * 
     * @param key
     *            The key to our Value
     * @return Whether the key is present in our Map
     */
    public boolean containsKey(K key) {
        return get(key).isPresent();
    }

    public boolean containsValue(V value) {
        return internalMap.containsValue(value);
    }

    public void ifPresent(K key, Consumer<? super V> consumer) {
        get(key).ifPresent(consumer);
    }

    public void ifAbsent(K key, Consumer<Void> consumer) {
        if (!containsKey(key))
            consumer.accept(null);
    }

    public Optional<V> put(K key, V value) {
        return Optional.ofNullable(internalMap.put(key, value));
    }

    public Optional<V> remove(K key) {
        return Optional.ofNullable(internalMap.remove(key));
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        internalMap.putAll(map);
    }

    public void clear() {
        internalMap.clear();
    }

    public Set<K> keySet() {
        return internalMap.keySet();
    }

    public Collection<V> values() {
        return internalMap.values();
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return internalMap.entrySet();
    }

    @Override
    public boolean equals(Object obj) {
        return internalMap.equals(obj);
    }

    @Override
    public int hashCode() {
        return internalMap.hashCode();
    }

    public V getOrDefault(K key, V defaultValue) {
        return internalMap.getOrDefault(key, defaultValue);
    }

    public void forEach(BiConsumer<? super K, ? super V> consumer) {
        internalMap.forEach(consumer);
    }

    public V putIfAbsent(K key, V value) {
        return internalMap.putIfAbsent(key, value);
    }

    public V computeIfAbsent(K key, Function<? super K, ? extends V> function) {
        return internalMap.computeIfAbsent(key, function);
    }

    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> function) {
        return internalMap.computeIfPresent(key, function);
    }

    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> function) {
        return internalMap.compute(key, function);
    }

    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> function) {
        return internalMap.merge(key, value, function);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return entrySet().iterator();
    }

    @Override
    public Stream<Entry<K, V>> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public @Nonnull Map<K, V> getInternalMap() {
        return internalMap;
    }

}
