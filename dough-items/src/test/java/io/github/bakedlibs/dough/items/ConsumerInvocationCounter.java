package io.github.bakedlibs.dough.items;

import java.util.function.Consumer;

public class ConsumerInvocationCounter<T> implements Consumer<T> {

    private final Consumer<T> nested;
    private int invokeCount = 0;

    public ConsumerInvocationCounter() {
        this(x -> {
        });
    }

    public ConsumerInvocationCounter(Consumer<T> consumer) {
        this.nested = consumer;
    }

    @Override
    public void accept(T t) {
        this.nested.accept(t);
        this.invokeCount += 1;
    }

    public boolean wasInvoked() {
        return this.invokeCount > 0;
    }

    public int getInvokeCount() {
        return this.invokeCount;
    }

    public void resetInvokeCount() {
        this.invokeCount = 0;
    }
}
