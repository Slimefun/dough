package io.github.thebusybiscuit.cscorelib2.skull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;
import lombok.Cleanup;

public final class SkullItem {
	
	private SkullItem() {}
	
	private static Method  property;
	private static Method insertProperty;
	
	private static Constructor<?> profileConstructor;
	private static Constructor<?> propertyConstructor;
	
	private static Class<?> profileClass;
	private static Class<?> propertyClass;
	private static Class<?> mapClass;
	
	static {
		try {
			profileClass = Class.forName("com.mojang.authlib.GameProfile");
			propertyClass = Class.forName("com.mojang.authlib.properties.Property");
			mapClass = Class.forName("com.mojang.authlib.properties.PropertyMap");
			
			profileConstructor = ReflectionUtils.getConstructor(profileClass, UUID.class, String.class);
			property = ReflectionUtils.getMethod(profileClass, "getProperties");
			propertyConstructor = ReflectionUtils.getConstructor(propertyClass, String.class, String.class);
			insertProperty = ReflectionUtils.getMethod(mapClass, "put", String.class, propertyClass);
		}  catch (Exception e) {
			System.err.println("Perhaps you forgot to shade CS-CoreLib's \"reflection\" package?");
			e.printStackTrace();
		}
	}
	
	private static Object createProfile(UUID uuid, String texture) throws Exception {
		Object profile = profileConstructor.newInstance(uuid, "CS-CoreLib");
		Object properties = property.invoke(profile);
		insertProperty.invoke(properties, "textures", propertyConstructor.newInstance("textures", texture));
		return profile;
	}
	
	/**
	 * This Method will simply return the Head of the specified Player
	 * 
	 * @param player	The Owner of your Head
	 * @return			A new Head Item for the specified Player
	 */
	public static ItemStack fromPlayer(OfflinePlayer player) {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwningPlayer(player);
		item.setItemMeta(meta);
		return item;
	}

	/**
	 * This Method will return a Custom Player Head with the Texture specified.
	 * 
	 * The specified UUID should be shared for this specific URL,
	 * if you use {@link UUID#randomUUID()} for this, then your Heads may not be stackable.
	 * You should probably store and re-use these UUIDs somewhere.
	 * 
	 * @param uuid		The UUID for the fake Profile that is created
	 * @param texture	The Base64 representation of your Texture
	 * @return			A new Player Head with the Texture you specified
	 */
	public static ItemStack fromBase64(UUID uuid, String texture) {
		try {
			Object profile = createProfile(uuid, texture);
			ItemStack item = new ItemStack(Material.PLAYER_HEAD);
			
			ItemMeta im = item.getItemMeta();
			ReflectionUtils.setFieldValue(im, "profile", profile);
			item.setItemMeta(im);
			return item;
		} catch(Exception x) {
			x.printStackTrace();
			return new ItemStack(Material.PLAYER_HEAD);
		}
	}

	/**
	 * This Method will return a Custom Player Head with the Texture specified.
	 * This method will simply call {@link SkullItem#fromURL(UUID, String)}
	 * and prepend the default minecraft.net skin url.
	 * 
	 * The specified UUID should be shared for this specific URL,
	 * if you use {@link UUID#randomUUID()} for this, then your Heads may not be stackable.
	 * You should probably store and re-use these UUIDs somewhere.
	 * 
	 * @param uuid		The UUID for the fake Profile that is created
	 * @param texture	The texture for your Player
	 * @return			A new Player Head with the Texture you specified
	 */
	public static ItemStack fromTextureID(UUID uuid, String texture) {
		return fromURL(uuid, "http://textures.minecraft.net/texture/" + texture);
	}
	
	/**
	 * This Method will return a Custom Player Head with the Texture
	 * found in the URL.
	 * Note that it should be a minecraft.net URL, otherwise this may not work.
	 * 
	 * The specified UUID should be shared for this specific URL,
	 * if you use {@link UUID#randomUUID()} for this, then your Heads may not be stackable.
	 * You should probably store and re-use these UUIDs somewhere.
	 * 
	 * @param uuid	The UUID for the fake Profile that is created
	 * @param url	The URL to your Texture
	 * @return		A new Player Head with the Texture you specified
	 */
	public static ItemStack fromURL(UUID uuid, String url) {
		return fromBase64(uuid, Base64.getEncoder().encodeToString(("{textures:{SKIN:{url:\"" + url + "\"}}}").getBytes()));
	}
	
	/**
	 * This Method returns the Player Head for a specific Player Name.
	 * IMPORTANT: This Method will make a Web Request to Mojang to look up the Player Texture.
	 * You should definitely perform this action asynchronously.
	 * 
	 * @param name			The Name of the Profile to look up
	 * @return				A new Player Head with the Texture of that Player
	 * @throws IOException	This method makes a Web Request, if that fails, it will result in an {@link IOException}
	 */
	public static ItemStack fromName(String name) throws IOException {
		@Cleanup
		InputStreamReader profile_reader = null, session_reader = null;
		
		URL profile = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
		profile_reader = new InputStreamReader(profile.openStream());
		String uuid = new JsonParser().parse(profile_reader).getAsJsonObject().get("id").getAsString();
		
		URL session = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
        session_reader = new InputStreamReader(session.openStream());
        JsonArray properties = new JsonParser().parse(session_reader).getAsJsonObject().get("properties").getAsJsonArray();
        
        for (JsonElement el: properties) {
        	if (el.isJsonObject() && el.getAsJsonObject().get("name").getAsString().equals("textures")) {
				return fromBase64(UUID.fromString(uuid), el.getAsJsonObject().get("value").getAsString());
        	}
        }
        
        return null;
	}

}
