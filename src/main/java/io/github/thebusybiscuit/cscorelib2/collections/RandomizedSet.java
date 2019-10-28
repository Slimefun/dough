package io.github.thebusybiscuit.cscorelib2.collections;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import lombok.NonNull;

public class RandomizedSet<T> implements Iterable<T> {
	
	private final Set<WeightedNode<T>> internalSet;
	
	private int size = 0;
	private float totalWeights = 0F;
	
	public RandomizedSet() {
		this(LinkedHashSet<WeightedNode<T>>::new);
	}
	
	public RandomizedSet(@NonNull Supplier<Set<WeightedNode<T>>> constructor) {
		internalSet = constructor.get();
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public boolean contains(@NonNull T obj) {
		for (WeightedNode<T> node: internalSet) {
			if (node.equals(obj)) return true;
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
				return node == null ? null: node.getObject();
			}
			
			@Override
			public void remove() {
				iterator.remove();
			}
		};
	}
	
	public T[] toArray(@NonNull IntFunction<T[]> constructor) {
		T[] array = constructor.apply(size);
		Iterator<T> iterator = iterator();
		int i = 0;
		
		while (iterator.hasNext()) {
			array[i] = iterator.next();
			i++;
		}
		
		return array;
	}
	
	public boolean add(@NonNull T obj, float weight) {
		if (weight <= 0F) throw new IllegalArgumentException("A Weight may never be less than or equal to zero!");
		
		if (internalSet.add(new WeightedNode<>(weight, obj))) {
			size++;
			totalWeights += weight;
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setWeight(@NonNull T obj, float weight) {
		if (weight <= 0F) throw new IllegalArgumentException("A Weight may never be less than or equal to zero!");
		
		for (WeightedNode<T> node: internalSet) {
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
	
	public boolean remove(@NonNull T obj) {
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
	
	public void clear() {
		size = 0;
		totalWeights = 0F;
		
		internalSet.clear();
	}
	
	public Stream<T> stream() {
		return StreamSupport.stream(spliterator(), false);
	}
	
	public T getRandom() {
		return getRandom(ThreadLocalRandom.current());
	}
	
	public T getRandom(@NonNull Random random) {
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
		
		return node.getObject();
	}
	
	public Set<T> getRandomSubset(int size) {
		return getRandomSubset(ThreadLocalRandom.current(), size);
	}
	
	public Set<T> getRandomSubset(@NonNull Random random, int size) {
		Set<T> subset = new HashSet<>();
		
		while (subset.size() < size) {
			subset.add(getRandom(random));
		}
		
		return subset;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		boolean first = true;
		for (WeightedNode<T> node: internalSet) {
			if (!first) {
				builder.append(", ");
			}
			else {
				first = false;
			}
			
			builder.append("(").append(node.getObject().toString()).append(" | " + node.getWeight() + ")");
		}
		
		return getClass().getSimpleName() + "{" + builder.toString() + "}";
	}

}
