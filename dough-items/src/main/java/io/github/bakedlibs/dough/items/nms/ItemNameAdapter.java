package io.github.bakedlibs.dough.items.nms;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.common.DoughLogger;
import io.github.bakedlibs.dough.versions.MinecraftVersion;

public interface ItemNameAdapter {

    @ParametersAreNonnullByDefault
    @Nonnull
    String getName(ItemStack item) throws IllegalAccessException, InvocationTargetException;

    public static @Nullable ItemNameAdapter get() {
        try {
            MinecraftVersion version = MinecraftVersion.get();

            if (MinecraftVersion.isMocked()) {
                // Special case for MockBukkit
                return new ItemNameAdapterMockBukkit();
            } else if (version.isAtLeast(1, 20)) {
                return new ItemNameAdapter20();
            } else if (version.isAtLeast(1, 19)) {
                return new ItemNameAdapter19();
            } else if (version.isAtLeast(1, 18, 2)) {
                return new ItemNameAdapter18v2();
            } else if (version.isAtLeast(1, 18)) {
                // 1.18+ mappings
                return new ItemNameAdapter18();
            } else if (version.isAtLeast(1, 17)) {
                // 1.17+ mappings
                return new ItemNameAdapter17();
            } else {
                // Old mappings
                return new ItemNameAdapterBefore17();
            }
        } catch (Exception x) {
            DoughLogger logger = new DoughLogger("items");
            logger.log(Level.SEVERE, "Failed to detect items nbt methods", x);
            return null;
        }

    }
}
