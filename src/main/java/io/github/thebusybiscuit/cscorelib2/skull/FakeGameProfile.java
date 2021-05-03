package io.github.thebusybiscuit.cscorelib2.skull;

import java.util.UUID;

import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;
import lombok.NonNull;

final class FakeGameProfile extends GameProfile {

    public static final String PROPERTY_KEY = "textures";

    FakeGameProfile(@NonNull UUID uuid, @NonNull String texture) {
        super(uuid, "CS-CoreLib");
        getProperties().put(PROPERTY_KEY, new Property(PROPERTY_KEY, texture));
    }

    void inject(@NonNull SkullMeta meta) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtils.setFieldValue(meta, "profile", this);

        // Forces SkullMeta to properly deserialize and serialize the profile
        meta.setOwningPlayer(meta.getOwningPlayer());

        // Now override the texture again
        ReflectionUtils.setFieldValue(meta, "profile", this);
    }

}
