package io.github.thebusybiscuit.cscorelib2.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.NonNull;

public class LoopIterator<T> implements Iterator<T>, Streamable<T> {
	
	private final List<T> list;
	private final int size;
	
	@Getter
	private int index = 0;
	
	public LoopIterator(@NonNull Collection<T> collection) {
		size = collection.size();
		
		if (size > 0) {
			list = new ArrayList<>(collection);
		}
		else {
			list = null;
		}
	}

	@Override
	public boolean hasNext() {
		return size > 0;
	}

	@Override
	public T next() {
		if (list == null) {
			throw new NoSuchElementException("The given collection was empty.");
		}
		else if (index >= size) {
			index = 0;
		}
		
		return list.get(index++);
	}

	@Override
	public Stream<T> stream() {
		return Stream.generate(this::next);
	}

}
