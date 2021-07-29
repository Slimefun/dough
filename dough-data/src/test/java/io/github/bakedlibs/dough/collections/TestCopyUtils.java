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

    private static <T> boolean haveSameElements(@Nonnull Collection<T> c1, @Nonnull Collection<T> c2) {
        if (c1.size() != c2.size()) {
            return false;
        }
        return c1.containsAll(c2);
    }

    private static <K, V> boolean haveSameElements(@Nonnull Map<K, V> m1, @Nonnull Map<K, V> m2) {
        if (m1.size() != m2.size()) {
            return false;
        }
        for (Map.Entry<K, V> entry : m1.entrySet()) {
            if (!Objects.equals(m2.get(entry.getKey()), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    private static <T> boolean haveClonedElements(@Nonnull Collection<T> c1, @Nonnull Collection<T> c2) {
        if (c1.size() != c2.size()) {
            return false;
        }
        for (T t1 : c1) {
            for (T t2 : c2) {
                // If they are the same, it did not clone and thus we fail.
                if (t1 == t2) {
                    return false;
                } else if (t1.equals(t2)) {
                    // If they are equal, the element has been cloned, thus, we break and check the next element.
                    break;
                }
            }
        }
        return true;
    }

    private static <K, V> boolean haveClonedElements(@Nonnull Map<K, V> m1, @Nonnull Map<K, V> m2) {
        if (m1.size() != m2.size()) {
            return false;
        }
        for (Map.Entry<K, V> entry : m1.entrySet()) {
            V otherValue = m2.get(entry.getKey());
            // If they are the same, it did not clone and thus we fail.
            if (entry.getValue() == otherValue) {
                return false;
            } else if (entry.getValue().equals(otherValue)) {
                // If they are equal, the element has been cloned, thus, we break and check the next element.
                break;
            }
        }
        return true;
    }


    @Test
    @DisplayName("Test if collections are deeply cloned properly")
    void testCloningCollections() {
        Collection<DummyData> dummyCollection =
            Arrays.asList(new DummyData(1), new DummyData(2), new DummyData(3));
        Collection<DummyData> clonedCollection =
            CopyUtils.deepCopy(dummyCollection, DummyData::clone, ArrayList::new);
        Assertions.assertTrue(haveSameElements(dummyCollection, clonedCollection));
        Assertions.assertTrue(haveClonedElements(dummyCollection, clonedCollection));
    }

    @Test
    @DisplayName("Test if maps are deeply cloned properly")
    void testCloningMaps() {
        Map<Integer, DummyData> dummyMap = new HashMap<>();
        dummyMap.put(1, new DummyData(1));
        dummyMap.put(2, new DummyData(2));
        dummyMap.put(3, new DummyData(3));
        Map<Integer, DummyData> clonedMap = CopyUtils.deepCopy(dummyMap, DummyData::clone, HashMap::new);
        Assertions.assertTrue(haveSameElements(dummyMap, clonedMap));
        Assertions.assertTrue(haveClonedElements(dummyMap, clonedMap));
        // We first perform a shallow copy from clonedMap
        Map<Integer, DummyData> clonedMapDeepCopy = new HashMap<>(clonedMap);
        // We then mutate the shallow copy and turn it into a deep copy.
        CopyUtils.deepCopy(clonedMapDeepCopy, DummyData::clone);
        Assertions.assertTrue(haveSameElements(clonedMap, clonedMapDeepCopy));
        Assertions.assertTrue(haveClonedElements(clonedMap, clonedMapDeepCopy));
    }

    @Test
    @DisplayName("Test if arrays are deeply cloned properly")
    void testCloningArrays() {
        DummyData[] dummyArray = new DummyData[]{new DummyData(1), new DummyData(2), new DummyData(3)};
        DummyData[] clonedArray = CopyUtils.deepCopy(dummyArray, DummyData::clone, DummyData[]::new);
        Assertions.assertTrue(haveSameElements(Arrays.asList(dummyArray), Arrays.asList(clonedArray)));
        Assertions.assertTrue(haveClonedElements(Arrays.asList(dummyArray), Arrays.asList(clonedArray)));
        DummyData[] deepClonedArray =  new DummyData[clonedArray.length];
        CopyUtils.deepCopy(clonedArray, DummyData::clone, deepClonedArray);
        Assertions.assertTrue(haveSameElements(Arrays.asList(dummyArray), Arrays.asList(deepClonedArray)));
        Assertions.assertTrue(haveClonedElements(Arrays.asList(dummyArray), Arrays.asList(deepClonedArray)));
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
