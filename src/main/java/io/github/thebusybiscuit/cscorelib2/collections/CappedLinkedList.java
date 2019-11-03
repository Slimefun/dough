package io.github.thebusybiscuit.cscorelib2.collections;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Stream;

import lombok.Getter;

public class CappedLinkedList<T> implements Iterable<T>, Streamable<T> {

	@Getter
	private int size;
	
	private final LinkedList<T> list;
	
	public CappedLinkedList(int size) {
		this.size = size;
		this.list = new LinkedList<>();
	}
	
	public void clear() {
		list.clear();
	}
	
	public void add(T obj) {
		list.add(obj);
		
		if (list.size() > size) {
			list.removeFirst();
		}
	}
	
	public void setSize(int size) {
		if (size < 1) throw new IllegalArgumentException("A CappedLinkedList must have a size of at least 1");
		
		this.size = size;
		
		while (size() > size) {
			list.removeFirst();
		}
	}
	
	public int size() {
		return list.size();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	@Override
	public Stream<T> stream() {
		return list.stream();
	}

}
