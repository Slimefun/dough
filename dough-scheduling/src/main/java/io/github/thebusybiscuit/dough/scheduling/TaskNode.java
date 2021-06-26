package io.github.thebusybiscuit.dough.scheduling;

import java.util.function.IntConsumer;

import javax.annotation.Nonnull;

import lombok.Data;

@Data
class TaskNode {

    private final IntConsumer runnable;
    private final boolean asynchronous;
    private int delay = 0;
    private TaskNode nextNode;

    protected TaskNode(@Nonnull IntConsumer consumer, boolean async) {
        this.runnable = consumer;
        this.asynchronous = async;
    }

    protected TaskNode(@Nonnull IntConsumer consumer, int delay, boolean async) {
        this.runnable = consumer;
        this.delay = delay;
        this.asynchronous = async;
    }

    protected boolean hasNextNode() {
        return nextNode != null;
    }

}