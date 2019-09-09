package io.github.thebusybiscuit.cscorelib2.config;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.NonNull;

public class Localization {
	
	private static final UnaryOperator<String> unicodes = str -> {
		if (str == null) return null;
		
		StringBuilder builder = new StringBuilder();
		char[] chars = str.toCharArray();
		
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '[' && i + 1 < chars.length && chars[i + 1] == 'u') {
				i += 2;
				
				String unicode = "";
				int j;
				
				for (j = 0; j < 6 && i < chars.length; j++) {
					if (chars[i] == ']') {
						unicode = String.valueOf((char) Integer.parseInt(unicode, 16));
						break;
					}
					unicode += chars[i];
					i++;
				}
				
				builder.append(unicode);
			}
			else {
				builder.append(chars[i]);
			}
		}
		
		return builder.toString();
	};
	
	private File file;
	
	@Getter
	private Config config;
	
	private boolean allowUnicodes = true;
	
	/**
	 * Creates a new Localization Object for the specified Plugin
	 *
	 * @param  plugin The Plugin this Localization is made for 
	 */ 
	public Localization(@NonNull Plugin plugin) {
		this.file = new File("plugins/" + plugin.getDescription().getName().replace(" ", "_"), "messages.yml");
		this.config = new Config(file);
	}
	
	/**
	 * Creates a new Localization Object for the specified Plugin
	 *
	 * @param  plugin The Plugin this Localization is made for 
	 * @param  name The Name of the file you want to use
	 */ 
	public Localization(@NonNull Plugin plugin, @NonNull String name) {
		this.file = new File("plugins/" + plugin.getDescription().getName().replace(" ", "_"), name);
		this.config = new Config(file);
	}
	
	/**
	 * Creates a new Localization Object for the specified Plugin
	 *
	 * @param  plugin The Plugin this Localization is made for 
	 * @param  name The Name of the file you want to use
	 * @param  allowUnicodes Whether Unicodes are allowed
	 */ 
	public Localization(@NonNull Plugin plugin, @NonNull String name, boolean allowUnicodes) {
		this.file = new File("plugins/" + plugin.getDescription().getName().replace(" ", "_") + "/" + name);
		this.config = new Config(file);
		this.allowUnicodes = allowUnicodes;
	}
	
	/**
	 * Sets the Default Message/s for the specified Key
	 *
	 * @param  key The Key of those Messages
	 * @param  messages The Messages which this key will refer to by default
	 */ 
	public void setDefaultMessages(String key, String... messages) {
		setDefaultMessages(key, Arrays.asList(messages));
	}
	
	public List<String> setDefaultMessages(String key, List<String> messages) {
		if (!config.contains(key)) {
			config.setValue(key, messages);
		}
		
		return getMessages(key);
	}
	
	/**
	 * Sets the Default Message/s for the specified Key
	 *
	 * @param  key The Key of those Message
	 * @param  message The Message which this key will refer to by default
	 */ 
	public String setDefaultMessage(String key, String message) {
		String msg = getMessage(message);
		
		if (msg == null) {
			config.setValue(key, message);
			return allowUnicodes ? unicodes.apply(message): message;
		}
		
		return msg;
	}
	
	/**
	 * Sets the default Message Prefix
	 *
	 * @param  prefix The Prefix by default
	 */ 
	public void setPrefix(String prefix) {
		setDefaultMessage("prefix", prefix);
	}
	
	/**
	 * Returns the Strings referring to the specified Key
	 *
	 * @param  key The Key of those Messages
	 * @return      The List this key is referring to
	 */ 
	public List<String> getMessages(String key) {
		if (!allowUnicodes) return config.getStringList(key);
		return config.getStringList(key).stream().map(unicodes).collect(Collectors.toList());
	}
	
	/**
	 * Returns the String referring to the specified Key
	 *
	 * @param  key The Key of those Messages
	 * @return      The Message this key is referring to
	 */ 
	public String getMessage(String key) {
		return allowUnicodes ? unicodes.apply(config.getString(key)): config.getString(key);
	}
	
	public void sendMessage(CommandSender sender, String key, boolean addPrefix) {
		String prefix = addPrefix && config.contains("prefix") ? getMessage("prefix"): "";
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + getMessage(key)));
	}
	
	public void sendMessage(CommandSender sender, String key, boolean addPrefix, Function<String, String> function) {
		String prefix = addPrefix && config.contains("prefix") ? getMessage("prefix"): "";
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + function.apply(getMessage(key))));
	}
	
	public void sendMessages(CommandSender sender, String key, boolean addPrefix, Function<String, String> function) {
		String prefix = addPrefix && config.contains("prefix") ? getMessage("prefix"): "";
		
		for (String translation: getMessages(key)) {
			translation = function.apply(translation);
			
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + translation));
		}
	}
	
	public void sendMessage(CommandSender sender, String key) {
		String prefix = config.contains("prefix") ? getMessage("prefix"): "";
		
		String message = ChatColor.translateAlternateColorCodes('&', prefix + getMessage(key));
		
		if (sender instanceof Player) {
			sender.sendMessage(message);
		}
		else {
			sender.sendMessage(ChatColor.stripColor(message));
		}
	}
	
	public void sendMessages(CommandSender sender, String key) {
		String prefix = config.contains("prefix") ? getMessage("prefix"): "";
		
		for (String translation: getMessages(key)) {
			String message = ChatColor.translateAlternateColorCodes('&', prefix + translation);
			
			if (sender instanceof Player) {
				sender.sendMessage(message);
			}
			else {
				sender.sendMessage(ChatColor.stripColor(message));
			}
		}
	}
	
	public void broadcastMessage(String key, boolean addPrefix, Function<String, String> function) {
		String prefix = addPrefix && config.contains("prefix") ? getMessage("prefix"): "";
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + function.apply(getMessage(key))));
	}
	
	public void broadcastMessages(String key, boolean addPrefix, Function<String, String> function) {
		String prefix = addPrefix && config.contains("prefix") ? getMessage("prefix"): "";
		
		for (String translation: getMessages(key)) {
			translation = function.apply(translation);
			
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + translation));
		}
	}
	
	public void broadcastMessage(String key) {
		String prefix = config.contains("prefix") ? getMessage("prefix"): "";
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + getMessage(key)));
	}
	
	public void broadcastMessages(String key) {
		String prefix = config.contains("prefix") ? getMessage("prefix"): "";
		
		for (String translation: getMessages(key)) {
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + translation));
		}
	}
	
	/**
	 * Reloads the messages.yml File
	 */ 
	public void reload() {
		config.reload();
	}
	
	/**
	 * Saves this Localization to its File
	 */ 
	public void save() {
		config.save();
	}

}
