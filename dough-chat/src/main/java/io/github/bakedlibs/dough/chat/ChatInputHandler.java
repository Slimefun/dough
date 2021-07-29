package io.github.bakedlibs.dough.chat;

import java.util.function.Predicate;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;

public interface ChatInputHandler extends Predicate<String> {

    @ParametersAreNonnullByDefault
    void onChat(Player p, String msg);

}
