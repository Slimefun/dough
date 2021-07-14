package io.github.bakedlibs.dough.collections;

import java.util.stream.Stream;

@FunctionalInterface
public interface Streamable<T> {

    Stream<T> stream();

}
