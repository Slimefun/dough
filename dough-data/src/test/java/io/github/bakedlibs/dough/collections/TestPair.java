package io.github.bakedlibs.dough.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Map;

class TestPair {

    @Test
    void testGettersAndSetters() {

        Object primary = new Object();
        Object secondary = new Object();

        Map.Entry<Object, Object> entry = new AbstractMap.SimpleEntry<>(primary, secondary);
        OptionalPair<Object, Object> optionalPair = new OptionalPair<>(primary, secondary);
        Pair<Object, Object> pair = new Pair<>(primary, secondary);
        Pair<Object, Object> pairFromOptional = new Pair<>(optionalPair);
        Pair<Object, Object> pairFromEntry = new Pair<>(entry);

        PairUtils.assertValues(pair, primary, secondary);
        PairUtils.assertValues(pairFromOptional, primary, secondary);
        PairUtils.assertValues(pairFromEntry, primary, secondary);

        Object modifiedPrimary = new Object();
        Object modifiedSecondary = new Object();

        pair.setFirstValue(modifiedPrimary);
        pair.setSecondValue(modifiedSecondary);
        PairUtils.assertValues(pair, modifiedPrimary, modifiedSecondary);

    }

    @Test
    void testEquality() {
        Object primary = new Object();
        Object secondary = new Object();

        Map.Entry<Object, Object> entry = new AbstractMap.SimpleEntry<>(primary, secondary);
        OptionalPair<Object, Object> optionalPair = new OptionalPair<>(primary, secondary);
        Pair<Object, Object> pair = new Pair<>(primary, secondary);
        Pair<Object, Object> pairFromOptional = new Pair<>(optionalPair);
        Pair<Object, Object> pairFromEntry = new Pair<>(entry);

        Assertions.assertEquals(pair, pairFromOptional);
        Assertions.assertEquals(pair.hashCode(), pairFromOptional.hashCode());
        Assertions.assertEquals(pair, pairFromEntry);
        Assertions.assertEquals(pair.hashCode(), pairFromOptional.hashCode());
        Assertions.assertEquals(pair, pair);

        Assertions.assertNotEquals(null, pair);
        Assertions.assertEquals(pair, new Pair<>(primary, secondary));
        Assertions.assertNotEquals(pair, new Pair<>(primary, new Object()));
        Assertions.assertNotEquals(pair, new Pair<>(new Object(), secondary));

        Pair<Object, Integer> differentTypePair = new Pair<>(primary, 10);
        Assertions.assertNotEquals(pair, differentTypePair);

    }

    @Test
    void testToString() {
        Pair<Integer, Integer> pair = new Pair<>(1, 2);
        String expected = "Pair(firstValue=1, secondValue=2)";
        Assertions.assertEquals(expected, pair.toString());
    }



}
