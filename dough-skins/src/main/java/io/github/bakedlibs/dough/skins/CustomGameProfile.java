package io.github.bakedlibs.dough.skins;

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

    void apply(@Nonnull SkullMeta meta) throws NoSuchFieldException, IllegalAccessException, UnknownServerVersionException {
        ReflectionUtils.setFieldValue(meta, "profile", this);

        // Forces SkullMeta to properly deserialize and serialize the profile
        // setOwnerProfile was added in 1.18, but setOwningPlayer throws a NullPointerException since 1.20.2
        if (MinecraftVersion.get().isAtLeast(MinecraftVersion.parse("1.20"))) {
            meta.setOwnerProfile(Bukkit.createPlayerProfile(meta.getOwningPlayer().getUniqueId(), PLAYER_NAME));
        } else {
            meta.setOwningPlayer(meta.getOwningPlayer());
        }

        // Now override the texture again
        ReflectionUtils.setFieldValue(meta, "profile", this);
    }

}
