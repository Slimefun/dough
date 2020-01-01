package io.github.thebusybiscuit.cscorelib2.chat.json;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.entity.Player;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.thebusybiscuit.cscorelib2.reflection.Packets;
import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;
import lombok.NonNull;

public class ChatComponent {
	
	private static Constructor<?> packetConstructor;
	private static Class<?> serializerClass;
	private static Method serializerMethod;
	
	static {
		try {
			packetConstructor = ReflectionUtils.getNMSClass("PacketPlayOutChat").getConstructor(ReflectionUtils.getNMSClass("IChatBaseComponent"));
			serializerClass = ReflectionUtils.getInnerNMSClass("IChatBaseComponent", "ChatSerializer");
			serializerMethod = ReflectionUtils.getMethod(serializerClass, "a", JsonElement.class);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
			System.err.println("Perhaps you forgot to shade CS-CoreLib's \"reflection\" package?");
			e.printStackTrace();
		}
	}
	
	private final JsonObject json;
	
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
	}
	
	public void append(@NonNull ChatComponent component) {
		json.add("extra", component.asJson());
	}
	
	public JsonObject asJson() {
		return json;
	}

	private Object getPacket() {
		try {
			return packetConstructor.newInstance(serializerMethod.invoke(serializerClass, json));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void send(Player... players) {
		if (players.length == 0) return;
		
		Object packet = getPacket();
		if (packet == null) return;
		
		for (Player p : players) {
			try {
				Packets.send(p, packet);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

}
