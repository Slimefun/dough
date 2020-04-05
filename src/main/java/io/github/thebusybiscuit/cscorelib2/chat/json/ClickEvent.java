package io.github.thebusybiscuit.cscorelib2.chat.json;

import java.util.function.Consumer;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.NonNull;

public class ClickEvent {

    private final JsonObject json;
    
    @Getter
    private ClickableText clickableText;

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

    public ClickEvent(@NonNull NamespacedKey key, @NonNull Consumer<Player> callback) {
        json = new JsonObject();
        json.addProperty("action", "run_command");
        json.addProperty("value", "written_book:open - " + key.toString());
        this.clickableText = new ClickableText(key, callback);
    }

    public JsonObject asJson() {
        return json;
    }

}
