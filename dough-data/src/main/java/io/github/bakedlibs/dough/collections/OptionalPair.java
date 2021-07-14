package io.github.bakedlibs.dough.collections;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class OptionalPair<P, S> {

    private Optional<P> firstValue;
    private Optional<S> secondValue;

    public OptionalPair(@Nullable P a, @Nullable S b) {
        this.firstValue = Optional.ofNullable(a);
        this.secondValue = Optional.ofNullable(b);
    }

    public OptionalPair(@Nonnull Map.Entry<P, S> mapEntry) {
        this(mapEntry.getKey(), mapEntry.getValue());
    }

    public OptionalPair(@Nonnull Pair<P, S> pair) {
        this(pair.getFirstValue(), pair.getSecondValue());
    }

    public @Nonnull Optional<P> getFirstValue() {
        return this.firstValue;
    }

    public @Nonnull Optional<S> getSecondValue() {
        return this.secondValue;
    }

    public void setFirstValue(@Nullable P firstValue) {
        this.firstValue = Optional.ofNullable(firstValue);
    }

    public void setSecondValue(@Nullable S secondValue) {
        this.secondValue = Optional.ofNullable(secondValue);
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

    public @Nonnull String toString() {
        return "OptionalPair(firstValue=" + firstValue + ", secondValue=" + secondValue
            + ")";
    }
}
