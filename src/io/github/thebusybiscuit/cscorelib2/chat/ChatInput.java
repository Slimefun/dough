package io.github.thebusybiscuit.cscorelib2.chat;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class ChatInput {
	
	private static ChatInputListener listener;

	private ChatInput() {}
	
	public static void waitForPlayer(Plugin plugin, Player p, BiConsumer<Player, String> handler) {
		waitForPlayer(plugin, p, s -> true, handler);
	}
	
	public static void waitForPlayer(Plugin plugin, Player p, Predicate<String> predicate, BiConsumer<Player, String> handler) {
		if (listener == null) listener = new ChatInputListener(plugin);
		listener.handlers.put(p.getUniqueId(), new IChatInput() {
			
			@Override
			public boolean test(String msg) {
				return predicate.test(msg);
			}
			
			@Override
			public void onChat(Player p, String msg) {
				handler.accept(p, msg);
			}
			
		});
	}
	
}
