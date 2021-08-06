package io.github.bakedlibs.dough.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestWeightedNode {

    @Test
    void testGettersAndSetters() {
        float weight1 = 1.0f;
        float weight2 = 2.0f;
        Object object1 = new Object();
        Object object2 = new Object();
        WeightedNode<Object> node = new WeightedNode<>(weight1, object1);
        Assertions.assertSame(object1, node.getObject());
        Assertions.assertEquals(weight1, node.getWeight());
        node.setWeight(weight2);
        node.setObject(object2);
        Assertions.assertSame(object2, node.getObject());
        Assertions.assertEquals(weight2, node.getWeight());
        Assertions.assertThrows(IllegalArgumentException.class, () -> node.setObject(null), "Object cannot be null");
    }

    @Test
    void testEqualityChecking() {
        float weight = 1.0f;
        Object object = new Object();
        WeightedNode<Object> node = new WeightedNode<>(weight, object);
        WeightedNode<Object> similarNode = new WeightedNode<>(weight, object);
        Assertions.assertNotSame(node, similarNode);
        Assertions.assertEquals(node, similarNode);
        Assertions.assertEquals(node.hashCode(), similarNode.hashCode());
        WeightedNode<Object> differentNode = new WeightedNode<>(weight, weight);
        Assertions.assertNotEquals(node, differentNode);
    }


}
