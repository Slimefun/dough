package io.github.thebusybiscuit.cscorelib2.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.NonNull;

public class LoopIterator<T> implements Iterator<T>, Streamable<T> {
	
	private final Queue<T> queue;
	private final boolean hasNext;
	
	public LoopIterator(@NonNull Collection<T> collection) {
		this.hasNext = collection.isEmpty();
		
		if (hasNext) {
			this.queue = new LinkedList<>(collection);
		}
		else {
			this.queue = null;
		}
	}
	
	@Getter
	private int index = 0;
	private Iterator<T> iterator;

	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@Override
	public T next() {
		if (iterator == null || !iterator.hasNext()) {
			iterator = queue.iterator();
			index = 0;
		}
		
		index++;
		return iterator.next();
	}

	@Override
	public Stream<T> stream() {
		return Stream.generate(this::next);
	}

}
