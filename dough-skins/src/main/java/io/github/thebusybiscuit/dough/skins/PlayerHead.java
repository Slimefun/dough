package io.github.thebusybiscuit.dough.skins;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;

import io.github.thebusybiscuit.dough.common.DoughLogger;
import io.github.thebusybiscuit.dough.reflection.ReflectionUtils;

public final class PlayerHead {

    private static Constructor<?> newPosition;

    private static Method handle;
    private static Method getTileEntity;
    private static Method setGameProfile;

    static {
        try {
            handle = ReflectionUtils.getOBCClass("CraftWorld").getMethod("getHandle");

            if (ReflectionUtils.getMajorVersion() >= 17) {
                setGameProfile = ReflectionUtils.getNetMinecraftClass("world.level.block.entity.TileEntitySkull").getMethod("setGameProfile", GameProfile.class);

                Class<?> blockPosition = ReflectionUtils.getNetMinecraftClass("core.BlockPosition");
                newPosition = ReflectionUtils.getConstructor(blockPosition, int.class, int.class, int.class);
                getTileEntity = ReflectionUtils.getNMSClass("level.WorldServer").getMethod("getTileEntity", blockPosition);
            } else {
                setGameProfile = ReflectionUtils.getNMSClass("TileEntitySkull").getMethod("setGameProfile", GameProfile.class);

                Class<?> blockPosition = ReflectionUtils.getNMSClass("BlockPosition");
                newPosition = ReflectionUtils.getConstructor(blockPosition, int.class, int.class, int.class);
                getTileEntity = ReflectionUtils.getNMSClass("WorldServer").getMethod("getTileEntity", blockPosition);
            }
        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            DoughLogger logger = new DoughLogger("skins");
            logger.log(Level.SEVERE, "Failed to detect skull nbt methods", e);
        }
    }

    private PlayerHead() {}

    /**
     * This Method will simply return the Head of the specified Player
     * 
     * @param player
     *            The Owner of your Head
     * 
     * @return A new Head Item for the specified Player
     */
    public static @Nonnull ItemStack getItemStack(@Nonnull OfflinePlayer player) {
        Validate.notNull(player, "The player can not be null!");

        return getItemStack(meta -> meta.setOwningPlayer(player));
    }

    /**
     * This Method will simply return the Head of the specified Player
     * 
     * @param skin
     *            The skin of the head you want.
     * 
     * @return A new Head Item for the specified Player
     */
    public static @Nonnull ItemStack getItemStack(@Nonnull PlayerSkin skin) {
        Validate.notNull(skin, "The skin can not be null!");

        return getItemStack(meta -> {
            try {
                skin.getProfile().apply(meta);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    private static @Nonnull ItemStack getItemStack(@Nonnull Consumer<SkullMeta> consumer) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        consumer.accept(meta);
        item.setItemMeta(meta);
        return item;
    }

    @ParametersAreNonnullByDefault
    public static void setSkin(Block block, PlayerSkin skin, boolean sendBlockUpdate) {
        Material material = block.getType();

        if (material != Material.PLAYER_HEAD && material != Material.PLAYER_WALL_HEAD) {
            throw new IllegalArgumentException("Cannot update a head texture. Expected a Player Head, received: " + material);
        }

        try {
            GameProfile profile = skin.getProfile();
            Object world = handle.invoke(block.getWorld());

            Object position = newPosition.newInstance(block.getX(), block.getY(), block.getZ());
            Object tileEntity = getTileEntity.invoke(world, position);

            if (tileEntity != null) {
                setGameProfile.invoke(tileEntity, profile);

                if (sendBlockUpdate) {
                    block.getState().update(true, false);
                }
            }

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
