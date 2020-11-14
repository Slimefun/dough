package io.github.thebusybiscuit.cscorelib2.scheduling;

import java.util.function.IntConsumer;

import lombok.Data;
import lombok.NonNull;

@Data
class TaskNode {

    private final IntConsumer runnable;
    private final boolean asynchronous;
    private int delay = 0;
    private TaskNode nextNode;

    protected TaskNode(@NonNull IntConsumer consumer, boolean async) {
        this.runnable = consumer;
        this.asynchronous = async;
    }

    protected TaskNode(@NonNull IntConsumer consumer, int delay, boolean async) {
        this.runnable = consumer;
        this.delay = delay;
        this.asynchronous = async;
    }

    protected boolean hasNextNode() {
        return nextNode != null;
    }

}