package io.github.bakedlibs.dough.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Map;

class TestOptionalPair {

    @Test
    void testGettersAndSetters() {

        Object primary = new Object();

        Map.Entry<Object, Object> entry = new AbstractMap.SimpleEntry<>(primary, null);
        OptionalPair<Object, Object> pair = new OptionalPair<>(primary, null);
        OptionalPair<Object, Object> pairFromEntry = new OptionalPair<>(entry);

        PairUtils.assertValues(pair, primary, null);
        PairUtils.assertValues(pairFromEntry, primary, null);

        Object modifiedSecondary = new Object();

        pair.setFirstValue(null);
        pair.setSecondValue(modifiedSecondary);
        PairUtils.assertValues(pair, null, modifiedSecondary);

    }

    @Test
    void testEquality() {

        Object primary = new Object();
        Object secondary = new Object();

        Map.Entry<Object, Object> entry = new AbstractMap.SimpleEntry<>(primary, secondary);
        OptionalPair<Object, Object> optionalPair = new OptionalPair<>(primary, secondary);
        OptionalPair<Object, Object> pairFromEntry = new OptionalPair<>(entry);
        OptionalPair<Object, Object> optionalPairFromPair = new OptionalPair<>(new Pair<>(primary, secondary));

        Assertions.assertTrue(optionalPairFromPair.getFirstValue().isPresent());
        Assertions.assertTrue(optionalPairFromPair.getSecondValue().isPresent());

        Assertions.assertEquals(optionalPair, optionalPairFromPair);
        Assertions.assertEquals(optionalPair.hashCode(), optionalPairFromPair.hashCode());
        Assertions.assertEquals(optionalPair, pairFromEntry);
        Assertions.assertEquals(optionalPair.hashCode(), optionalPairFromPair.hashCode());
        Assertions.assertEquals(optionalPair, optionalPair);

        Assertions.assertNotEquals(null, optionalPair);
        Assertions.assertEquals(optionalPair, new OptionalPair<>(primary, secondary));
        Assertions.assertNotEquals(optionalPair, new OptionalPair<>(primary, null));
        Assertions.assertNotEquals(optionalPair, new OptionalPair<>(null, secondary));

        OptionalPair<Object, Integer> differentTypePair = new OptionalPair<>(primary, 10);
        Assertions.assertNotEquals(optionalPair, differentTypePair);

    }

    @Test
    void testToString() {
        OptionalPair<Integer, Integer> pair = new OptionalPair<>(1, null);
        String expected = "OptionalPair(firstValue=Optional[1], secondValue=Optional.empty)";
        Assertions.assertEquals(expected, pair.toString());
    }



}
