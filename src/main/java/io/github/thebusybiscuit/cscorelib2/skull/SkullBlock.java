package io.github.thebusybiscuit.cscorelib2.skull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.mojang.authlib.GameProfile;

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

            setGameProfile = ReflectionUtils.getNMSClass("TileEntitySkull").getMethod("setGameProfile", GameProfile.class);

            Class<?> blockPosition = ReflectionUtils.getNMSClass("BlockPosition");
            newPosition = ReflectionUtils.getConstructor(blockPosition, int.class, int.class, int.class);
            getTileEntity = ReflectionUtils.getNMSClass("WorldServer").getMethod("getTileEntity", blockPosition);
        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            System.err.println("Perhaps you forgot to shade CS-CoreLib's \"reflection\" package?");
            e.printStackTrace();
        }
    }

    private SkullBlock() {}

    public static void setFromHash(@NonNull Block block, @NonNull String hash) {
        setFromHash(block, hash, true);
    }

    public static void setFromHash(@NonNull Block block, @NonNull String hash, boolean causeBlockUpdate) {
        setFromHash(block, UUID.nameUUIDFromBytes(hash.getBytes(StandardCharsets.UTF_8)), hash, causeBlockUpdate);
    }

    public static void setFromHash(@NonNull Block block, @NonNull UUID uuid, @NonNull String hash) {
        setFromHash(block, uuid, hash, true);
    }

    public static void setFromHash(@NonNull Block block, @NonNull UUID uuid, @NonNull String hash, boolean causeBlockUpdate) {
        setFromURL(block, uuid, "http://textures.minecraft.net/texture/" + hash, causeBlockUpdate);
    }

    public static void setFromURL(@NonNull Block block, @NonNull String url) {
        setFromURL(block, url, true);
    }

    public static void setFromURL(@NonNull Block block, @NonNull String url, boolean causeBlockUpdate) {
        setFromURL(block, UUID.nameUUIDFromBytes(url.getBytes(StandardCharsets.UTF_8)), url, causeBlockUpdate);
    }

    public static void setFromURL(@NonNull Block block, @NonNull UUID uuid, @NonNull String url) {
        setFromURL(block, uuid, url, true);
    }

    public static void setFromURL(@NonNull Block block, @NonNull UUID uuid, @NonNull String url, boolean causeBlockUpdate) {
        setFromBase64(block, uuid, Base64.getEncoder().encodeToString(("{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}").getBytes(StandardCharsets.UTF_8)), causeBlockUpdate);
    }

    public static void setFromBase64(@NonNull Block block, @NonNull String texture) {
        setFromBase64(block, UUID.nameUUIDFromBytes(texture.getBytes(StandardCharsets.UTF_8)), texture);
    }

    public static void setFromBase64(@NonNull Block block, @NonNull UUID uuid, @NonNull String texture) {
        setFromBase64(block, uuid, texture, true);
    }

    public static void setFromBase64(@NonNull Block block, @NonNull UUID uuid, @NonNull String texture, boolean causeBlockUpdate) {
        Material material = block.getType();

        if (material != Material.PLAYER_HEAD && material != Material.PLAYER_WALL_HEAD) {
            throw new IllegalArgumentException("Cannot update a head texture. Expected a Player Head, received: " + material);
        }

        try {
            GameProfile profile = new FakeProfile(uuid, texture);
            Object world = handle.invoke(block.getWorld());

            Object position = newPosition.newInstance(block.getX(), block.getY(), block.getZ());
            Object tileEntity = getTileEntity.invoke(world, position);

            if (tileEntity != null) {
                setGameProfile.invoke(tileEntity, profile);

                if (causeBlockUpdate) {
                    block.getState().update(true, false);
                }
            }

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

}
