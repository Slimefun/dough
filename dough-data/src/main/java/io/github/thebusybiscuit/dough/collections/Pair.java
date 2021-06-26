package io.github.thebusybiscuit.dough.collections;

import java.util.Map;

import lombok.Data;

@Data
public class Pair<P, S> {

    private P firstValue;
    private S secondValue;

    public Pair(P a, S b) {
        this.firstValue = a;
        this.secondValue = b;
    }

    public Pair(Map.Entry<P, S> mapEntry) {
        this(mapEntry.getKey(), mapEntry.getValue());
    }

    public Pair(OptionalPair<P, S> pair) {
        this(pair.getFirstValue().orElse(null), pair.getSecondValue().orElse(null));
    }

}
