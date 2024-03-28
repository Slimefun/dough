package io.github.bakedlibs.dough.skins.nms;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.github.bakedlibs.dough.reflection.ReflectionUtils;
import io.github.bakedlibs.dough.versions.UnknownServerVersionException;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

public class PlayerHeadAdapterPaper implements PlayerHeadAdapter {

    private static final String PROPERTY_KEY = "textures";
    private final Method createPaperGameProfile;
    private final Method paperGameProfileSetProperty;
    private final Constructor<?> createProperty;
    private final Method setGameProfile;


    public PlayerHeadAdapterPaper() throws NoSuchMethodException, SecurityException, ClassNotFoundException, UnknownServerVersionException {
        Class<?> paperGameProfile = Class.forName("com.destroystokyo.paper.profile.PlayerProfile");
        Class<?> paperProperty = Class.forName("com.destroystokyo.paper.profile.ProfileProperty");
        createPaperGameProfile = ReflectionUtils.getMethod(Bukkit.class, "createProfile");
        paperGameProfileSetProperty = ReflectionUtils.getMethod(paperGameProfile, "setProperty", paperProperty);
        createProperty = ReflectionUtils.getConstructor(paperProperty, String.class, String.class);
        setGameProfile = ReflectionUtils.getMethod(Skull.class, "setPlayerProfile", paperGameProfile);
    }

    public static boolean canApply() {
        try {
            Class.forName("com.destroystokyo.paper.profile.PlayerProfile");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }

    }

    @Nullable
    @Override
    public Object getTileEntity(Block block) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return block.getState();
    }

    @Override
    public void setGameProfile(Object tileEntity, GameProfile profile) throws IllegalAccessException, InvocationTargetException {
        if (!(tileEntity instanceof Skull)) {
            throw new IllegalArgumentException("tileEntity must be BukkitAPI Skull tile entity. Provided " + tileEntity.getClass().getName());
        }
        Skull skull = (Skull) tileEntity;
        Collection<Property> properties = profile.getProperties().get(PROPERTY_KEY);
        if (properties.isEmpty()) {
            return;
        }
        String texture = properties.iterator().next().getValue();

        Object paperGameProfile = createPaperGameProfile.invoke(null, profile.getId());
        Object property;
        try {
            property = createProperty.newInstance(PROPERTY_KEY, texture);
        } catch (InstantiationException e) {
            //shouldnt fail if it got this far
            throw new InvocationTargetException(e);
        }
        paperGameProfileSetProperty.invoke(paperGameProfile, property);
        setGameProfile.invoke(skull, paperGameProfile);
    }
}
