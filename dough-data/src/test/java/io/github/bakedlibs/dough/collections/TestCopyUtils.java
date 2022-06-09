package io.github.bakedlibs.dough.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class TestCopyUtils {

    @Test
    @DisplayName("Test if collections are deeply cloned properly")
    void testCloningCollections() {
        Collection<DummyData> dummyCollection =
            Arrays.asList(new DummyData(1), new DummyData(2), new DummyData(3));
        Collection<DummyData> clonedCollection =
            CopyUtils.deepCopy(dummyCollection, DummyData::clone, ArrayList::new);
        CollectionTestUtils.assertHaveEqualElements(dummyCollection, clonedCollection);
        CollectionTestUtils.assertHaveClonedElements(dummyCollection, clonedCollection);
    }

    @Test
    @DisplayName("Test if maps are deeply cloned properly")
    void testCloningMaps() {
        Map<Integer, DummyData> dummyMap = new HashMap<>();
        dummyMap.put(1, new DummyData(1));
        dummyMap.put(2, new DummyData(2));
        dummyMap.put(3, new DummyData(3));
        Map<Integer, DummyData> clonedMap = CopyUtils.deepCopy(dummyMap, DummyData::clone, HashMap::new);
        CollectionTestUtils.assertHaveEqualElements(dummyMap, clonedMap);
        CollectionTestUtils.assertHaveClonedElements(dummyMap, clonedMap);
        // We first perform a shallow copy from clonedMap
        Map<Integer, DummyData> clonedMapDeepCopy = new HashMap<>(clonedMap);
        // We then mutate the shallow copy and turn it into a deep copy.
        CopyUtils.deepCopy(clonedMapDeepCopy, DummyData::clone);
        CollectionTestUtils.assertHaveEqualElements(clonedMap, clonedMapDeepCopy);
        CollectionTestUtils.assertHaveClonedElements(clonedMap, clonedMapDeepCopy);
    }

    @Test
    @DisplayName("Test if arrays are deeply cloned properly")
    void testCloningArrays() {
        DummyData[] dummyArray = new DummyData[]{new DummyData(1), new DummyData(2), new DummyData(3)};
        DummyData[] clonedArray = CopyUtils.deepCopy(dummyArray, DummyData::clone, DummyData[]::new);
        CollectionTestUtils.assertHaveEqualElements(Arrays.asList(dummyArray), Arrays.asList(clonedArray));
        CollectionTestUtils.assertHaveClonedElements(Arrays.asList(dummyArray), Arrays.asList(clonedArray));
        DummyData[] deepClonedArray =  new DummyData[clonedArray.length];
        CopyUtils.deepCopy(clonedArray, DummyData::clone, deepClonedArray);
        CollectionTestUtils.assertHaveEqualElements(Arrays.asList(dummyArray), Arrays.asList(deepClonedArray));
        CollectionTestUtils.assertHaveClonedElements(Arrays.asList(dummyArray), Arrays.asList(deepClonedArray));
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
