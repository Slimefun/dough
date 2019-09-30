package io.github.thebusybiscuit.cscorelib2.players;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class PlayerList {

	private PlayerList() {}
	
	public static Stream<Player> stream() {
		return Bukkit.getOnlinePlayers().stream().map(p -> (Player) p);
	}
	
	public static Optional<Player> findByName(String name) {
		return stream()
				.filter(p -> p.getName().equalsIgnoreCase(name))
				.findAny();
	}
	
	public static Set<Player> findPermitted(String permission) {
		return stream()
				.filter(p -> p.hasPermission(permission))
				.collect(Collectors.toSet());
	}
	
	public static boolean isOnline(String name) {
		return findByName(name).isPresent();
	}
	
}
