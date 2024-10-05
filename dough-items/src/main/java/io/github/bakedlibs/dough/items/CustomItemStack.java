package io.github.bakedlibs.dough.items;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public final class CustomItemStack {

    private CustomItemStack() {
        throw new IllegalStateException("Cannot instantiate CustomItemStack");
    }

    public static ItemStack create(ItemStack itemStack, Consumer<ItemMeta> metaConsumer) {
        return new ItemStackEditor(itemStack).appendMetaConsumer(metaConsumer).create();
    }

    public static ItemStack create(Material material, Consumer<ItemMeta> metaConsumer) {
        return new ItemStackEditor(material).appendMetaConsumer(metaConsumer).create();
    }

    public static ItemStack create(ItemStack item, @Nullable String name, String... lore) {
        return new ItemStackEditor(item)
                .setDisplayName(name)
                .setLore(lore)
                .create();
    }

    public static ItemStack create(ItemStack item, Color color, @Nullable String name, String... lore) {
        return new ItemStackEditor(item)
                .setColor(color)
                .setDisplayName(name)
                .setLore(lore)
                .create();
    }

    public static ItemStack create(Material material, @Nullable String name, String... lore) {
        return create(new ItemStack(material), name, lore);
    }

    public static ItemStack create(Material type, @Nullable String name, List<String> lore) {
        return create(new ItemStack(type), name, lore.toArray(String[]::new));
    }


    public static ItemStack create(ItemStack item, List<String> list) {
        return create(new ItemStack(item), list.get(0), list.subList(1, list.size()).toArray(String[]::new));
    }

    public static ItemStack create(Material type, List<String> list) {
        return create(new ItemStack(type), list);
    }

    public static ItemStack create(ItemStack item, int amount) {
        return new ItemStackEditor(item).setAmount(amount).create();
    }

    /**
     * Clones the item stack and sets its type
     *
     * @param itemStack The item
     * @param type      The new type
     * @return Returns the item with a new type
     * @deprecated Setting the type via {@link ItemStack#setType(Material)} will not be supported soon.
     */
    @Deprecated(forRemoval = true)
    public static ItemStack create(ItemStack itemStack, Material type) {
        return new ItemStackEditor(itemStack).appendStackConsumer(item -> item.setType(type)).create();
    }

}
