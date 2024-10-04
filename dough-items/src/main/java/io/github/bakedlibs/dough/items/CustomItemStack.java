package io.github.bakedlibs.dough.items;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Fluent class to apply edits/transformations to an {@link ItemStack} and it's {@link ItemMeta} instance
 * <p>
 * All methods in this class which are not getters mutate this instance.<br>
 * The {@link ItemStack} instance which this class holds on to is never mutated.
 *
 * @see #create()
 * @see #applyTo(ItemStack)
 */
@ParametersAreNonnullByDefault
public class CustomItemStack {

    private final ItemStack itemStack;
    private Consumer<ItemMeta> metaTransform = meta -> {
    };
    private Consumer<ItemStack> stackTransform = stack -> {
    };

    public CustomItemStack(ItemStack item) {
        this.itemStack = item.clone();
    }

    public CustomItemStack(Material type) {
        this.itemStack = new ItemStack(type);
    }

    public CustomItemStack(ItemStack item, Consumer<ItemMeta> meta) {
        this(item);
        this.metaTransform = meta;
    }

    public CustomItemStack(Material type, Consumer<ItemMeta> meta) {
        this(new ItemStack(type), meta);
    }

    public CustomItemStack(ItemStack item, @Nullable String name, String... lore) {
        this(item);
        setDisplayName(name).setLore(lore);
    }

    public CustomItemStack(ItemStack item, Color color, @Nullable String name, String... lore) {
        this(item, name, lore);
        setColor(color);
    }

    public CustomItemStack(Material type, @Nullable String name, String... lore) {
        this(new ItemStack(type), name, lore);
    }

    public CustomItemStack(Material type, @Nullable String name, List<String> lore) {
        this(new ItemStack(type), name, lore.toArray(new String[lore.size()]));
    }

    public CustomItemStack(ItemStack item, List<String> list) {
        this(item, list.get(0), list.subList(1, list.size()).toArray(new String[0]));
    }

    public CustomItemStack(Material type, List<String> list) {
        this(new ItemStack(type), list);
    }

    public CustomItemStack(ItemStack item, int amount) {
        this(item);
        setAmount(amount);
    }

    public CustomItemStack(ItemStack item, Material type) {
        this(item);
        appendStackConsumer(stack -> stack.setType(type));
    }

    public CustomItemStack addFlags(ItemFlag... flags) {
        return appendMetaConsumer(ItemStackUtil.appendItemFlags(flags));
    }

    public CustomItemStack setCustomModel(int data) {
        return appendMetaConsumer(ItemStackUtil.editCustomModelData(data));
    }

    public CustomItemStack setCustomModel(@Nullable Integer data) {
        return appendMetaConsumer(ItemStackUtil.editCustomModelData(data));
    }

    public CustomItemStack setAmount(int amount) {
        return appendStackConsumer(stack -> stack.setAmount(amount));
    }

    public CustomItemStack setColor(Color color) {
        return appendMetaConsumer(meta -> {
            if (meta instanceof LeatherArmorMeta) {
                ((LeatherArmorMeta) meta).setColor(color);
            }
            if (meta instanceof PotionMeta) {
                ((PotionMeta) meta).setColor(color);
            }
        });
    }

    public CustomItemStack setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    public CustomItemStack setLore(List<String> list) {
        return appendMetaConsumer(ItemStackUtil.editLore(list));
    }

    public CustomItemStack setDisplayName(@Nullable String name) {
        return appendMetaConsumer(ItemStackUtil.editDisplayName(name));
    }

    public CustomItemStack appendMetaConsumer(Consumer<ItemMeta> consumer) {
        return setMetaConsumer(this.metaTransform.andThen(consumer));
    }

    public CustomItemStack setMetaConsumer(Consumer<ItemMeta> consumer) {
        this.metaTransform = consumer;
        return this;
    }

    public CustomItemStack setStackConsumer(Consumer<ItemStack> consumer) {
        this.stackTransform = consumer;
        return this;
    }

    public CustomItemStack appendStackConsumer(Consumer<ItemStack> consumer) {
        return setStackConsumer(this.stackTransform.andThen(consumer));
    }

    public Consumer<ItemMeta> getMetaTransform() {
        return this.metaTransform;
    }

    public Consumer<ItemStack> getStackTransform() {
        return this.stackTransform;
    }

    public ItemStack create() {
        ItemStack cloned = this.itemStack.clone();
        applyTo(cloned);
        return cloned;
    }

    public void applyTo(ItemStack itemStack) {
        this.stackTransform.accept(itemStack);
        ItemStackUtil.editMeta(itemStack, this.metaTransform);
    }

}
