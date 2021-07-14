package io.github.bakedlibs.dough.data.persistent;

import javax.annotation.Nonnull;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public final class PersistentJsonDataType<T extends JsonElement> implements PersistentDataType<String, T> {

    public static final PersistentJsonDataType<JsonObject> JSON_OBJECT = new PersistentJsonDataType<>(JsonObject.class);
    public static final PersistentJsonDataType<JsonArray> JSON_ARRAY = new PersistentJsonDataType<>(JsonArray.class);

    private final Class<T> jsonClass;
    private final JsonParser parser = new JsonParser();

    public PersistentJsonDataType(@Nonnull Class<T> jsonClass) {
        this.jsonClass = jsonClass;
    }

    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public Class<T> getComplexType() {
        return jsonClass;
    }

    @Override
    public String toPrimitive(JsonElement complex, PersistentDataAdapterContext context) {
        return complex.toString();
    }

    @Override
    public T fromPrimitive(String primitive, PersistentDataAdapterContext context) {
        JsonElement json = parser.parse(primitive);

        if (jsonClass.isInstance(json)) {
            return jsonClass.cast(json);
        } else {
            return null;
        }
    }

}
