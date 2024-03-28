package io.github.bakedlibs.dough.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class TestCopyUtils {

    private static <T> void assertHaveEqualElements(@Nonnull Collection<T> c1, @Nonnull Collection<T> c2) {
        Assertions.assertEquals(c1.size(), c2.size());
        Assertions.assertTrue(c1.containsAll(c2));
    }

    private static <K, V> void assertHaveEqualElements(@Nonnull Map<K, V> m1, @Nonnull Map<K, V> m2) {
        Assertions.assertEquals(m1.size(), m2.size());
        for (Map.Entry<K, V> entry : m1.entrySet()) {
            Assertions.assertEquals(m2.get(entry.getKey()), entry.getValue());
        }
    }

    private static <T> void assertHaveClonedElements(@Nonnull Collection<T> c1, @Nonnull Collection<T> c2) {
        Assertions.assertEquals(c1.size(), c2.size());
        for (T t1 : c1) {
            for (T t2 : c2) {
                // If they are the same, it did not clone and thus we fail.
                Assertions.assertNotSame(t1, t2);
                if (t1.equals(t2)) {
                    // If they are equal, the element has been cloned, thus, we break and check the next element.
                    break;
                }
            }
        }
    }

    private static <K, V> void assertHaveClonedElements(@Nonnull Map<K, V> m1, @Nonnull Map<K, V> m2) {
        Assertions.assertEquals(m1.size(), m2.size());
        for (Map.Entry<K, V> entry : m1.entrySet()) {
            V otherValue = m2.get(entry.getKey());
            // If they are the same, it did not clone and thus we fail.
            Assertions.assertNotSame(entry.getValue(), otherValue);
            if (entry.getValue().equals(otherValue)) {
                // If they are equal, the element has been cloned, thus, we break and check the next element.
                break;
            }
        }
    }


    @Test
    @DisplayName("Test if collections are deeply cloned properly")
    void testCloningCollections() {
        Collection<DummyData> dummyCollection =
            Arrays.asList(new DummyData(1), new DummyData(2), new DummyData(3));
        Collection<DummyData> clonedCollection =
            CopyUtils.deepCopy(dummyCollection, DummyData::clone, ArrayList::new);
        assertHaveEqualElements(dummyCollection, clonedCollection);
        assertHaveClonedElements(dummyCollection, clonedCollection);
    }

    @Test
    @DisplayName("Test if maps are deeply cloned properly")
    void testCloningMaps() {
        Map<Integer, DummyData> dummyMap = new HashMap<>();
        dummyMap.put(1, new DummyData(1));
        dummyMap.put(2, new DummyData(2));
        dummyMap.put(3, new DummyData(3));
        Map<Integer, DummyData> clonedMap = CopyUtils.deepCopy(dummyMap, DummyData::clone, HashMap::new);
        assertHaveEqualElements(dummyMap, clonedMap);
        assertHaveClonedElements(dummyMap, clonedMap);
        // We first perform a shallow copy from clonedMap
        Map<Integer, DummyData> clonedMapDeepCopy = new HashMap<>(clonedMap);
        // We then mutate the shallow copy and turn it into a deep copy.
        CopyUtils.deepCopy(clonedMapDeepCopy, DummyData::clone);
        assertHaveEqualElements(clonedMap, clonedMapDeepCopy);
        assertHaveClonedElements(clonedMap, clonedMapDeepCopy);
    }

    @Test
    @DisplayName("Test if arrays are deeply cloned properly")
    void testCloningArrays() {
        DummyData[] dummyArray = new DummyData[]{new DummyData(1), new DummyData(2), new DummyData(3)};
        DummyData[] clonedArray = CopyUtils.deepCopy(dummyArray, DummyData::clone, DummyData[]::new);
        assertHaveEqualElements(Arrays.asList(dummyArray), Arrays.asList(clonedArray));
        assertHaveClonedElements(Arrays.asList(dummyArray), Arrays.asList(clonedArray));
        DummyData[] deepClonedArray =  new DummyData[clonedArray.length];
        CopyUtils.deepCopy(clonedArray, DummyData::clone, deepClonedArray);
        assertHaveEqualElements(Arrays.asList(dummyArray), Arrays.asList(deepClonedArray));
        assertHaveClonedElements(Arrays.asList(dummyArray), Arrays.asList(deepClonedArray));
        // Cannot clone if the length of the sink < length of source
        DummyData[] invalidArray = new DummyData[clonedArray.length - 1];
        Assertions.assertThrows(IllegalArgumentException.class, () -> CopyUtils.deepCopy(dummyArray, DummyData::clone, invalidArray));
        try {
            DummyData[] validArray = new DummyData[clonedArray.length + 1];
            CopyUtils.deepCopy(dummyArray, DummyData::clone, validArray);
        } catch (Exception ex) {
            // Unexpected failure
            Assertions.fail(ex);
        }
    }

}
