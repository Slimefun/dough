package io.github.bakedlibs.dough.data.persistent;

import be.seeseemelk.mockbukkit.persistence.PersistentDataContainerMock;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestPersistentJson {

    @SuppressWarnings("deprecation")
    @Test
    void testSerialization() {

        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < 100; i++) {
            jsonArray.add(i);
        }
        JsonObject jsonObject = new JsonObject();
        JsonPrimitive primitiveString = new JsonPrimitive("aString");
        jsonObject.add("string", primitiveString);
        PersistentDataContainer pdc = new PersistentDataContainerMock();
        NamespacedKey keyString = new NamespacedKey("dummy", "string");
        NamespacedKey keyArray = new NamespacedKey("dummy", "array");
        pdc.set(keyString, PersistentJsonDataType.JSON_OBJECT, jsonObject);
        pdc.set(keyArray, PersistentJsonDataType.JSON_ARRAY, jsonArray);

        Assertions.assertTrue(pdc.has(keyString, PersistentJsonDataType.JSON_OBJECT));
        JsonObject deserialized = pdc.get(keyString, PersistentJsonDataType.JSON_OBJECT);
        Assertions.assertNotNull(deserialized);
        Assertions.assertEquals(jsonObject, deserialized);

        Assertions.assertTrue(pdc.has(keyArray, PersistentJsonDataType.JSON_ARRAY));
        JsonArray deserializedArray = pdc.get(keyArray, PersistentJsonDataType.JSON_ARRAY);
        Assertions.assertNotNull(deserializedArray);
        Assertions.assertEquals(jsonArray, deserializedArray);

    }

}
