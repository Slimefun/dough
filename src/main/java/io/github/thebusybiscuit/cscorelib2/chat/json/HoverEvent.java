package io.github.thebusybiscuit.cscorelib2.chat.json;

import com.google.gson.JsonObject;

public class HoverEvent {
	
	private final JsonObject json;
	
	public HoverEvent(String... text) {
		json = new JsonObject();
		json.addProperty("action", "show_text");
		json.addProperty("value", String.join("\n", text));
	}
	
	public JsonObject asJson() {
		return json;
	}

}
