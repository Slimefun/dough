package io.github.thebusybiscuit.cscorelib2.item;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;
import lombok.NonNull;

/**
 * This is an immutable version of ItemMeta.
 * Use this class to optimize your ItemStack#getItemMeta() calls by returning
 * a field of this immutable copy.
 * 
 * @author TheBusyBiscuit
 *
 */
@Getter
public class ImmutableItemMeta {
	
	private final Optional<String> displayName;
	private final Optional<List<String>> lore;
	private final Optional<Integer> customModelData;
	
	private final Set<ItemFlag> itemFlags;
	private final Map<Enchantment, Integer> enchants;
	
	public ImmutableItemMeta(Supplier<ItemMeta> supplier) {
		this(supplier.get());
	}
	
	public ImmutableItemMeta(ItemStack item) {
		this(item.getItemMeta());
	}
	
	public ImmutableItemMeta(@NonNull ItemMeta meta) {
		this.displayName = meta.hasDisplayName() ? Optional.of(meta.getDisplayName()): Optional.empty();
		this.lore = meta.hasLore() ? Optional.of(meta.getLore()): Optional.empty();
		this.customModelData = meta.hasCustomModelData() ? Optional.of(meta.getCustomModelData()): Optional.empty();
		
		this.itemFlags = meta.getItemFlags();
		this.enchants = meta.getEnchants();
	}
}
