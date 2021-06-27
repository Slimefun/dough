package io.github.thebusybiscuit.dough.skins;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.plugin.Plugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.thebusybiscuit.dough.common.DoughLogger;

public final class PlayerSkin {

    private static final String ERROR_TOKEN = "error";

    private final CustomGameProfile profile;

    PlayerSkin(@Nonnull UUID uuid, @Nullable String base64skinTexture) {
        this.profile = new CustomGameProfile(uuid, base64skinTexture);
    }

    final @Nonnull CustomGameProfile getProfile() {
        return profile;
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull PlayerSkin fromBase64(UUID uuid, String base64skinTexture) {
        return new PlayerSkin(uuid, base64skinTexture);
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull PlayerSkin fromBase64(String base64skinTexture) {
        UUID uuid = UUID.nameUUIDFromBytes(base64skinTexture.getBytes(StandardCharsets.UTF_8));
        return fromBase64(uuid, base64skinTexture);
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull PlayerSkin fromURL(UUID uuid, String url) {
        String value = "{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}";
        String base64skinTexture = Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
        return fromBase64(uuid, base64skinTexture);
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull PlayerSkin fromURL(String url) {
        UUID uuid = UUID.nameUUIDFromBytes(url.getBytes(StandardCharsets.UTF_8));
        return fromURL(uuid, url);
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull PlayerSkin fromHashCode(UUID uuid, String hashCode) {
        return fromURL(uuid, "http://textures.minecraft.net/texture/" + hashCode);
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull PlayerSkin fromHashCode(String hashCode) {
        UUID uuid = UUID.nameUUIDFromBytes(hashCode.getBytes(StandardCharsets.UTF_8));
        return fromHashCode(uuid, hashCode);
    }

    @ParametersAreNonnullByDefault
    public static @Nonnull CompletableFuture<PlayerSkin> fromPlayerUUID(Plugin plugin, UUID uuid) {
        CompletableFuture<PlayerSkin> future = new CompletableFuture<>();
        DoughLogger logger = new DoughLogger(plugin.getServer(), "skins");

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            String targetUrl = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", "") + "?unsigned=false";

            try (InputStreamReader reader = new InputStreamReader(new URL(targetUrl).openStream(), StandardCharsets.UTF_8)) {
                JsonElement element = new JsonParser().parse(reader);

                if (!(element instanceof JsonNull)) {
                    JsonObject obj = element.getAsJsonObject();

                    if (obj.has(ERROR_TOKEN)) {
                        String error = obj.get(ERROR_TOKEN).getAsString();
                        future.completeExceptionally(new UnsupportedOperationException(error));
                        return;
                    }

                    JsonArray properties = obj.get("properties").getAsJsonArray();

                    for (JsonElement el : properties) {
                        if (el.isJsonObject() && el.getAsJsonObject().get("name").getAsString().equals("textures")) {
                            String base64Texture = el.getAsJsonObject().get("value").getAsString();
                            PlayerSkin playerSkin = PlayerSkin.fromBase64(uuid, base64Texture);

                            future.complete(playerSkin);
                            return;
                        }
                    }

                }
            } catch (MalformedURLException e) {
                logger.log(Level.SEVERE, "Malformed sessions url: {0}", targetUrl);
                future.completeExceptionally(e);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Exception while requesting skin: {0}", targetUrl);
                future.completeExceptionally(e);
            }
        });

        return future;
    }

}
