package io.github.bakedlibs.dough.collections;

import org.apache.commons.lang.Validate;

import javax.annotation.Nonnull;

public class WeightedNode<T> {

    private float weight;

    private T object;

    public WeightedNode(float weight, @Nonnull T object) {
        Validate.notNull(object, "Object cannot be null");

        this.weight = weight;
        this.object = object;
    }

    public float getWeight() {
        return this.weight;
    }

    public @Nonnull T getObject() {
        return this.object;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setObject(@Nonnull T object) {
        Validate.notNull(object, "Object cannot be null");
        this.object = object;
    }

    @Override
    public int hashCode() {
        return object.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        Object compared = obj;
        if (obj instanceof WeightedNode) {
            compared = ((WeightedNode<?>) obj).getObject();
        }

        return object.getClass().isInstance(compared) && compared.hashCode() == hashCode();
    }

}
