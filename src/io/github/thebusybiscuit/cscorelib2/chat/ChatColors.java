package io.github.thebusybiscuit.cscorelib2.chat;

import org.bukkit.ChatColor;

public final class ChatColors {

	private ChatColors() {}
	
	public static String color(String input) {
		return ChatColor.translateAlternateColorCodes('&', input);
	}
	
}
