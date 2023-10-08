package io.github.bakedlibs.dough.skins;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import io.github.bakedlibs.dough.reflection.ReflectionUtils;

final class CustomGameProfile extends GameProfile {

    /**
     * The player name for this profile.
     * "CS-CoreLib" for historical reasons and backwards compatibility.
     */
    static final String PLAYER_NAME = "CS-CoreLib";

    /**
     * The skin's property key.
     */
    static final String PROPERTY_KEY = "textures";

    CustomGameProfile(@Nonnull UUID uuid, @Nullable String texture) {
        super(uuid, PLAYER_NAME);

        if (texture != null) {
            getProperties().put(PROPERTY_KEY, new Property(PROPERTY_KEY, texture));
        }
    }

    void apply(@Nonnull SkullMeta meta) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtils.setFieldValue(meta, "profile", this);

        // Forces SkullMeta to properly deserialize and serialize the profile
        meta.setOwnerProfile(Bukkit.createPlayerProfile(meta.getOwningPlayer().getUniqueId(), PLAYER_NAME));

        // Now override the texture again
        ReflectionUtils.setFieldValue(meta, "profile", this);
    }

}
