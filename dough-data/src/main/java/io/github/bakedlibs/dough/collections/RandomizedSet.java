package io.github.bakedlibs.dough.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;

/**
 * This Class represents a collection of elements.
 * Each element is given a weight making the randomized selection
 * of elements weight-based.
 * 
 * This collection acts like a {@link Set}, it will not allow duplicates.
 * 
 * Use {@link RandomizedSet#getRandom()} to draw a random element from this Set.
 * You can also create Subsets of this collection via {@link RandomizedSet#getRandomSubset(int)}
 * 
 * @author TheBusyBiscuit
 *
 * @param <T>
 *            The Type of Element that is stored in this Set
 */
public class RandomizedSet<T> implements Iterable<T>, Streamable<T> {

    private final Set<WeightedNode<T>> internalSet;

    private int size = 0;
    private float totalWeights = 0F;

    /**
     * This will initialize a new {@link RandomizedSet} with the internal Set
     * being a {@link LinkedHashSet}
     */
    public RandomizedSet() {
        this(LinkedHashSet::new);
    }

    /**
     * This will initialize a new {@link RandomizedSet} using the given implementation of {@link Set}
     * 
     * <code>RandomizedSet&gt;String&lt; map = new RandomizedSet&gt;&lt;(HashSet::new);</code>
     * 
     * @param constructor
     *            The Constructor for an implementation of {@link Set}
     */
    public RandomizedSet(@Nonnull Supplier<Set<WeightedNode<T>>> constructor) {
        internalSet = constructor.get();
    }

    /**
     * This will initialize a new {@link RandomizedSet} with the internal Set
     * being a {@link LinkedHashSet}
     * 
     * It will be populated with elements from the given {@link Collection},
     * each element will be given a weight of 1.
     * 
     * @param collection
     *            A {@link Collection} to pick elements from, each with the weight of 1.
     */
    public RandomizedSet(@Nonnull Collection<T> collection) {
        this();

        for (T element : collection) {
            add(element, 1);
        }
    }

    /**
     * This method returns the cardinality of this set.
     * The cardinality describes the amount of elements included in that Set.
     * 
     * @return The number of elements in this Set
     */
    public int size() {
        return size;
    }

    /**
     * This method returns the sum of all the weights in this set.
     * 
     * @return The sum of all the individual weights from the elements included in this Set.
     */
    public float sumWeights() {
        return totalWeights;
    }

    /**
     * This method returns whether this Set is empty.
     * 
     * @return Whether this Set is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * This method returns whether the given element is contained in this Set
     * 
     * @param obj
     *            The element to check for
     * @return Whether the given element is contained in this Set
     */
    public boolean contains(@Nonnull T obj) {
        for (WeightedNode<T> node : internalSet) {
            if (node.equals(obj)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private Iterator<WeightedNode<T>> iterator = internalSet.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                WeightedNode<T> node = iterator.next();
                return node == null ? null : node.getObject();
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }

    /**
     * Returns an Array for all elements contained in this Set.
     * 
     * @param constructor
     *            A reference to an Array constructor
     * @return An Array containing all elements in this Set
     */
    public T[] toArray(@Nonnull IntFunction<T[]> constructor) {
        T[] array = constructor.apply(size);
        Iterator<T> iterator = iterator();
        int i = 0;

        while (iterator.hasNext()) {
            array[i] = iterator.next();
            i++;
        }

        return array;
    }

    /**
     * This method adds a new element to this Set with the given weight.
     * The weight must be greater than 0.
     * 
     * @param obj
     *            The element to add
     * @param weight
     *            The associated weight
     * @return Whether the element was added successfully
     */
    public boolean add(@Nonnull T obj, float weight) {
        if (weight <= 0F) {
            throw new IllegalArgumentException("A Weight may never be less than or equal to zero!");
        }

        if (internalSet.add(new WeightedNode<>(weight, obj))) {
            size++;
            totalWeights += weight;
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method updates an element with the given weight.
     * The element must be contained in this Set before calling this method,
     * otherwise an {@link IllegalStateException} will be thrown.
     * 
     * @param obj
     *            The element in this Set
     * @param weight
     *            The new weight for this element
     */
    public void setWeight(@Nonnull T obj, float weight) {
        if (weight <= 0F) {
            throw new IllegalArgumentException("A Weight may never be less than or equal to zero!");
        }

        for (WeightedNode<T> node : internalSet) {
            if (node.equals(obj)) {
                size--;
                totalWeights -= node.getWeight();
                totalWeights += weight;

                node.setWeight(weight);
                return;
            }
        }

        throw new IllegalStateException("The specified Object is not contained in this Set");
    }

    /**
     * This method will remove the given Item from this Set.
     * If the element is not contained in the Set, it will return false.
     * 
     * @param obj
     *            The element to remove
     * @return Whether the element was removed successfully
     */
    public boolean remove(@Nonnull T obj) {
        Iterator<WeightedNode<T>> iterator = internalSet.iterator();

        while (iterator.hasNext()) {
            WeightedNode<T> node = iterator.next();

            if (node.equals(obj)) {
                size--;
                totalWeights -= node.getWeight();

                iterator.remove();
                return true;
            }
        }

        return false;
    }

    /**
     * This method clears this Set and removes all elements from it.
     */
    public void clear() {
        size = 0;
        totalWeights = 0F;

        internalSet.clear();
    }

    /**
     * This method allows you to stream all elements in this Set.
     * 
     * @return A Stream of elements from this Set
     */
    @Override
    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * This method gives you a randomly selected item from this Set.
     * The selection is based on their weights.
     * 
     * @return A random element from this Set
     */
    public T getRandom() {
        return getRandom(ThreadLocalRandom.current());
    }

    /**
     * This method gives you a randomly selected item from this Set.
     * The selection is based on their weights.
     * 
     * You can specify an instance of {@link Random}.
     * 
     * @param random
     *            An instance of {@link Random} to be used.
     * @return A random element from this Set
     */
    public T getRandom(@Nonnull Random random) {
        float goal = random.nextFloat() * totalWeights;
        float i = 0;

        Iterator<WeightedNode<T>> iterator = internalSet.iterator();
        WeightedNode<T> node = null;

        while (iterator.hasNext()) {
            node = iterator.next();
            i += node.getWeight();

            if (i >= goal) {
                return node.getObject();
            }
        }

        return node == null ? null : node.getObject();
    }

    /**
     * This will create a random subset of unique elements
     * from this Set.
     * The selection is based on their weights.
     * 
     * If the size you specify is bigger than the size of this Set,
     * an {@link IllegalArgumentException} will be thrown.
     * 
     * @param size
     *            The amount of items to draw from this Set.
     * @return A random Subset from this Set.
     */
    public Set<T> getRandomSubset(int size) {
        return getRandomSubset(ThreadLocalRandom.current(), size);
    }

    /**
     * This will create a random subset of unique elements
     * from this Set.
     * The selection is based on their weights.
     * 
     * You can specify an instance of {@link Random}.
     * 
     * If the size you specify is bigger than the size of this Set,
     * an {@link IllegalArgumentException} will be thrown.
     * 
     * @param random
     *            An instance of {@link Random} to be used.
     * @param size
     *            The amount of items to draw from this Set.
     * @return A random Subset from this Set.
     */
    public Set<T> getRandomSubset(@Nonnull Random random, int size) {
        if (size > size()) {
            throw new IllegalArgumentException("A random Subset may not be larger than the original Set! (" + size + " > " + size() + ")");
        }

        if (size == size()) {
            return internalSet.stream().map(WeightedNode::getObject).collect(Collectors.toSet());
        }

        Set<T> subset = new HashSet<>();

        while (subset.size() < size) {
            subset.add(getRandom(random));
        }

        return subset;
    }

    /**
     * This method returns a Map that holds all elements from this Set
     * and their associated weights.
     * 
     * @return A Map representing this Set's elements and their weights.
     */
    public Map<T, Float> toMap() {
        Map<T, Float> map = new HashMap<>();

        for (WeightedNode<T> node : internalSet) {
            map.put(node.getObject(), node.getWeight() / totalWeights);
        }

        return map;
    }

    /**
     * This method will provide an infinite {@link Stream} of elements, randomly
     * drawn from this {@link RandomizedSet}.
     * 
     * @return An infinite unordered Stream of random elements from this Set
     */
    public Stream<T> randomInfiniteStream() {
        return Stream.generate(this::getRandom);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        boolean first = true;

        for (WeightedNode<T> node : internalSet) {
            if (!first) {
                builder.append(", ");
            } else {
                first = false;
            }

            builder.append("(").append(node.getObject().toString()).append(" | " + node.getWeight() + ")");
        }

        return getClass().getSimpleName() + "{" + builder.toString() + "}";
    }

}
