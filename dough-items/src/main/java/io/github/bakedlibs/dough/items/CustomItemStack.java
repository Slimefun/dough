package io.github.bakedlibs.dough.items;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

public class CustomItemStack extends ItemStack {

    public CustomItemStack(ItemStack item) {
        super(item);
    }

    public CustomItemStack(Material type) {
        super(type);
    }

    public CustomItemStack(ItemStack item, Consumer<ItemMeta> meta) {
        super(item);
        ItemMeta im = getItemMeta();
        meta.accept(im);
        setItemMeta(im);
    }

    public CustomItemStack(Material type, Consumer<ItemMeta> meta) {
        this(new ItemStack(type), meta);

    }

    public CustomItemStack(ItemStack item, String name, String... lore) {
        this(item, im -> {
            if (name != null) {
                im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }

            if (lore.length > 0) {
                List<String> lines = new ArrayList<>();

                for (String line : lore) {
                    lines.add(ChatColor.translateAlternateColorCodes('&', line));
                }
                im.setLore(lines);
            }
        });
    }

    public CustomItemStack(ItemStack item, Color color, String name, String... lore) {
        this(item, im -> {
            if (name != null) {
                im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }

            if (lore.length > 0) {
                List<String> lines = new ArrayList<>();

                for (String line : lore) {
                    lines.add(ChatColor.translateAlternateColorCodes('&', line));
                }
                im.setLore(lines);
            }

            if (im instanceof LeatherArmorMeta) {
                ((LeatherArmorMeta) im).setColor(color);
            }
            if (im instanceof PotionMeta) {
                ((PotionMeta) im).setColor(color);
            }
        });
    }

    public CustomItemStack addFlags(ItemFlag... flags) {
        ItemMeta im = getItemMeta();
        im.addItemFlags(flags);
        setItemMeta(im);

        return this;
    }

    public CustomItemStack setCustomModel(int data) {
        ItemMeta im = getItemMeta();
        im.setCustomModelData(data == 0 ? null : data);
        setItemMeta(im);

        return this;
    }

    public CustomItemStack(Material type, String name, String... lore) {
        this(new ItemStack(type), name, lore);
    }

    public CustomItemStack(Material type, String name, List<String> lore) {
        this(new ItemStack(type), name, lore.toArray(new String[lore.size()]));
    }

    public CustomItemStack(ItemStack item, List<String> list) {
        this(item, list.get(0), list.subList(1, list.size()).toArray(new String[0]));
    }

    public CustomItemStack(Material type, List<String> list) {
        this(new ItemStack(type), list);
    }

    public CustomItemStack(ItemStack item, int amount) {
        super(item);
        setAmount(amount);
    }

    public CustomItemStack(ItemStack item, Material type) {
        super(item);
        setType(type);
    }

}
