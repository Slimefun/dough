package io.github.thebusybiscuit.cscorelib2.players;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class PlayerList {

	private PlayerList() {}
	
	public static Optional<Player> findByName(String name) {
		return Bukkit.getOnlinePlayers().stream()
				.filter(p -> p.getName().equalsIgnoreCase(name))
				.findAny()
				.map(p -> (Player) p);
	}
	
	public static Set<Player> findPermitted(String permission) {
		return Bukkit.getOnlinePlayers().stream()
				.filter(p -> p.hasPermission(permission))
				.map(p -> (Player) p)
				.collect(Collectors.toSet());
	}
	
}
