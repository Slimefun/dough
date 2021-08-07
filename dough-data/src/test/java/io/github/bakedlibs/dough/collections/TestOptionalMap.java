package io.github.bakedlibs.dough.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class TestOptionalMap {

    @Test
    void testNullSupplier() {
        Assertions.assertThrows(IllegalStateException.class, () -> new OptionalMap<>(() -> null), "Internal Map is not allowed to be null!");
    }

    @Test
    void testGetters() {
        Map<Integer, Integer> map = Collections.emptyMap();
        OptionalMap<Integer, Integer> optionalMap = new OptionalMap<>(() -> map);
        Assertions.assertFalse(optionalMap.get(1).isPresent());
        Assertions.assertFalse(optionalMap.containsKey(1));
        Assertions.assertFalse(optionalMap.containsValue(1));
        Assertions.assertEquals(0, optionalMap.size());
        Assertions.assertSame(map.keySet(), optionalMap.keySet());
        Assertions.assertSame(map.entrySet(), optionalMap.entrySet());
        Assertions.assertSame(map.values(), optionalMap.values());
        Assertions.assertSame(map.entrySet().iterator(), optionalMap.iterator());
        Assertions.assertSame(map, optionalMap.getInternalMap());
        Assertions.assertEquals(optionalMap, map);
        Assertions.assertEquals(map.hashCode(), optionalMap.hashCode());
        Assertions.assertEquals(1, optionalMap.getOrDefault(10, 1));
    }

    @Test
    void testMapModification() {
        OptionalMap<Integer, Integer> map = new OptionalMap<>(HashMap::new);
        map.put(1, 1);
        Assertions.assertTrue(map.containsKey(1));
        Assertions.assertTrue(map.get(1).isPresent());
        Assertions.assertEquals(1, map.get(1).orElse(null));
        Assertions.assertEquals(1, map.size());
        map.clear();
        Assertions.assertEquals(0, map.size());
        Assertions.assertTrue(map.isEmpty());
        map.put(1, 1);
        Assertions.assertThrows(RuntimeException.class,
                () -> map.ifPresent(1, x -> {
                    throw new RuntimeException("Value = " + x);
                }), "Value = 1");
        map.remove(1);
        Assertions.assertThrows(RuntimeException.class,
                () -> map.ifAbsent(1, v -> {
                    throw new RuntimeException("Value not found!");
                }), "Value not found!");
        Assertions.assertEquals(2, map.computeIfAbsent(2, k -> k));
        Assertions.assertEquals(3, map.computeIfPresent(2, (k, v) -> v + 1));
        Assertions.assertNull(map.compute(4, (k, v) -> null));
        Assertions.assertEquals(4, map.compute(4, (k, v) -> k));
        // The value in the map for key=4 is 4, here we merge the values by summing them together.
        Assertions.assertEquals(8, map.merge(4, 4, Integer::sum));
        // Since the value mapped for key=4 is 8, we check to see if 8 is whats returned.
        Assertions.assertEquals(8, map.putIfAbsent(4, 4));
        // No value for key=5 so it should be null
        Assertions.assertNull(map.putIfAbsent(5, 5));
        Assertions.assertEquals(5, map.get(5).orElse(null));
        Map<Integer, Integer> map2 = new HashMap<>();
        map2.put(6, 6);
        map2.put(7, 7);
        map.putAll(map2);
        Assertions.assertEquals(6, map.get(6).orElse(null));
        Assertions.assertEquals(7, map.get(7).orElse(null));
    }



}
