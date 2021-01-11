package io.github.thebusybiscuit.cscorelib2.skull;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;
import lombok.NonNull;

final class FakeProfile {

    public static final String PROPERTY_KEY = "textures";

    private FakeProfile() {}

    static GameProfile createProfile(@NonNull UUID uuid, @NonNull String texture) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        GameProfile profile = new GameProfile(uuid, "CS-CoreLib");
        PropertyMap properties = profile.getProperties();
        properties.put(PROPERTY_KEY, new Property(PROPERTY_KEY, texture));
        return profile;
    }

    static void inject(@NonNull SkullMeta meta, @NonNull UUID uuid, @NonNull String texture) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        ReflectionUtils.setFieldValue(meta, "profile", createProfile(uuid, texture));

        // Forces SkullMeta to properly deserialize and serialize the profile
        meta.setOwningPlayer(meta.getOwningPlayer());

        // Now override the texture again
        ReflectionUtils.setFieldValue(meta, "profile", createProfile(uuid, texture));
    }

}
