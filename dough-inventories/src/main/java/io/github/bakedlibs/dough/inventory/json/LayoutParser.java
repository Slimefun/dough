package io.github.bakedlibs.dough.inventory.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import io.github.bakedlibs.dough.inventory.MenuLayout;
import io.github.bakedlibs.dough.inventory.SlotGroup;
import io.github.bakedlibs.dough.inventory.builders.MenuLayoutBuilder;
import io.github.bakedlibs.dough.inventory.builders.SlotGroupBuilder;

/**
 * This class allows you to parse {@link JsonObject}s, {@link String}s or {@link InputStream}s
 * into a {@link MenuLayout}, given they follow our format.
 * 
 * @author TheBusyBiscuit
 *
 */
public class LayoutParser {

    private LayoutParser() {}

    @ParametersAreNonnullByDefault
    public static @Nonnull MenuLayout parseStream(InputStream stream, Consumer<SlotGroupBuilder> slotGroups) throws InvalidLayoutException {
        Validate.notNull(stream, "InputStream must not be null");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            return parseString(reader.lines().collect(Collectors.joining("")), slotGroups);
        } catch (IOException x) {
            throw new InvalidLayoutException(x);
        }
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull MenuLayout parseStream(InputStream stream) throws InvalidLayoutException {
        return parseStream(stream, builder -> {});
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull MenuLayout parseString(String string, Consumer<SlotGroupBuilder> slotGroups) throws InvalidLayoutException {
        Validate.notNull(string, "String must not be null");

        try {
            JsonParser parser = new JsonParser();
            JsonObject root = parser.parse(string).getAsJsonObject();
            return parseJson(root, slotGroups);
        } catch (IllegalStateException | JsonParseException x) {
            throw new InvalidLayoutException(x);
        }
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull MenuLayout parseString(String string) throws InvalidLayoutException {
        return parseString(string, builder -> {});
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull MenuLayout parseJson(JsonObject json, Consumer<SlotGroupBuilder> slotGroups) throws InvalidLayoutException {
        Validate.notNull(json, "JsonObject must not be null");

        try {
            LayoutShape shape = parseShape(json);
            MenuLayoutBuilder builder = new MenuLayoutBuilder(shape.getSize());

            for (Map.Entry<Character, Set<Integer>> entry : shape.getGroups().entrySet()) {
                builder.addSlotGroup(parseGroup(json, entry.getKey(), entry.getValue(), slotGroups));
            }

            return builder.build();
        } catch (Exception x) {
            throw new InvalidLayoutException(x);
        }
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull MenuLayout parseJson(JsonObject json) throws InvalidLayoutException {
        return parseJson(json, builder -> {});
    }

    @ParametersAreNonnullByDefault
    private static @Nonnull LayoutShape parseShape(JsonObject json) throws InvalidLayoutException {
        String attribute = "layout";

        if (!json.has(attribute) || !json.get(attribute).isJsonArray()) {
            throw new InvalidLayoutException("Missing 'layout' child!");
        }

        JsonArray array = json.getAsJsonArray(attribute);
        String[] rows = new String[array.size()];

        if (rows.length == 0) {
            throw new InvalidLayoutException("'layout' is empty!");
        }

        int i = 0;
        for (JsonElement row : array) {
            if (row.isJsonPrimitive() && row.getAsJsonPrimitive().isString()) {
                rows[i] = row.getAsString();
                i++;
            } else {
                throw new InvalidLayoutException("Expected String in layout, found: " + row);
            }
        }

        return new LayoutShape(rows);
    }

    @ParametersAreNonnullByDefault
    private static @Nonnull SlotGroup parseGroup(JsonObject json, char identifier, Set<Integer> slots, Consumer<SlotGroupBuilder> consumer) throws InvalidLayoutException {
        String attribute = "groups";

        if (!json.has(attribute) || !json.get(attribute).isJsonObject()) {
            throw new InvalidLayoutException("Missing 'groups' child!");
        }

        JsonObject groups = json.getAsJsonObject(attribute);
        String key = String.valueOf(identifier);

        if (!groups.has(key) || !groups.get(key).isJsonObject()) {
            throw new InvalidLayoutException("Missing 'groups." + identifier + "' child!");
        }

        JsonObject group = groups.getAsJsonObject(key);
        JsonElement name = group.get("name");

        if (name == null) {
            throw new InvalidLayoutException("Slot group '" + identifier + "' has no name!");
        }

        SlotGroupBuilder builder = new SlotGroupBuilder(identifier, name.getAsString());

        JsonElement interactable = group.get("interactable");

        if (interactable != null && interactable.isJsonPrimitive() && interactable.getAsJsonPrimitive().isBoolean()) {
            builder.interactable(interactable.getAsBoolean());
        } else {
            throw new InvalidLayoutException("Slot group '" + identifier + "' has no valid 'interactable' attribute!");
        }

        builder.withSlots(slots.stream().mapToInt(Integer::intValue).toArray());
        consumer.accept(builder);

        return builder.build();
    }

}
