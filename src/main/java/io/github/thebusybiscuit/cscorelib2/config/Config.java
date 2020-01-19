package io.github.thebusybiscuit.cscorelib2.config;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.NonNull;

public class Config implements AbstractConfig {
	
	@Getter
	private File file;
	
	protected FileConfiguration fileConfig;
	
	/**
	 * Creates a new Config Object for the config.yml File of
	 * the specified Plugin
	 *
	 * @param  plugin The Instance of the Plugin, the config.yml is referring to
	 */
	public Config(@NonNull Plugin plugin) {
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveConfig();
		
		this.file = new File("plugins/" + plugin.getName().replace(" ", "_"), "config.yml");
		this.fileConfig = YamlConfiguration.loadConfiguration(this.file);
		fileConfig.options().copyDefaults(true);
	}
	
	public Config(@NonNull Plugin plugin, @NonNull String name) {
		this.file = new File("plugins/" + plugin.getName().replace(" ", "_"), name);
		this.fileConfig = YamlConfiguration.loadConfiguration(this.file);
		fileConfig.options().copyDefaults(true);
	}
	
	/**
	 * Creates a new Config Object for the specified File and FileConfiguration
	 *
	 * @param  file The File to save to
	 * @param  config The FileConfiguration
	 */
	public Config(@NonNull File file, @NonNull FileConfiguration config) {
		this.file = file;
		this.fileConfig = config;
		config.options().copyDefaults(true);
	}
	
	/**
	 * Creates a new Config Object for the specified File
	 *
	 * @param  file The File for which the Config object is created for
	 */
	public Config(@NonNull File file) {
		this(file, YamlConfiguration.loadConfiguration(file));
	}
	
	/**
	 * Creates a new Config Object for the File with in
	 * the specified Location
	 *
	 * @param  path The Path of the File which the Config object is created for
	 */
	public Config(@NonNull String path) {
		this(new File(path));
	}
	
	/**
	 * Converts this Config Object into a plain FileConfiguration Object
	 *
	 * @return      The converted FileConfiguration Object
	 */ 
	@Override
	public FileConfiguration getConfiguration() {
		return this.fileConfig;
	}

	@Override
	public void clear() {
		for (String key : getKeys()) {
			setValue(key, null);
		}
	}
	
	protected void store(@NonNull String path, Object value) {
		this.fileConfig.set(path, value);
	}
	
	/**
	 * Sets the Value for the specified Path
	 *
	 * @param  path The path in the Config File
	 * @param  value The Value for that Path
	 */
	@Override
	public void setValue(@NonNull String path, Object value) {
		if (value == null) {
			this.store(path, value);
		}
		else if (value instanceof Inventory) {
			this.store(path + ".size", ((Inventory) value).getSize());
			for (int i = 0; i < ((Inventory) value).getSize(); i++) {
				this.store(path + "." + i, ((Inventory) value).getItem(i));
			}
		}
		else if (value instanceof Date) {
			this.store(path, String.valueOf(((Date) value).getTime()));
		}
		else if (value instanceof Long) {
			this.store(path, String.valueOf(value));
		}
		else if (value instanceof UUID) {
			this.store(path, value.toString());
		}
		else if (value instanceof Sound) {
			this.store(path, String.valueOf(value));
		}
		else if (value instanceof Location) {
			this.store(path + ".x", ((Location) value).getX());
			this.store(path + ".y", ((Location) value).getY());
			this.store(path + ".z", ((Location) value).getZ());
			this.store(path + ".pitch", ((Location) value).getPitch());
			this.store(path + ".yaw", ((Location) value).getYaw());
			this.store(path + ".world", ((Location) value).getWorld().getName());
		}
		else if (value instanceof Chunk) {
			this.store(path + ".x", ((Chunk) value).getX());
			this.store(path + ".z", ((Chunk) value).getZ());
			this.store(path + ".world", ((Chunk) value).getWorld().getName());
		}
		else if (value instanceof World) {
			this.store(path, ((World) value).getName());
		}
		else this.store(path, value);
	}
	
	/**
	 * Saves the Config Object to its File
	 */ 
	@Override
	public void save() {
		try {
			fileConfig.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves the Config Object to a File
	 * 
	 * @param  file The File you are saving this Config to
	 */ 
	@Override
	public void save(@NonNull File file) {
		try {
			fileConfig.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Sets the Value for the specified Path 
	 * (IF the Path does not yet exist)
	 *
	 * @param  path The path in the Config File
	 * @param  value The Value for that Path
	 */
	@Override
	public void setDefaultValue(@NonNull String path, Object value) {
		if (!contains(path)) setValue(path, value);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getOrSetDefault(@NonNull String path, T value) {
		Object val = getValue(path);
		
		if (value.getClass().isInstance(val)) {
			return (T) val;
		}
		else {
			setValue(path, value);
			return value;
		}
	}
	
	/**
	 * Checks whether the Config contains the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      True/false
	 */ 
	@Override
	public boolean contains(@NonNull String path) {
		return fileConfig.contains(path);
	}
	
	/**
	 * Returns the Object at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The Value at that Path
	 */ 
	@Override
	public Object getValue(@NonNull String path) {
		return fileConfig.get(path);
	}
	
	@Override
	public <T> Optional<T> getValueAs(@NonNull Class<T> c, @NonNull String path) {
		Object obj = getValue(path);
		return c.isInstance(obj) ? Optional.of(c.cast(obj)): Optional.empty();
	}
	
	
	/**
	 * Returns the ItemStack at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The ItemStack at that Path
	 */ 
	@Override
	public ItemStack getItem(@NonNull String path) {
		return fileConfig.getItemStack(path);
	}
	
	/**
	 * Returns the String at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The String at that Path
	 */ 
	@Override
	public String getString(@NonNull String path) {
		return fileConfig.getString(path);
	}
	
	/**
	 * Returns the Integer at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The Integer at that Path
	 */ 
	@Override
	public int getInt(@NonNull String path) {
		return fileConfig.getInt(path);
	}
	
	/**
	 * Returns the Boolean at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The Boolean at that Path
	 */ 
	@Override
	public boolean getBoolean(@NonNull String path) {
		return fileConfig.getBoolean(path);
	}
	
	/**
	 * Returns the StringList at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The StringList at that Path
	 */ 
	@Override
	public List<String> getStringList(@NonNull String path) {
		return fileConfig.getStringList(path);
	}
	
	/**
	 * Returns the IntegerList at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The IntegerList at that Path
	 */ 
	public List<Integer> getIntList(@NonNull String path) {
		return fileConfig.getIntegerList(path);
	}
	
	/**
	 * Recreates the File of this Config
	 *
	 * @return Returns if the file was successfully created
	 */ 
	public boolean createFile() {
		try {
			return this.file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Returns the Float at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The Float at that Path
	 */ 
	@Override
	public float getFloat(@NonNull String path) {
		return Float.valueOf(String.valueOf(getValue(path)));
	}
	
	/**
	 * Returns the Long at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The Long at that Path
	 */ 
	@Override
	public long getLong(@NonNull String path) {
		return Long.valueOf(String.valueOf(getValue(path)));
	}

	/**
	 * Returns the Sound at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The Sound at that Path
	 */ 
	@Override
	public Sound getSound(@NonNull String path) {
		return Sound.valueOf(getString(path));
	}
	
	/**
	 * Returns the Date at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The Date at that Path
	 */ 
	@Override
	public Date getDate(@NonNull String path) {
		return new Date(getLong(path));
	}
	
	/**
	 * Returns the Chunk at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The Chunk at that Path
	 */ 
	public Chunk getChunk(@NonNull String path) {
		return Bukkit.getWorld(getString(path + ".world")).getChunkAt(getInt(path + ".x"), getInt(path + ".z"));
	}
	
	/**
	 * Returns the UUID at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The UUID at that Path
	 */ 
	@Override
	public UUID getUUID(@NonNull String path) {
		return UUID.fromString(getString(path));
	}
	
	/**
	 * Returns the World at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The World at that Path
	 */ 
	public World getWorld(@NonNull String path) {
		return Bukkit.getWorld(getString(path));
	}
	
	/**
	 * Returns the Double at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The Double at that Path
	 */ 
	@Override
	public double getDouble(@NonNull String path) {
		return fileConfig.getDouble(path);
	}
	
	/**
	 * Returns the Location at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @return      The Location at that Path
	 */ 
	public Location getLocation(@NonNull String path) {
		return new Location(
				Bukkit.getWorld(
				getString(path + ".world")),
				getDouble(path + ".x"),
				getDouble(path + ".y"),
				getDouble(path + ".z"),
				getFloat(path + ".yaw"),
				getFloat(path + ".pitch")
		);
	}
	
	/**
	 * Gets the Contents of an Inventory at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @param  size The Size of the Inventory
	 * @param  title The Title of the Inventory
	 * @return      The generated Inventory
	 */ 
	public Inventory getInventory(@NonNull String path, int size, @NonNull String title) {
		Inventory inventory = Bukkit.createInventory(null, size, ChatColor.translateAlternateColorCodes('&', title));
		for (int i = 0; i < size; i++) {
			inventory.setItem(i, getItem(path + "." + i));
		}
		return inventory;
	}
	
	/**
	 * Gets the Contents of an Inventory at the specified Path
	 *
	 * @param  path The path in the Config File
	 * @param title The title of the inventory, this can accept &amp; for color codes.
	 * @return      The generated Inventory
	 */ 
	public Inventory getInventory(@NonNull String path, @NonNull String title) {
		int size = getInt(path + ".size");
		Inventory inventory = Bukkit.createInventory(null, size, ChatColor.translateAlternateColorCodes('&', title));
		
		for (int i = 0; i < size; i++) {
			inventory.setItem(i, getItem(path + "." + i));
		}
		
		return inventory;
	}
	
	/**
	 * Returns all Paths in this Config
	 *
	 * @return      All Paths in this Config
	 */ 
	@Override
	public Set<String> getKeys() {
		return fileConfig.getKeys(false);
	}
	
	/**
	 * Returns all Sub-Paths in this Config
	 *
	 * @param  path The path in the Config File
	 * @return      All Sub-Paths of the specified Path
	 */ 
	@Override
	public Set<String> getKeys(@NonNull String path) {
		ConfigurationSection section = fileConfig.getConfigurationSection(path);
		return section == null ? new HashSet<>(): section.getKeys(false);
	}
	
	/**
	 * Reloads the Configuration File
	 */ 
	@Override
	public void reload() {
		this.fileConfig = YamlConfiguration.loadConfiguration(this.file);
	}
}
