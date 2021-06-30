package io.github.thebusybiscuit.dough.collections;

public class WeightedNode<T> {

    private float weight;

    private T object;

    public WeightedNode(float weight, T object) {
        this.weight = weight;
        this.object = object;
    }

    public float getWeight() {
        return this.weight;
    }

    public T getObject() {
        return this.object;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setObject(T object) {
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

        return obj != null && object.getClass().isInstance(compared) && compared.hashCode() == hashCode();
    }

}
