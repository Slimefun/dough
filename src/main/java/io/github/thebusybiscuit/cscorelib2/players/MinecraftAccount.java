package io.github.thebusybiscuit.cscorelib2.players;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownServiceException;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.AllArgsConstructor;
import lombok.NonNull;

public final class MinecraftAccount {

    private static final Pattern UUID_PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");
    private static final String ERROR_TOKEN = "error";
    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final Pattern NAME_PATTERN = Pattern.compile("[\\w_]+");

    private MinecraftAccount() {}

    /**
     * This method returns an Optional for a Player's UUID.
     * The Player has to have a Minecraft account but doesn't have to have ever
     * played on the current server.
     * This will perform a blocking web request, so it should never
     * be run on the main thread.
     * If there is no Player with the given name, the Optional will be empty.
     * 
     * @param name
     *            The Name of the Player
     * @return An Optional describing the UUID of the Player
     * @throws IOException thrown if the stream cannot be opened.
     * @throws TooManyRequestsException
     *             If too many requests were sent to the Server
     */
    public static Optional<UUID> getUUID(@NonNull String name) throws IOException, TooManyRequestsException {
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("\"" + name + "\" is not a valid Minecraft Username!");
        }

        Optional<URL> url = getURL("https://api.mojang.com/users/profiles/minecraft/" + name);

        if (url.isPresent()) {
            try (InputStreamReader reader = new InputStreamReader(url.get().openStream())) {
                JsonElement element = JSON_PARSER.parse(reader);

                if (!(element instanceof JsonNull)) {
                    JsonObject obj = element.getAsJsonObject();

                    if (obj.has(ERROR_TOKEN)) {
                        String error = obj.get(ERROR_TOKEN).getAsString();

                        if (error.equals("TooManyRequestsException")) {
                            throw new TooManyRequestsException(url.orElse(null));
                        }
                        else {
                            throw new UnknownServiceException(error);
                        }
                    }

                    String id = obj.get("id").getAsString();
                    return Optional.ofNullable(UUID.fromString(UUID_PATTERN.matcher(id).replaceAll("$1-$2-$3-$4-$5")));
                }
            }
        }

        return Optional.empty();
    }

    /**
     * This method returns an Optional for a Player's Skin.
     * The Player has to have a Minecraft account but doesn't have to have ever
     * played on the current server.
     * This will perform a blocking web request, so it should never
     * be run on the main thread.
     * If there is no Player with the given UUID, the Optional will be empty.
     * 
     * The Skin Texture will be a Base64-Representation of the Skin's URL.
     * 
     * @param uuid
     *            The UUID of the Player
     * @return An Optional describing the Skin Texture of the Player
     * @throws TooManyRequestsException
     *             If too many requests were sent to the Server
     */
    public static Optional<String> getSkin(@NonNull UUID uuid) throws TooManyRequestsException {
        Optional<URL> url = getURL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", "") + "?unsigned=false");

        if (url.isPresent()) {
            try (InputStreamReader reader = new InputStreamReader(url.get().openStream())) {
                JsonElement element = JSON_PARSER.parse(reader);

                if (!(element instanceof JsonNull)) {
                    JsonObject obj = element.getAsJsonObject();

                    if (obj.has(ERROR_TOKEN)) {
                        String error = obj.get(ERROR_TOKEN).getAsString();

                        if (error.equals("TooManyRequestsException")) {
                            throw new TooManyRequestsException(url.orElse(null));
                        }
                        else {
                            throw new UnknownServiceException(error);
                        }
                    }

                    JsonArray properties = obj.get("properties").getAsJsonArray();

                    for (JsonElement el : properties) {
                        if (el.isJsonObject() && el.getAsJsonObject().get("name").getAsString().equals("textures")) {
                            return Optional.ofNullable(el.getAsJsonObject().get("value").getAsString());
                        }
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Optional.empty();
    }

    private static Optional<URL> getURL(String url) {
        try {
            return Optional.ofNullable(new URL(url));
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @AllArgsConstructor
    public static class TooManyRequestsException extends Exception {

        private static final long serialVersionUID = -7137562700404366948L;
        private final URL url;

        @Override
        public String getMessage() {
            return "Sent too many Requests to the Server! URL: " + url;
        }
    }

}
