package io.github.bakedlibs.dough.skins.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;

import com.mojang.authlib.GameProfile;

import io.github.bakedlibs.dough.reflection.ReflectionUtils;
import io.github.bakedlibs.dough.versions.UnknownServerVersionException;

class PlayerHeadAdapter17 implements PlayerHeadAdapter {

    private final Constructor<?> newPosition;

    private final Method getHandle;
    private final Method getTileEntity;
    private final Method setGameProfile;

    PlayerHeadAdapter17() throws NoSuchMethodException, SecurityException, ClassNotFoundException, UnknownServerVersionException {
        setGameProfile = ReflectionUtils.getNetMinecraftClass("world.level.block.entity.TileEntitySkull").getMethod("setGameProfile", GameProfile.class);
        getHandle = ReflectionUtils.getOBCClass("CraftWorld").getMethod("getHandle");

        Class<?> blockPosition = ReflectionUtils.getNetMinecraftClass("core.BlockPosition");
        newPosition = ReflectionUtils.getConstructor(blockPosition, int.class, int.class, int.class);
        getTileEntity = ReflectionUtils.getNMSClass("level.WorldServer").getMethod("getTileEntity", blockPosition);
    }

    @Override
    @ParametersAreNonnullByDefault
    public @Nullable Object getTileEntity(Block block) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object world = getHandle.invoke(block.getWorld());

        Object position = newPosition.newInstance(block.getX(), block.getY(), block.getZ());
        return getTileEntity.invoke(world, position);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setGameProfile(Object tileEntity, GameProfile profile) throws IllegalAccessException, InvocationTargetException {
        setGameProfile.invoke(tileEntity, profile);
    }

}
