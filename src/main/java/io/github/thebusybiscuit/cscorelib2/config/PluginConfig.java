package io.github.thebusybiscuit.cscorelib2.config;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public interface PluginConfig {
	
	File getFile();
	FileConfiguration getConfiguration();
	
	void reload();
	void save();
	void save(File file);
	
	void setValue(String path, Object value);
	void setDefaultValue(String path, Object value);
	
	<T> T getOrSetDefault(String path, T value);
	
	boolean contains(String path);
	
	Object getValue(String path);
	<T> Optional<T> getValueAs(Class<T> c, String path);
	
	ItemStack getItem(String path);
	String getString(String path);
	int getInt(String path);
	double getDouble(String path);
	float getFloat(String path);
	long getLong(String path);
	boolean getBoolean(String path);
	List<String> getStringList(String path);
	List<Integer> getIntList(String path);
	Sound getSound(String path);
	Date getDate(String path);
	UUID getUUID(String path);
	
	Set<String> getKeys();
	Set<String> getKeys(String path);

}
