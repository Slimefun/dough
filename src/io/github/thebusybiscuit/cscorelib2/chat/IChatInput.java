package io.github.thebusybiscuit.cscorelib2.chat;

import java.util.function.Predicate;

import org.bukkit.entity.Player;

interface IChatInput extends Predicate<String> {
	
	void onChat(Player p, String msg);

}
