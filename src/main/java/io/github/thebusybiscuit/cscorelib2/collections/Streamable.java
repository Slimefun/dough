package io.github.thebusybiscuit.cscorelib2.collections;

import java.util.stream.Stream;

@FunctionalInterface
public interface Streamable<T> {

	Stream<T> stream();
	
}
