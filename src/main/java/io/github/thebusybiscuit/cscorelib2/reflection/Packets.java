package io.github.thebusybiscuit.cscorelib2.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.entity.Player;

public final class Packets {
	
	private Packets() {}
	
	private static Method playerHandle;
	private static Method sendPacket;
	private static Field playerConnection;
	
	static {
		try {
			playerHandle = ReflectionUtils.getOBCClass("entity.CraftPlayer").getMethod("getHandle");
			playerConnection = ReflectionUtils.getNMSClass("EntityPlayer").getField("playerConnection");
			sendPacket = ReflectionUtils.getMethod(ReflectionUtils.getNMSClass("PlayerConnection"), "sendPacket");
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
	
	public static void send(Player p, Object packet) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		sendPacket.invoke(playerConnection.get(playerHandle.invoke(p)), packet);
	}

}
