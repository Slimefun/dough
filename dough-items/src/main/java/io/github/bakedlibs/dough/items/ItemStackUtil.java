package io.github.bakedlibs.dough.items;

import io.papermc.lib.PaperLib;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Utility class to edit the {@link ItemMeta} on an {@link ItemStack}
 *
 * @author md5sha256
 */
@ParametersAreNonnullByDefault
public final class ItemStackUtil {

    private ItemStackUtil() {
        throw new IllegalStateException("Static utility class cannot be instantiated");
    }

    /**
     * Curries a {@link Consumer} which sets the display name to the given {@link String}.
     * The string will have its color codes translated.
     *
     * @param name The name to set
     * @return Returns a {@link Consumer}
     */
    public static Consumer<ItemMeta> editDisplayName(@Nullable String name) {
        return (meta) -> {
            if (name != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }
        };
    }

    /**
     * Curries a {@link Consumer} which sets the lore to the given {@link String}s.
     * The strings will have their color codes translated.
     *
     * @param lore The lore to set
     * @return Returns a {@link Consumer}
     */
    public static Consumer<ItemMeta> editLore(@Nonnull String... lore) {
        return editLore(Arrays.asList(lore));
    }


    /**
     * Curries a {@link Consumer} which sets the lore to the given {@link String}s.
     * The strings will have their color codes translated.
     *
     * @param lore The lore to set
     * @return Returns a {@link Consumer}
     */
    public static Consumer<ItemMeta> editLore(@Nonnull List<String> lore) {
        return (meta) -> {
            if (lore.isEmpty()) {
                meta.lore(Collections.emptyList());
                return;
            }
            List<String> newLore = new ArrayList<>(lore);
            newLore.replaceAll(line -> ChatColor.translateAlternateColorCodes('&', line));
            meta.setLore(newLore);
        };
    }

    /**
     * Curries a {@link Consumer} which calls {@link PotionMeta#setColor(Color)} or {@link LeatherArmorMeta#setColor(Color)}
     * if the {@link ItemMeta} is an instance of either.
     *
     * @param color The color to set
     * @return Returns a {@link Consumer}
     */
    public static Consumer<ItemMeta> editColor(@Nonnull Color color) {
        return (meta) -> {
            if (meta instanceof LeatherArmorMeta) {
                ((LeatherArmorMeta) meta).setColor(color);
            }
            if (meta instanceof PotionMeta) {
                ((PotionMeta) meta).setColor(color);
            }
        };
    }

    /**
     * Curries a {@link Consumer} which sets the custom model data to the given integer
     *
     * @param data The custom model data to set
     * @return Returns a {@link Consumer}
     */
    public static Consumer<ItemMeta> editCustomModelData(@Nullable Integer data) {
        return (meta) -> meta.setCustomModelData(data);
    }

    /**
     * Curries a {@link Consumer} which sets the custom model data to the given integer
     *
     * @param data The custom model data to set
     * @return Returns a {@link Consumer}
     */
    public static Consumer<ItemMeta> editCustomModelData(int data) {
        return (meta) -> meta.setCustomModelData(data == 0 ? null : data);
    }

    /**
     * Curries a {@link Consumer} which adds the given {@link ItemFlag}s
     * @param flags The item flags
     * @return Returns a {@link Consumer}
     */
    public static Consumer<ItemMeta> appendItemFlags(ItemFlag... flags) {
        return (meta) -> meta.addItemFlags(flags);
    }

    /**
     * Applies an edit to the {@link ItemMeta} of the given {@link ItemStack}, returning whether
     * the changes were successfully applied
     *
     * @param itemStack The item
     * @param consumer  The edits to apply
     * @return {@link ItemStack#editMeta(Consumer)} if on paper, {@link ItemStack#setItemMeta(ItemMeta)} otherwise
     */
    public static boolean editMeta(ItemStack itemStack, Consumer<ItemMeta> consumer) {
        if (PaperLib.isPaper()) {
            return itemStack.editMeta(consumer);
        } else {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                consumer.accept(itemMeta);
            }
            return itemStack.setItemMeta(itemMeta);
        }
    }
}
