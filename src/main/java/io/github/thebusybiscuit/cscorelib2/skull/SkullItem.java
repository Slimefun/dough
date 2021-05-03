package io.github.thebusybiscuit.cscorelib2.skull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import lombok.Cleanup;
import lombok.NonNull;

public final class SkullItem {

    private SkullItem() {}

    /**
     * This Method will simply return the Head of the specified Player
     * 
     * @param player
     *            The Owner of your Head
     * 
     * @return A new Head Item for the specified Player
     */
    @Nonnull
    public static ItemStack fromPlayer(@NonNull OfflinePlayer player) {
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
     * @param uuid
     *            The UUID for the fake Profile that is created
     * @param texture
     *            The Base64 representation of your Texture
     * 
     * @return A new Player Head with the Texture you specified
     */
    @Nonnull
    public static ItemStack fromBase64(@NonNull UUID uuid, @NonNull String texture) {
        try {
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);

            SkullMeta meta = (SkullMeta) item.getItemMeta();
            new FakeGameProfile(uuid, texture).inject(meta);

            item.setItemMeta(meta);
            return item;
        } catch (Exception x) {
            x.printStackTrace();
            return new ItemStack(Material.PLAYER_HEAD);
        }
    }

    /**
     * This will call {@link SkullItem#fromBase64(UUID, String)} and use an instance of {@link UUID}
     * that was generated from the provided texture using {@link String#getBytes()}
     * 
     * @param texture
     *            The Base64 representation of your Texture
     * 
     * @return A new Player Head with the Texture you specified
     */
    @Nonnull
    public static ItemStack fromBase64(@NonNull String texture) {
        return fromBase64(UUID.nameUUIDFromBytes(texture.getBytes(StandardCharsets.UTF_8)), texture);
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
     * @param uuid
     *            The UUID for the fake Profile that is created
     * @param texture
     *            The texture for your Player
     * 
     * @return A new Player Head with the Texture you specified
     */
    @Nonnull
    public static ItemStack fromHash(@NonNull UUID uuid, @NonNull String texture) {
        return fromURL(uuid, "http://textures.minecraft.net/texture/" + texture);
    }

    /**
     * This will call {@link SkullItem#fromHash(UUID, String)} and use an instance of {@link UUID}
     * that was generated from the provided texture using {@link String#getBytes()}
     * 
     * @param texture
     *            The texture for your Player
     * 
     * @return A new Player Head with the Texture you specified
     */
    @Nonnull
    public static ItemStack fromHash(@NonNull String texture) {
        return fromHash(UUID.nameUUIDFromBytes(texture.getBytes(StandardCharsets.UTF_8)), texture);
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
     * @param uuid
     *            The UUID for the fake Profile that is created
     * @param url
     *            The URL to your Texture
     * 
     * @return A new Player Head with the Texture you specified
     */
    @Nonnull
    public static ItemStack fromURL(@NonNull UUID uuid, @NonNull String url) {
        return fromBase64(uuid, Base64.getEncoder().encodeToString(("{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}").getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * This will call {@link SkullItem#fromURL(UUID, String)} and use an instance of {@link UUID}
     * that was generated from the provided texture using {@link String#getBytes()}
     * 
     * @param url
     *            The URL to your Texture
     * 
     * @return A new Player Head with the Texture you specified
     */
    @Nonnull
    public static ItemStack fromURL(@NonNull String url) {
        return fromURL(UUID.nameUUIDFromBytes(url.getBytes(StandardCharsets.UTF_8)), url);
    }

    /**
     * This Method returns the Player Head for a specific Player Name.
     * IMPORTANT: This Method will make a Web Request to Mojang to look up the Player Texture.
     * You should definitely perform this action asynchronously.
     * 
     * @param name
     *            The Name of the Profile to look up
     * @return A new Player Head with the Texture of that Player
     * @throws IOException
     *             This method makes a Web Request, if that fails, it will result in an {@link IOException}
     */
    @Nonnull
    public static ItemStack fromName(@NonNull String name) throws IOException {
        @Cleanup
        InputStreamReader profileReader = null;

        @Cleanup
        InputStreamReader sessionReader = null;

        URL profile = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
        profileReader = new InputStreamReader(profile.openStream(), StandardCharsets.UTF_8);
        String uuid = new JsonParser().parse(profileReader).getAsJsonObject().get("id").getAsString();

        URL session = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
        sessionReader = new InputStreamReader(session.openStream(), StandardCharsets.UTF_8);
        JsonArray properties = new JsonParser().parse(sessionReader).getAsJsonObject().get("properties").getAsJsonArray();

        for (JsonElement el : properties) {
            if (el.isJsonObject() && el.getAsJsonObject().get("name").getAsString().equals(FakeGameProfile.PROPERTY_KEY)) {
                return fromBase64(UUID.fromString(uuid), el.getAsJsonObject().get("value").getAsString());
            }
        }

        return new ItemStack(Material.PLAYER_HEAD);
    }

}
