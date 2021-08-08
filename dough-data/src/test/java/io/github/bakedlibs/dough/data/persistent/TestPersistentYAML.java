package io.github.bakedlibs.dough.data.persistent;

import be.seeseemelk.mockbukkit.persistence.PersistentDataContainerMock;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.persistence.PersistentDataContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestPersistentYAML {

    @Test
    @SuppressWarnings("deprecation")
    void testSerialization() {
        FileConfiguration configuration = new YamlConfiguration();
        configuration.set("a", "b");
        PersistentDataContainer pdc = new PersistentDataContainerMock();
        NamespacedKey key = new NamespacedKey("dummy", "key");
        pdc.set(key, PersistentYAMLDataType.CONFIG, configuration);
        Assertions.assertTrue(pdc.has(key, PersistentYAMLDataType.CONFIG));
        FileConfiguration deserialized = pdc.get(key, PersistentYAMLDataType.CONFIG);
        Assertions.assertNotNull(deserialized);
        Assertions.assertEquals("b", deserialized.get("a"));
    }

    @Test
    void testTypeInformation() {
        Assertions.assertEquals(FileConfiguration.class, PersistentYAMLDataType.CONFIG.getComplexType());
        Assertions.assertEquals(String.class, PersistentYAMLDataType.CONFIG.getPrimitiveType());
    }

}
