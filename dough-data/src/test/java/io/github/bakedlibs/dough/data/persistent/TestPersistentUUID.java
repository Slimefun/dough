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

}
