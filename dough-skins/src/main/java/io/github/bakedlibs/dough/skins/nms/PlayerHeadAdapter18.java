package io.github.bakedlibs.dough.skins.nms;

import com.mojang.authlib.GameProfile;
import io.github.bakedlibs.dough.reflection.ReflectionUtils;
import io.github.bakedlibs.dough.versions.UnknownServerVersionException;
import org.bukkit.block.Block;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class PlayerHeadAdapter18 implements PlayerHeadAdapter {

    private final Constructor<?> newPosition;

    private final Method getHandle;
    private final Method getTileEntity;
    private final Method setGameProfile;

    PlayerHeadAdapter18() throws NoSuchMethodException, SecurityException, ClassNotFoundException, UnknownServerVersionException {
        setGameProfile = ReflectionUtils.getNetMinecraftClass("world.level.block.entity.TileEntitySkull").getMethod("a", GameProfile.class);
        getHandle = ReflectionUtils.getOBCClass("CraftWorld").getMethod("getHandle");
        Class<?> blockPosition = ReflectionUtils.getNetMinecraftClass("core.BlockPosition");
        newPosition = ReflectionUtils.getConstructor(blockPosition, int.class, int.class, int.class);
        getTileEntity = ReflectionUtils.getNMSClass("level.WorldServer").getMethod("getBlockEntity", blockPosition, boolean.class);
    }

    @Override
    @ParametersAreNonnullByDefault
    public @Nullable Object getTileEntity(Block block) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object world = getHandle.invoke(block.getWorld());

        Object position = newPosition.newInstance(block.getX(), block.getY(), block.getZ());
        return getTileEntity.invoke(world, position, true);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setGameProfile(Object tileEntity, GameProfile profile) throws IllegalAccessException, InvocationTargetException {
        setGameProfile.invoke(tileEntity, profile);
    }

}
