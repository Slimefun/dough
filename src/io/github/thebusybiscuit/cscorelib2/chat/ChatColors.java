package io.github.thebusybiscuit.cscorelib2.chat;

import org.bukkit.ChatColor;

public final class ChatColors {

	private ChatColors() {}
	
	public static String color(String input) {
		return ChatColor.translateAlternateColorCodes('&', input);
	}
	
	public static String alternating(String text, ChatColor... colors) {
		int i = 0;
		StringBuilder builder = new StringBuilder(text.length() * 3);
		
		for (char c: text.toCharArray()) {
			builder.append(c + colors[i % colors.length].toString());
			i++;
		}
		
		return builder.toString();
	}
	
}
