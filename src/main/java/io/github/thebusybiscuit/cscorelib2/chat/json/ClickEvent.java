package io.github.thebusybiscuit.cscorelib2.chat.json;

import com.google.gson.JsonObject;

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
	
	public JsonObject asJson() {
		return json;
	}

}
