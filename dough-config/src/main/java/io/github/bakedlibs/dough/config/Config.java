package io.github.bakedlibs.dough.config;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

import io.github.bakedlibs.dough.common.DoughLogger;

public class Config {

    private final File file;

    private Logger logger;
    private String header;
    protected FileConfiguration fileConfig;

    /**
     * Creates a new Config Object for the config.yml File of
     * the specified Plugin
     *
     * @param plugin
     *            The Instance of the Plugin, the config.yml is referring to
     */
    public Config(@Nonnull Plugin plugin) {
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();

        this.logger = plugin.getLogger();
        this.file = new File("plugins/" + plugin.getName().replace(" ", "_"), "config.yml");

        this.fileConfig = YamlConfiguration.loadConfiguration(this.file);
        fileConfig.options().copyDefaults(true);
    }

    public Config(@Nonnull Plugin plugin, @Nonnull String name) {
        this.logger = plugin.getLogger();
        this.file = new File("plugins/" + plugin.getName().replace(" ", "_"), name);
        this.fileConfig = YamlConfiguration.loadConfiguration(this.file);

        fileConfig.options().copyDefaults(true);
    }

    /**
     * Creates a new Config Object for the specified File and FileConfiguration
     *
     * @param file
     *            The File to save to
     * @param config
     *            The FileConfiguration
     */
    public Config(@Nonnull File file, @Nonnull FileConfiguration config) {
        this.logger = new DoughLogger("config");
        this.file = file;

        this.fileConfig = config;
        fileConfig.options().copyDefaults(true);
    }

    /**
     * Creates a new Config Object for the specified File
     *
     * @param file
     *            The File for which the Config object is created for
     */
    public Config(@Nonnull File file) {
        this(file, YamlConfiguration.loadConfiguration(file));
    }

    /**
     * Creates a new Config Object for the File with in
     * the specified Location
     *
     * @param path
     *            The path of the File which the Config object is created for
     */
    public Config(@Nonnull String path) {
        this(new File(path));
    }

    public @Nonnull File getFile() {
        return this.file;
    }

    public @Nullable String getHeader() {
        return this.header;
    }

    public void setHeader(@Nullable String header) {
        this.header = header;
    }

    /**
     * Converts this Config Object into a plain FileConfiguration Object
     *
     * @return The converted FileConfiguration Object
     */
    public @Nonnull FileConfiguration getConfiguration() {
        return this.fileConfig;
    }

    /**
     * This sets the {@link Logger} instance to be used for this {@link Config}.
     * 
     * @param logger
     *            Your {@link Logger} instance
     */
    public void setLogger(@Nonnull Logger logger) {
        this.logger = logger;
    }

    public void clear() {
        for (String key : getKeys()) {
            setValue(key, null);
        }
    }

    protected void store(@Nonnull String path, Object value) {
        this.fileConfig.set(path, value);
    }

    /**
     * Sets the Value for the specified path
     *
     * @param path
     *            The path in the Config File
     * @param value
     *            The Value for that path
     */
    public void setValue(@Nonnull String path, Object value) {
        if (value == null) {
            this.store(path, value);
        } else if (value instanceof Optional) {
            this.store(path, ((Optional<?>) value).orElse(null));
        } else if (value instanceof Inventory) {
            this.store(path + ".size", ((Inventory) value).getSize());
            for (int i = 0; i < ((Inventory) value).getSize(); i++) {
                this.store(path + "." + i, ((Inventory) value).getItem(i));
            }
        } else if (value instanceof Date) {
            this.store(path, String.valueOf(((Date) value).getTime()));
        } else if (value instanceof Long) {
            this.store(path, String.valueOf(value));
        } else if (value instanceof UUID) {
            this.store(path, value.toString());
        } else if (value instanceof Sound) {
            this.store(path, String.valueOf(value));
        } else if (value instanceof Location) {
            this.store(path + ".x", ((Location) value).getX());
            this.store(path + ".y", ((Location) value).getY());
            this.store(path + ".z", ((Location) value).getZ());
            this.store(path + ".pitch", ((Location) value).getPitch());
            this.store(path + ".yaw", ((Location) value).getYaw());
            this.store(path + ".world", ((Location) value).getWorld().getName());
        } else if (value instanceof Chunk) {
            this.store(path + ".x", ((Chunk) value).getX());
            this.store(path + ".z", ((Chunk) value).getZ());
            this.store(path + ".world", ((Chunk) value).getWorld().getName());
        } else if (value instanceof World) {
            this.store(path, ((World) value).getName());
        } else
            this.store(path, value);
    }

    /**
     * Saves the {@link Config} to its {@link File}
     */
    public void save() {
        save(this.file);
    }

    /**
     * Saves the {@link Config} to a {@link File}
     * 
     * @param file
     *            The {@link File} you are saving this {@link Config} to
     */
    public void save(@Nonnull File file) {
        try {
            if (header != null) {
                fileConfig.options().copyHeader(true);
                fileConfig.options().header(header);
            } else {
                fileConfig.options().copyHeader(false);
            }

            fileConfig.save(file);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception while saving a Config file", e);
        }
    }

    /**
     * Sets the Value for the specified path
     * (If the path does not yet exist)
     *
     * @param path
     *            The path in the {@link Config} file
     * @param value
     *            The Value for that path
     */
    public void setDefaultValue(@Nonnull String path, @Nullable Object value) {
        if (!contains(path)) {
            setValue(path, value);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrSetDefault(@Nonnull String path, T value) {
        Object val = getValue(path);

        if (value.getClass().isInstance(val)) {
            return (T) val;
        } else {
            setValue(path, value);
            return value;
        }
    }

    /**
     * Checks whether the Config contains the specified path
     *
     * @param path
     *            The path in the Config File
     * @return True/false
     */
    public boolean contains(@Nonnull String path) {
        return fileConfig.contains(path);
    }

    /**
     * Returns the Object at the specified path
     *
     * @param path
     *            The path in the Config File
     * 
     * @return The Value at that path
     */
    @Nullable
    public Object getValue(@Nonnull String path) {
        return fileConfig.get(path);
    }

    @Nonnull
    public <T> Optional<T> getValueAs(@Nonnull Class<T> c, @Nonnull String path) {
        Object obj = getValue(path);
        return c.isInstance(obj) ? Optional.of(c.cast(obj)) : Optional.empty();
    }

    /**
     * Returns the ItemStack at the specified path
     *
     * @param path
     *            The path in the Config File
     * 
     * @return The ItemStack at that path
     */
    @Nullable
    public ItemStack getItem(@Nonnull String path) {
        return fileConfig.getItemStack(path);
    }

    /**
     * Returns the String at the specified path
     *
     * @param path
     *            The path in the Config File
     * 
     * @return The String at that path
     */
    @Nullable
    public String getString(@Nonnull String path) {
        return fileConfig.getString(path);
    }

    /**
     * Returns the Integer at the specified path
     *
     * @param path
     *            The path in the Config File
     * 
     * @return The Integer at that path
     */
    public int getInt(@Nonnull String path) {
        return fileConfig.getInt(path);
    }

    /**
     * Returns the Boolean at the specified path
     *
     * @param path
     *            The path in the Config File
     * 
     * @return The Boolean at that path
     */
    public boolean getBoolean(@Nonnull String path) {
        return fileConfig.getBoolean(path);
    }

    /**
     * Returns the StringList at the specified path
     *
     * @param path
     *            The path in the Config File
     * 
     * @return The StringList at that path
     */
    @Nonnull
    public List<String> getStringList(@Nonnull String path) {
        return fileConfig.getStringList(path);
    }

    /**
     * Returns the IntegerList at the specified path
     *
     * @param path
     *            The path in the Config File
     * 
     * @return The IntegerList at that path
     */
    @Nonnull
    public List<Integer> getIntList(@Nonnull String path) {
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
            logger.log(Level.SEVERE, "Exception while creating a Config file", e);
            return false;
        }
    }

    /**
     * Returns the Float at the specified path
     *
     * @param path
     *            The path in the Config File
     * 
     * @return The Float at that path
     */
    public float getFloat(@Nonnull String path) {
        return Float.valueOf(String.valueOf(getValue(path)));
    }

    /**
     * Returns the Long at the specified path
     *
     * @param path
     *            The path in the Config File
     * 
     * @return The Long at that path
     */
    public long getLong(@Nonnull String path) {
        return Long.valueOf(String.valueOf(getValue(path)));
    }

    /**
     * Returns the Date at the specified path
     *
     * @param path
     *            The path in the Config File
     * 
     * @return The Date at that path
     */
    public Date getDate(@Nonnull String path) {
        return new Date(getLong(path));
    }

    /**
     * Returns the Chunk at the specified path
     *
     * @param path
     *            The path in the Config File
     * 
     * @return The Chunk at that path
     */
    public Chunk getChunk(@Nonnull String path) {
        return Bukkit.getWorld(getString(path + ".world")).getChunkAt(getInt(path + ".x"), getInt(path + ".z"));
    }

    /**
     * Returns the UUID at the specified path
     *
     * @param path
     *            The path in the Config File
     * 
     * @return The UUID at that path
     */
    public UUID getUUID(@Nonnull String path) {
        String value = getString(path);
        return value != null ? UUID.fromString(value) : null;
    }

    /**
     * Returns the World at the specified path
     *
     * @param path
     *            The path in the Config File
     * 
     * @return The World at that path
     */
    public World getWorld(@Nonnull String path) {
        return Bukkit.getWorld(getString(path));
    }

    /**
     * Returns the Double at the specified path
     *
     * @param path
     *            The path in the Config File
     * 
     * @return The Double at that path
     */
    public double getDouble(@Nonnull String path) {
        return fileConfig.getDouble(path);
    }

    /**
     * Returns the Location at the specified path
     *
     * @param path
     *            The path in the Config File
     * 
     * @return The Location at that path
     */
    @Nonnull
    public Location getLocation(@Nonnull String path) {
        return new Location(Bukkit.getWorld(getString(path + ".world")), getDouble(path + ".x"), getDouble(path + ".y"), getDouble(path + ".z"), getFloat(path + ".yaw"), getFloat(path + ".pitch"));
    }

    /**
     * Gets the Contents of an Inventory at the specified path
     *
     * @param path
     *            The path in the Config File
     * @param size
     *            The Size of the Inventory
     * @param title
     *            The Title of the Inventory
     * 
     * @return The generated Inventory
     */
    @Nonnull
    public Inventory getInventory(@Nonnull String path, int size, @Nonnull String title) {
        Inventory inventory = Bukkit.createInventory(null, size, ChatColor.translateAlternateColorCodes('&', title));
        for (int i = 0; i < size; i++) {
            inventory.setItem(i, getItem(path + "." + i));
        }
        return inventory;
    }

    /**
     * Gets the Contents of an Inventory at the specified path
     *
     * @param path
     *            The path in the Config File
     * @param title
     *            The title of the inventory, this can accept &amp; for color codes.
     * 
     * @return The generated Inventory
     */
    @Nonnull
    public Inventory getInventory(@Nonnull String path, @Nonnull String title) {
        int size = getInt(path + ".size");
        Inventory inventory = Bukkit.createInventory(null, size, ChatColor.translateAlternateColorCodes('&', title));

        for (int i = 0; i < size; i++) {
            inventory.setItem(i, getItem(path + "." + i));
        }

        return inventory;
    }

    /**
     * Returns all paths in this Config
     *
     * @return All paths in this Config
     */
    @Nonnull
    public Set<String> getKeys() {
        return fileConfig.getKeys(false);
    }

    /**
     * Returns all sub-paths in this Config
     *
     * @param path
     *            The path in the Config File
     * 
     * @return All sub-paths of the specified path
     */
    @Nonnull
    public Set<String> getKeys(@Nonnull String path) {
        ConfigurationSection section = fileConfig.getConfigurationSection(path);
        return section == null ? new HashSet<>() : section.getKeys(false);
    }

    /**
     * Reloads the Configuration File
     */
    public void reload() {
        this.fileConfig = YamlConfiguration.loadConfiguration(this.file);
    }
}
