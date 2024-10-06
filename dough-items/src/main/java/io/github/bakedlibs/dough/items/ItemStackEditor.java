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
public class ItemStackEditor {

    private final ItemStack itemStack;
    private Consumer<ItemMeta> metaTransform = null;
    private Consumer<ItemStack> stackTransform = null;

    public ItemStackEditor(ItemStack item) {
        this.itemStack = item.clone();
    }

    public ItemStackEditor(Material type) {
        this.itemStack = new ItemStack(type);
    }

    public ItemStackEditor addFlags(ItemFlag... flags) {
        return appendMetaConsumer(ItemStackUtil.appendItemFlags(flags));
    }

    public ItemStackEditor setCustomModel(int data) {
        return appendMetaConsumer(ItemStackUtil.editCustomModelData(data));
    }

    public ItemStackEditor setCustomModel(@Nullable Integer data) {
        return appendMetaConsumer(ItemStackUtil.editCustomModelData(data));
    }

    public ItemStackEditor setAmount(int amount) {
        return appendStackConsumer(stack -> stack.setAmount(amount));
    }

    public ItemStackEditor setColor(Color color) {
        return appendMetaConsumer(meta -> {
            if (meta instanceof LeatherArmorMeta) {
                ((LeatherArmorMeta) meta).setColor(color);
            }
            if (meta instanceof PotionMeta) {
                ((PotionMeta) meta).setColor(color);
            }
        });
    }

    public ItemStackEditor setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    public ItemStackEditor setLore(List<String> list) {
        return appendMetaConsumer(ItemStackUtil.editLore(list));
    }

    public ItemStackEditor setDisplayName(@Nullable String name) {
        return appendMetaConsumer(ItemStackUtil.editDisplayName(name));
    }

    public ItemStackEditor appendMetaConsumer(Consumer<ItemMeta> consumer) {
        if (this.metaTransform == null) {
            return setMetaConsumer(consumer);
        }
        return setMetaConsumer(this.metaTransform.andThen(consumer));
    }

    public ItemStackEditor setMetaConsumer(@Nullable Consumer<ItemMeta> consumer) {
        this.metaTransform = consumer;
        return this;
    }

    public ItemStackEditor setStackConsumer(@Nullable Consumer<ItemStack> consumer) {
        this.stackTransform = consumer;
        return this;
    }

    public ItemStackEditor appendStackConsumer(Consumer<ItemStack> consumer) {
        if (this.stackTransform == null) {
            return setStackConsumer(consumer);
        }
        return setStackConsumer(this.stackTransform.andThen(consumer));
    }

    public ItemStack create() {
        ItemStack cloned = this.itemStack.clone();
        applyTo(cloned);
        return cloned;
    }

    public void applyTo(ItemStack itemStack) {
        if (this.stackTransform != null) {
            this.stackTransform.accept(itemStack);
        }
        if (this.metaTransform != null) {
            ItemStackUtil.editMeta(itemStack, this.metaTransform);
        }
    }

}
