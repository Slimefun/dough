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
 * This class is immutable.
 * The {@link ItemStack} instance which this class holds on to is never mutated.
 *
 * @see #create()
 * @see #applyTo(ItemStack)
 */
@ParametersAreNonnullByDefault
public class ItemStackEditor {

    private final ItemStack itemStack;
    private final Consumer<ItemMeta> metaTransform;
    private final Consumer<ItemStack> stackTransform;

    private ItemStackEditor(ItemStack itemStack,
                            @Nullable Consumer<ItemMeta> metaTransform,
                            @Nullable Consumer<ItemStack> stackTransform) {
        this.itemStack = itemStack;
        this.metaTransform = metaTransform;
        this.stackTransform = stackTransform;
    }

    public ItemStackEditor(ItemStack item) {
        this(item.clone(), null, null);
    }

    public ItemStackEditor(Material type) {
        this(new ItemStack(type));
    }

    public ItemStackEditor addFlags(ItemFlag... flags) {
        return andMetaConsumer(ItemStackUtil.appendItemFlags(flags));
    }

    public ItemStackEditor setCustomModel(int data) {
        return andMetaConsumer(ItemStackUtil.editCustomModelData(data));
    }

    public ItemStackEditor setCustomModel(@Nullable Integer data) {
        return andMetaConsumer(ItemStackUtil.editCustomModelData(data));
    }

    public ItemStackEditor setAmount(int amount) {
        return andStackConsumer(stack -> stack.setAmount(amount));
    }

    public ItemStackEditor setColor(Color color) {
        return andMetaConsumer(meta -> {
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
        return andMetaConsumer(ItemStackUtil.editLore(list));
    }

    public ItemStackEditor setDisplayName(@Nullable String name) {
        return andMetaConsumer(ItemStackUtil.editDisplayName(name));
    }

    public ItemStackEditor andMetaConsumer(Consumer<ItemMeta> consumer) {
        if (this.metaTransform == null) {
            return withMetaConsumer(consumer);
        }
        return withMetaConsumer(this.metaTransform.andThen(consumer));
    }

    public ItemStackEditor withMetaConsumer(@Nullable Consumer<ItemMeta> consumer) {
        return new ItemStackEditor(this.itemStack, consumer, this.stackTransform);
    }

    public ItemStackEditor withStackConsumer(@Nullable Consumer<ItemStack> consumer) {
        return new ItemStackEditor(this.itemStack, this.metaTransform, consumer);
    }

    public ItemStackEditor andStackConsumer(Consumer<ItemStack> consumer) {
        if (this.stackTransform == null) {
            return withStackConsumer(consumer);
        }
        return withStackConsumer(this.stackTransform.andThen(consumer));
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
