package io.github.thebusybiscuit.cscorelib2.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItem extends ItemStack {
	
	public CustomItem(ItemStack item) {
		super(item);
	}
	
	public CustomItem(Material type) {
		super(type);
	}
	
	public CustomItem(ItemStack item, String name, String... lore) {
		super(item);
		ItemMeta im = getItemMeta();
		
		if (name != null) {
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		}
		
		if (lore.length > 0) {
			List<String> lines = new ArrayList<String>();
			for (String line: lore) {
				lines.add(ChatColor.translateAlternateColorCodes('&', line));
			}
			im.setLore(lines);
		}
		
		setItemMeta(im);
	}
	
	public CustomItem addFlags(ItemFlag... flags) {
		ItemMeta im = getItemMeta();
		im.addItemFlags(flags);
		setItemMeta(im);
		
		return this;
	}
	
	public CustomItem setCustomModel(int data) {
		ItemMeta im = getItemMeta();
		im.setCustomModelData(data == 0 ? null: data);
		setItemMeta(im);
		
		return this;
	}
	
	public CustomItem(Material type, String name, String... lore) {
		this(new ItemStack(type), name, lore);
	}
	
	public CustomItem(Material type, String name, List<String> lore) {
		this(new ItemStack(type), name, lore.toArray(new String[lore.size()]));
	}
	
	public CustomItem(ItemStack item, List<String> list) {
		this(item, list.get(0), list.subList(1, list.size()).toArray(new String[list.size() - 1]));
	}
	
	public CustomItem(Material type, List<String> list) {
		this(new ItemStack(type), list);
	}

	public CustomItem(ItemStack item, int amount) {
		super(item);
		setAmount(amount);
	}

	public CustomItem(ItemStack item, Material type) {
		super(item);
		setType(type);
	}

}
