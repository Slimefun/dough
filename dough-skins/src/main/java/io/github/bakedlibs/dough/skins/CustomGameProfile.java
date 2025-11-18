package io.github.bakedlibs.dough.skins;

import java.net.URL;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import io.github.bakedlibs.dough.reflection.ReflectionUtils;
import io.github.bakedlibs.dough.versions.MinecraftVersion;
import io.github.bakedlibs.dough.versions.UnknownServerVersionException;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

public final class CustomGameProfile {

    /**
     * The player name for this profile.
     * "CS-CoreLib" for historical reasons and backwards compatibility.
     */
    private static final String PLAYER_NAME = "CS-CoreLib";

    /**
     * The skin's property key.
     */
    private static final String PROPERTY_KEY = "textures";

    private final URL skinUrl;
    private final String texture;
    private final GameProfile delegate;
    
    public CustomGameProfile(@Nonnull UUID uuid, @Nullable String texture, @Nonnull URL url) {
        this.delegate = new GameProfile(uuid, PLAYER_NAME);
        this.skinUrl = url;
        this.texture = texture;

        if (texture != null) {
            getProperties().put(PROPERTY_KEY, new Property(PROPERTY_KEY, texture));
        }
    }

    public void apply(@Nonnull SkullMeta meta) throws NoSuchFieldException, IllegalAccessException, UnknownServerVersionException {
        if (MinecraftVersion.get().isAtLeast(MinecraftVersion.parse("1.20"))) {
            PlayerProfile playerProfile = Bukkit.createPlayerProfile(this.delegate.getId(), PLAYER_NAME);
            PlayerTextures playerTextures = playerProfile.getTextures();
            playerTextures.setSkin(this.skinUrl);
            playerProfile.setTextures(playerTextures);
            meta.setOwnerProfile(playerProfile);
        } else {
            // Force SkullMeta to use our wrapped GameProfile
            ReflectionUtils.setFieldValue(meta, "profile", this.delegate);

            meta.setOwningPlayer(meta.getOwningPlayer());

            // Override the texture again
            ReflectionUtils.setFieldValue(meta, "profile", this.delegate);
        }
    }

    /**
     * Get the base64 encoded texture from the underline GameProfile.
     *
     * @return the base64 encoded texture.
     */
    @Nullable
    public String getBase64Texture() {
        return this.texture;
    }
    
    public GameProfile getHandle() {
        return this.delegate;
    }
}
