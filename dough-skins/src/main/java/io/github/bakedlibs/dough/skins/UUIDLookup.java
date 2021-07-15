package io.github.bakedlibs.dough.skins;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.bakedlibs.dough.common.DoughLogger;

public class UUIDLookup {

    private static final Pattern UUID_PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");
    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final String ERROR_TOKEN = "error";
    private static final Pattern NAME_PATTERN = Pattern.compile("[\\w]+");

    private UUIDLookup() {}

    @ParametersAreNonnullByDefault
    public static @Nonnull CompletableFuture<UUID> forUsername(Plugin plugin, String name) {
        Validate.notNull(plugin, "The plugin instance must not be null!");
        Validate.notNull(name, "The username cannot be null!");

        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("\"" + name + "\" is not a valid Minecraft Username!");
        }

        CompletableFuture<UUID> future = new CompletableFuture<>();
        DoughLogger logger = new DoughLogger(plugin.getServer(), "skins");

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            String targetUrl = "https://api.mojang.com/users/profiles/minecraft/" + name;

            try (InputStreamReader reader = new InputStreamReader(new URL(targetUrl).openStream(), StandardCharsets.UTF_8)) {
                JsonElement element = JSON_PARSER.parse(reader);

                if (!(element instanceof JsonNull)) {
                    JsonObject obj = element.getAsJsonObject();

                    if (obj.has(ERROR_TOKEN)) {
                        String error = obj.get(ERROR_TOKEN).getAsString();
                        future.completeExceptionally(new UnsupportedOperationException(error));
                    }

                    String id = obj.get("id").getAsString();
                    future.complete(UUID.fromString(UUID_PATTERN.matcher(id).replaceAll("$1-$2-$3-$4-$5")));
                }
            } catch (MalformedURLException e) {
                logger.log(Level.SEVERE, "Malformed sessions url: {0}", targetUrl);
                future.completeExceptionally(e);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Exception while requesting skin: {0}", targetUrl);
                future.completeExceptionally(e);
            }
        });

        return future;
    }

}
