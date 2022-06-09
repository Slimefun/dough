package io.github.bakedlibs.dough.collections;

import org.bukkit.NamespacedKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class TestKeyMap {

    @Test
    @DisplayName("Test if the add method uses the correct key")
    @SuppressWarnings("deprecation")
    void testAdd() {
        NamespacedKey key = new NamespacedKey("abc", "def");
        DummyKeyed dummyKeyed = new DummyKeyed(key);
        KeyMap<DummyKeyed> keyMap1 = new KeyMap<>();
        keyMap1.add(dummyKeyed);
        Assertions.assertEquals(dummyKeyed, keyMap1.get(dummyKeyed.getKey()).orElse(null));
    }

    @Test
    @DisplayName("Test if the constructor uses the output of the supplier")
    void testConstructor() {
        Map<NamespacedKey, DummyKeyed> map = new HashMap<>();
        KeyMap<DummyKeyed> keyMap2 = new KeyMap<>(() -> map);
        Assertions.assertSame(map, keyMap2.getInternalMap());
    }

}
