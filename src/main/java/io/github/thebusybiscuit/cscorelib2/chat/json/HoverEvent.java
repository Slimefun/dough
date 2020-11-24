package io.github.thebusybiscuit.cscorelib2.chat.json;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonObject;

import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;

public class HoverEvent {

    private static Constructor<?> nbtConstructor;
    private static Method copy;
    private static Method getNBT;

    static {
        try {
            copy = ReflectionUtils.getOBCClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class);
            nbtConstructor = ReflectionUtils.getNMSClass("NBTTagCompound").getConstructor();
            getNBT = ReflectionUtils.getNMSClass("ItemStack").getMethod("save", nbtConstructor.getDeclaringClass());
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
            System.err.println("Perhaps you forgot to shade CS-CoreLib's \"reflection\" package?");
            e.printStackTrace();
        }
    }

    private final JsonObject json;

    public HoverEvent(String... lines) {
        this("show_text", String.join("\n", lines));
    }

    public HoverEvent(Collection<String> lines) {
        this("show_text", String.join("\n", lines));
    }

    public HoverEvent(ItemStack item) {
        this("show_item", encodeItemStack(item));
    }

    private HoverEvent(String type, String value) {
        json = new JsonObject();
        json.addProperty("action", type);
        json.addProperty("value", value);
    }

    private static String encodeItemStack(ItemStack item) {
        try {
            Object nbt = getNBT.invoke(copy.invoke(null, item), nbtConstructor.newInstance());
            return nbt.toString();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public JsonObject asJson() {
        return json;
    }

}
