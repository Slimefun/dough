package io.github.bakedlibs.dough.data.persistent;

import be.seeseemelk.mockbukkit.persistence.PersistentDataContainerMock;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class TestPersistentUUID {

    @SuppressWarnings("deprecation")
    @Test
    void testSerialization() {
        UUID uuid = UUID.randomUUID();
        PersistentDataContainer pdc = new PersistentDataContainerMock();
        NamespacedKey key = new NamespacedKey("dummy", "key");

        pdc.set(key, PersistentUUIDDataType.TYPE, uuid);
        Assertions.assertEquals(uuid, pdc.get(key, PersistentUUIDDataType.TYPE));

    }

    @Test
    void testInvalidCases() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> PersistentUUIDDataType.fromIntArray(null), "The provided integer array cannot be null!");
        int[] invalidData = new int[0];
        Assertions.assertThrows(IllegalArgumentException.class, () -> PersistentUUIDDataType.fromIntArray(invalidData), "The integer array must have a length of 4.");
        Assertions.assertThrows(IllegalArgumentException.class, () -> PersistentUUIDDataType.toIntArray(null), "The provided uuid cannot be null!");
    }

    @Test
    void testTypeInformation() {
        Assertions.assertEquals(UUID.class, PersistentUUIDDataType.TYPE.getComplexType());
        Assertions.assertEquals(int[].class, PersistentUUIDDataType.TYPE.getPrimitiveType());
    }

}
