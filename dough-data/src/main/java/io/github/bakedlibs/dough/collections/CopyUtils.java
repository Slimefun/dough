package io.github.bakedlibs.dough.collections;

import java.util.Collection;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import javax.annotation.Nonnull;

/**
 * Utilities for deep-copying collections, maps and arrays
 *
 * @author md5sha256
 */
public final class CopyUtils {

    private CopyUtils() {}

    /**
     * Perform a deep copy of all the elements from a given {@link Collection} to another.
     * <p>
     * If the source collection contains <code>null</code> elements, the cloning function should be
     * able to handle null input. Additionally, the sink should also accept any values
     * contained by the source collection, null or not.
     *
     * @param source
     *            The source of the elements
     * @param cloningFunction
     *            The function which clones the elements
     * @param sink
     *            The collection in which to copy to the cloned elements to
     * @param <T>
     *            The type of elements in the collections
     *
     */
    public static <T> void deepCopy(@Nonnull Collection<T> source, @Nonnull UnaryOperator<T> cloningFunction, @Nonnull Collection<T> sink) {
        for (T original : source) {
            T cloned = cloningFunction.apply(original);
            sink.add(cloned);
        }
    }

    /**
     * Perform a deep copy of all the elements from a given {@link Collection} to another.
     * <p>
     * If the source collection contains <code>null</code> elements, the cloning function should be
     * able to handle null input. Additionally, the sink supplied by the sink supplier
     * should also accept any values contained by the source collection, null or not.
     *
     * @param source
     *            The source of the elements
     * @param cloningFunction
     *            The function which clones the elements
     * @param sinkSupplier
     *            The supplier which consumes the size of the source collection and
     *            supplies the collection to copy cloned elements to
     * 
     * @param <T>
     *            The type of elements in the collections
     * @param <C>
     *            The type of the returned collection
     *
     */
    public static @Nonnull <T, C extends Collection<T>> C deepCopy(@Nonnull Collection<T> source, @Nonnull UnaryOperator<T> cloningFunction, @Nonnull IntFunction<C> sinkSupplier) {
        C sink = sinkSupplier.apply(source.size());
        deepCopy(source, cloningFunction, sink);
        return sink;
    }

    /**
     * Perform a deep copy of all the elements from a given {@link Map} to another.
     * <p>
     * If the source map contains <code>null</code> values, the cloning function should be
     * able to handle null input. Additionally, the sink should also accept any keys and values
     * contained by the source Map, null or not.
     *
     * @param source
     *            The source of the elements
     * @param cloningFunction
     *            The function which clones the elements
     * @param sink
     *            The map in which to copy to the cloned elements to
     * @param <K>
     *            The type of keys in the maps
     * @param <V>
     *            The type of values in the maps
     *
     */
    public static <K, V> void deepCopy(@Nonnull Map<K, V> source, @Nonnull UnaryOperator<V> cloningFunction, @Nonnull Map<K, V> sink) {
        for (Map.Entry<K, V> entry : source.entrySet()) {
            V original = entry.getValue();
            V cloned = cloningFunction.apply(original);
            sink.put(entry.getKey(), cloned);
        }
    }

    /**
     * Perform a deep copy of all the elements from a given {@link Map} to another.
     * <p>
     * If the source map contains <code>null</code> values, the cloning function should be
     * able to handle null input. Additionally, the sink should also accept any keys and values
     * contained by the source Map, null or not.
     *
     * @param source
     *            The source of the elements
     * @param cloningFunction
     *            The function which clones the elements
     * @param sinkSupplier
     *            The supplier which consumes the size of the source map and
     *            supplies the map to copy cloned elements to
     * @param <K>
     *            The type of keys in the maps
     * @param <V>
     *            The type of values in the maps
     * @param <M>
     *            The type of the returned map
     *
     */
    public static @Nonnull <K, V, M extends Map<K, V>> M deepCopy(@Nonnull Map<K, V> source, @Nonnull UnaryOperator<V> cloningFunction, @Nonnull Supplier<M> sinkSupplier) {
        M sink = sinkSupplier.get();
        deepCopy(source, cloningFunction, sink);
        return sink;
    }

    /**
     * Perform a deep-clone transformation on all values in a given {@link Map}
     * <p>
     * If the source map contains <code>null</code> values, the cloning function should be
     * able to handle null input.
     *
     * @param source
     *            The source of the elements
     * @param cloningFunction
     *            The function which clones the elements
     * @param <K>
     *            The type of keys in the map
     * @param <V>
     *            The type of values in the map
     *
     */
    public static <K, V> void deepCopy(@Nonnull Map<K, V> source, @Nonnull UnaryOperator<V> cloningFunction) {
        for (Map.Entry<K, V> entry : source.entrySet()) {
            V original = entry.getValue();
            V cloned = cloningFunction.apply(original);
            entry.setValue(cloned);
        }
    }

    /**
     * Perform a deep copy of all the elements from a given array to another.
     * <p>
     * If the source array contains <code>null</code> elements, the cloning function should be
     * able to handle null input.
     *
     * @param source
     *            The source of the elements
     * @param cloningFunction
     *            The function which clones the elements
     * @param sink
     *            The array in which to copy to the cloned elements to
     * @param <T>
     *            The type of elements in the arrays
     *
     */
    public static <T> void deepCopy(@Nonnull T[] source, @Nonnull UnaryOperator<T> cloningFunction, @Nonnull T[] sink) {
        if (source.length > sink.length) {
            throw new IllegalArgumentException("Length of sink must be greater than or equal to that of the source!");
        }
        for (int i = 0; i < source.length; i++) {
            sink[i] = cloningFunction.apply(source[i]);
        }
    }

    /**
     * Perform a deep copy of all the elements from a given array to another.
     * <p>
     * If the source array contains <code>null</code> elements, the cloning function should be
     * able to handle null input.
     *
     * @param source
     *            The source of the elements
     * @param cloningFunction
     *            The function which clones the elements
     * @param sinkSupplier
     *            The supplier which consumes the length of the source array and supplies
     *            the array to copy the cloned elements to
     * @param <T>
     *            The type of elements in the arrays
     *
     */
    public static @Nonnull <T> T[] deepCopy(@Nonnull T[] source, @Nonnull UnaryOperator<T> cloningFunction, @Nonnull IntFunction<T[]> sinkSupplier) {
        T[] sink = sinkSupplier.apply(source.length);
        deepCopy(source, cloningFunction, sink);
        return sink;
    }

}
