package io.github.thebusybiscuit.dough.collections;

import java.util.stream.Stream;

@FunctionalInterface
public interface Streamable<T> {

    Stream<T> stream();

}
