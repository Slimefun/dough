package io.github.thebusybiscuit.cscorelib2.chat.json;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.google.gson.JsonObject;

import io.github.thebusybiscuit.cscorelib2.chat.ChatInput;
import io.github.thebusybiscuit.cscorelib2.chat.IChatInput;
import lombok.NonNull;

public class ClickEvent {
	
	private final JsonObject json;
	
	public ClickEvent(@NonNull ClickEventAction action, @NonNull String value) {
		json = new JsonObject();
		json.addProperty("action", action.toString());
		json.addProperty("value", value);
	}
	
	public ClickEvent(int bookPage) {
		json = new JsonObject();
		json.addProperty("action", "change_page");
		json.addProperty("value", String.valueOf(bookPage));
	}
	
	public ClickEvent(@NonNull Plugin plugin, @NonNull Player p, @NonNull Consumer<Player> callback) {
		this(plugin, p, -1, callback);
	}
	
	public ClickEvent(@NonNull Plugin plugin, @NonNull Player p, int minutesValid, @NonNull Consumer<Player> callback) {
		json = new JsonObject();
		json.addProperty("action", "run_command");
		
		long timestamp = System.nanoTime();
		String command = " -- cs-corelib json_text_click -- " + timestamp;
		json.addProperty("value", command);
		
		ChatInput.queue(plugin, p, new IChatInput() {
			
			@Override
			public boolean isExpired() {
				if (minutesValid == -1) return false;
				else {
					return System.nanoTime() >= timestamp + TimeUnit.NANOSECONDS.convert(minutesValid, TimeUnit.MINUTES);
				}
			}
			
			@Override
			public boolean test(String text) {
				return text.equals(command);
			}
			
			@Override
			public void onChat(Player p, String msg) {
				callback.accept(p);
			}
		});
	}
	
	public JsonObject asJson() {
		return json;
	}

}
