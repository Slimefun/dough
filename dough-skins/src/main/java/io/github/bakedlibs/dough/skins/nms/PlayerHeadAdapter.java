package io.github.bakedlibs.dough.skins.nms;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;

import com.mojang.authlib.GameProfile;

import io.github.bakedlibs.dough.common.DoughLogger;
import io.github.bakedlibs.dough.versions.MinecraftVersion;

public interface PlayerHeadAdapter {

    @ParametersAreNonnullByDefault
    @Nullable
    Object getTileEntity(Block block) throws IllegalAccessException, InvocationTargetException, InstantiationException;

    @ParametersAreNonnullByDefault
    void setGameProfile(Object tileEntity, GameProfile profile) throws IllegalAccessException, InvocationTargetException;

    public static @Nullable PlayerHeadAdapter get() {
        try {
            MinecraftVersion version = MinecraftVersion.get();

            if (version.isAtLeast(1, 18)) {
                // 1.18 mappings
                return new PlayerHeadAdapter18();
            } else if (version.isAtLeast(1, 17)) {
                // 1.17 mappings
                return new PlayerHeadAdapter17();
            } else {
                // Old mappings
                return new PlayerHeadAdapterBefore17();
            }
        } catch (Exception x) {
            DoughLogger logger = new DoughLogger("skins");
            logger.log(Level.SEVERE, "Failed to detect skull nbt methods", x);
            return null;
        }

    }
}
