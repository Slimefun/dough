package io.github.bakedlibs.dough.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

class TestRandomizedSet {

    @Test
    void testModification() {
        RandomizedSet<Integer> randomizedSet = new RandomizedSet<>();
        Assertions.assertTrue(randomizedSet.isEmpty());
        Assertions.assertEquals(0, randomizedSet.size());
        Assertions.assertThrows(IllegalArgumentException.class, () -> randomizedSet.add(1, 0), "A Weight may never be less than or equal to zero!");
        Assertions.assertTrue(randomizedSet.add(1, 1));
        Assertions.assertEquals(1, randomizedSet.size());
        Assertions.assertEquals(1, randomizedSet.sumWeights());
        Assertions.assertThrows(IllegalArgumentException.class, () -> randomizedSet.setWeight(1, 0), "A Weight may never be less than or equal to zero!");
        randomizedSet.setWeight(1, 0.5f);
        Assertions.assertEquals(0.5f, randomizedSet.sumWeights());
        randomizedSet.setWeight(1, 1);
        Assertions.assertThrows(IllegalStateException.class, () -> randomizedSet.setWeight(2, 1), "The specified Object is not contained in this Set");
        Assertions.assertFalse(randomizedSet.add(1, 2));
        // There is only one element in the set.
        Assertions.assertEquals(1, randomizedSet.getRandom());
        Assertions.assertTrue(randomizedSet.remove(1));
        Assertions.assertNull(randomizedSet.getRandom());
        Assertions.assertFalse(randomizedSet.remove(1));
        randomizedSet.add(1, 1f);
        randomizedSet.clear();
        Assertions.assertTrue(randomizedSet.isEmpty());
        Assertions.assertEquals(0, randomizedSet.sumWeights());

    }

    @Test
    void testProbability() {
        RandomizedSet<Integer> randomizedSet = new RandomizedSet<>();
        randomizedSet.add(1, 1);
        randomizedSet.add(2, 1);
        Assertions.assertEquals(2, randomizedSet.sumWeights());
        Map<Integer, Float> expectedWeights = new HashMap<>();
        expectedWeights.put(1, 0.5f);
        expectedWeights.put(2, 0.5f);
        CollectionTestUtils.assertHaveEqualElements(expectedWeights, randomizedSet.toMap());
        int ones = 0;
        int twos = 0;
        for (int i = 0; i < 1000; i++) {
            if (randomizedSet.getRandom() == 1) {
                ones++;
            } else {
                twos++;
            }
        }
        // +- 10%
        Assertions.assertEquals(500, ones, 50);
        Assertions.assertEquals(500, twos, 50);
    }

    @Test
    void testRandomSubset() {
        // 1, 2, 3, and 4 have an equal weight.
        RandomizedSet<Integer> randomizedSet = new RandomizedSet<>(Arrays.asList(1, 2, 3, 4));
        Set<Integer> set = randomizedSet.getRandomSubset(2);
        Assertions.assertEquals(2, set.size());
        Assertions.assertTrue(Arrays.asList(1, 2, 3, 4).containsAll(set));
        Assertions.assertThrows(IllegalArgumentException.class, () -> randomizedSet.getRandomSubset(5), "A random Subset may not be larger than the original Set! (5 > 4");
        Set<Integer> sameSizeSet = randomizedSet.getRandomSubset(4);
        Assertions.assertEquals(4, sameSizeSet.size());
        CollectionTestUtils.assertHaveEqualElements(new HashSet<>(Arrays.asList(1, 2, 3, 4)), sameSizeSet);
        CollectionTestUtils.assertHaveEqualElements(sameSizeSet, Arrays.asList(randomizedSet.toArray(Integer[]::new)));
    }
}
