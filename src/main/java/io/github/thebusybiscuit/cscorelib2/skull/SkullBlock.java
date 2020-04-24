package io.github.thebusybiscuit.cscorelib2.skull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;

import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;
import lombok.NonNull;

public final class SkullBlock {

    private static Constructor<?> newPosition;

    private static Method handle;
    private static Method getTileEntity;
    private static Method setGameProfile;

    static {
        try {
            handle = ReflectionUtils.getOBCClass("CraftWorld").getMethod("getHandle");

            setGameProfile = ReflectionUtils.getNMSClass("TileEntitySkull").getMethod("setGameProfile", FakeProfile.profileClass);

            Class<?> blockPosition = ReflectionUtils.getNMSClass("BlockPosition");
            newPosition = ReflectionUtils.getConstructor(blockPosition, int.class, int.class, int.class);
            getTileEntity = ReflectionUtils.getNMSClass("WorldServer").getMethod("getTileEntity", blockPosition);
        }
        catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            System.err.println("Perhaps you forgot to shade CS-CoreLib's \"reflection\" package?");
            e.printStackTrace();
        }
    }

    private SkullBlock() {}

    public static void setFromBase64(@NonNull Block block, @NonNull String texture) {
        setFromBase64(block, UUID.nameUUIDFromBytes(texture.getBytes()), texture);
    }

    public static void setFromBase64(@NonNull Block block, @NonNull UUID uuid, @NonNull String texture) {
        if (!block.getType().equals(Material.PLAYER_HEAD) && !block.getType().equals(Material.PLAYER_WALL_HEAD)) {
            throw new IllegalArgumentException("Block is not a Skull");
        }

        try {
            Object profile = FakeProfile.createProfile(uuid, texture);
            Object world = handle.invoke(block.getWorld());

            Object position = newPosition.newInstance(block.getX(), block.getY(), block.getZ());
            Object tileEntity = getTileEntity.invoke(world, position);

            if (tileEntity != null) {
                setGameProfile.invoke(tileEntity, profile);
                block.getState().update(true);
            }

        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public static void setFromURL(@NonNull Block block, @NonNull UUID uuid, @NonNull String url) {
        setFromBase64(block, uuid, Base64.getEncoder().encodeToString(("{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}").getBytes()));
    }

    public static void setFromURL(@NonNull Block block, @NonNull String url) {
        setFromURL(block, UUID.nameUUIDFromBytes(url.getBytes()), url);
    }

    public static void setFromHash(@NonNull Block block, @NonNull UUID uuid, @NonNull String hash) {
        setFromURL(block, uuid, "http://textures.minecraft.net/texture/" + hash);
    }

    public static void setFromHash(@NonNull Block block, @NonNull String hash) {
        setFromHash(block, UUID.nameUUIDFromBytes(hash.getBytes()), hash);
    }

}
