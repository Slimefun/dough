package io.github.thebusybiscuit.cscorelib2.chat;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import lombok.NonNull;

public final class ChatInput {
	
	protected static ChatInputListener listener;

	private ChatInput() {}
	
	public static void waitForPlayer(@NonNull Plugin plugin, @NonNull Player p, @NonNull Consumer<String> handler) {
		waitForPlayer(plugin, p, s -> true, handler);
	}
	
	public static void waitForPlayer(@NonNull Plugin plugin, @NonNull Player p, @NonNull Predicate<String> predicate, @NonNull Consumer<String> handler) {
		if (listener == null) listener = new ChatInputListener(plugin);
		listener.handlers.put(p.getUniqueId(), new IChatInput() {
			
			@Override
			public boolean test(String msg) {
				return predicate.test(msg);
			}
			
			@Override
			public void onChat(Player p, String msg) {
				handler.accept(msg);
			}
			
		});
	}
	
	public static void waitForPlayer(@NonNull Plugin plugin, @NonNull Player p, @NonNull BiConsumer<Player, String> handler) {
		waitForPlayer(plugin, p, s -> true, handler);
	}
	
	public static void waitForPlayer(@NonNull Plugin plugin, @NonNull Player p, @NonNull Predicate<String> predicate, @NonNull BiConsumer<Player, String> handler) {
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
