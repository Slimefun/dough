package io.github.thebusybiscuit.cscorelib2.chat.json;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
	
	public HoverEvent(String... text) {
		json = new JsonObject();
		json.addProperty("action", "show_text");
		json.addProperty("value", String.join("\n", text));
	}
	
	public HoverEvent(ItemStack item) {
		json = new JsonObject();
		json.addProperty("action", "show_item");

		try {
			Object nbt = getNBT.invoke(copy.invoke(null, item), nbtConstructor.newInstance());
			json.addProperty("value", nbt.toString());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
		}
		
	}
	
	public JsonObject asJson() {
		return json;
	}

}
