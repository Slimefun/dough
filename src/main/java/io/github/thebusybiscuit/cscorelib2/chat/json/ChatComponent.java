package io.github.thebusybiscuit.cscorelib2.chat.json;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.thebusybiscuit.cscorelib2.reflection.Packets;
import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;
import lombok.Getter;
import lombok.NonNull;

public class ChatComponent {

    private static Constructor<?> packetConstructor;
    // private static Constructor<?> actionbarConstructor;
    private static Class<?> serializerClass;
    private static Method serializerMethod;

    static {
        try {
            packetConstructor = ReflectionUtils.getNMSClass("PacketPlayOutChat").getConstructor(ReflectionUtils.getNMSClass("IChatBaseComponent"));
            // actionbarConstructor =
            // ReflectionUtils.getNMSClass("PacketPlayOutChat").getConstructor(ReflectionUtils.getNMSClass("IChatBaseComponent"),
            // byte.class);
            serializerClass = ReflectionUtils.getInnerNMSClass("IChatBaseComponent", "ChatSerializer");
            serializerMethod = ReflectionUtils.getMethod(serializerClass, "a", JsonElement.class);
        }
        catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
            System.err.println("Perhaps you forgot to shade CS-CoreLib's \"reflection\" package?");
            e.printStackTrace();
        }
    }

    private final JsonObject json;

    @Getter
    private final Map<NamespacedKey, Consumer<Player>> clickables = new HashMap<>();

    public ChatComponent(@NonNull String text) {
        json = new JsonObject();
        json.addProperty("text", text);
    }

    public void setColor(@NonNull ChatComponentColor color) {
        json.addProperty("color", color.toString());
    }

    public void setBold(boolean bold) {
        json.addProperty("bold", bold);
    }

    public void setItalic(boolean italic) {
        json.addProperty("italic", italic);
    }

    public void setUnderlined(boolean underlined) {
        json.addProperty("underlined", underlined);
    }

    public void setStrikethrough(boolean strikethrough) {
        json.addProperty("strikethrough", strikethrough);
    }

    public void setObfuscated(boolean obfuscated) {
        json.addProperty("obfuscated", obfuscated);
    }

    public void setHoverEvent(@NonNull HoverEvent hoverEvent) {
        json.add("hoverEvent", hoverEvent.asJson());
    }

    public void setClickEvent(@NonNull ClickEvent clickEvent) {
        json.add("clickEvent", clickEvent.asJson());

        ClickableText clickable = clickEvent.getClickableText();
        if (clickable != null) {
            clickables.put(clickable.getKey(), clickable.getCallback());
        }
    }

    public void append(@NonNull ChatComponent component) {
        JsonArray array;

        if (json.has("extra")) {
            array = json.get("extra").getAsJsonArray();
        }
        else {
            array = new JsonArray();
            json.add("extra", array);
        }

        clickables.putAll(component.getClickables());
        array.add(component.asJson());
    }

    public int getAttachments() {
        return json.has("extra") ? json.get("extra").getAsJsonArray().size() : 0;
    }

    public JsonObject asJson() {
        return json;
    }

    private Object getPacket() {
        try {
            return packetConstructor.newInstance(getAsNMSComponent());
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object getActionBarPacket() {
        return null;
        // try {
        // return actionbarConstructor.newInstance(getAsNMSComponent(), (byte) 2);
        // } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException |
        // InstantiationException e) {
        // e.printStackTrace();
        // return null;
        // }
    }

    public Object getAsNMSComponent() {
        try {
            return serializerMethod.invoke(serializerClass, json);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendMessage(Player... players) {
        if (players.length == 0) return;

        Object packet = getPacket();
        if (packet == null) return;

        for (Player p : players) {
            try {
                Packets.send(p, packet);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendActionBar(Player... players) {
        if (players.length == 0) return;

        Object packet = getActionBarPacket();
        if (packet == null) return;

        for (Player p : players) {
            try {
                Packets.send(p, packet);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}
