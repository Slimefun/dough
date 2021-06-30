package io.github.thebusybiscuit.dough.collections;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class OptionalPair<P, S> {

    private Optional<P> firstValue;
    private Optional<S> secondValue;

    public OptionalPair(P a, S b) {
        this.firstValue = Optional.ofNullable(a);
        this.secondValue = Optional.ofNullable(b);
    }

    public OptionalPair(Map.Entry<P, S> mapEntry) {
        this(mapEntry.getKey(), mapEntry.getValue());
    }

    public OptionalPair(Pair<P, S> pair) {
        this(pair.getFirstValue(), pair.getSecondValue());
    }

    public Optional<P> getFirstValue() {
        return this.firstValue;
    }

    public Optional<S> getSecondValue() {
        return this.secondValue;
    }

    public void setFirstValue(Optional<P> firstValue) {
        this.firstValue = firstValue;
    }

    public void setSecondValue(Optional<S> secondValue) {
        this.secondValue = secondValue;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OptionalPair)) {
            return false;
        }
        final OptionalPair<?, ?> other = (OptionalPair<?, ?>) o;
        if (!Objects.equals(firstValue, other.firstValue)) {
            return false;
        }
        return Objects.equals(secondValue, other.secondValue);
    }

    public int hashCode() {
        final int prime = 59;
        int result = 1;
        result = result * prime + (!firstValue.isPresent() ? 43 : firstValue.hashCode());
        result = result * prime + (!secondValue.isPresent() ? 43 : secondValue.hashCode());
        return result;
    }

    public String toString() {
        return "OptionalPair(firstValue=" + this.getFirstValue() + ", secondValue=" + this.getSecondValue()
            + ")";
    }
}
