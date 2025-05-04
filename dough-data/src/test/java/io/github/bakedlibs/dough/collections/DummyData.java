package io.github.bakedlibs.dough.collections;

import javax.annotation.Nonnull;

class DummyData implements Cloneable {

    private final int value;

    DummyData(int value) {
        this.value = value;
    }

    private DummyData(@Nonnull DummyData other) {
        this.value = other.value;
    }

    @Override
    public @Nonnull DummyData clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        return new DummyData(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        DummyData dummyData = (DummyData) o;

        return value == dummyData.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "DummyData{" + "value=" + value + '}';
    }

}
