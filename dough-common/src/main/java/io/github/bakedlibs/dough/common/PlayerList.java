package io.github.bakedlibs.dough.common;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Some utility methods dealing with the {@link Player} list.
 * 
 * @author TheBusyBiscuit
 *
 */
public final class PlayerList {

    private PlayerList() {}

    /**
     * This method returns a Stream containing all online Players
     * 
     * @return A Stream of online Players
     */
    public static @Nonnull Stream<Player> stream() {
        return Bukkit.getOnlinePlayers().stream().map(Player.class::cast);
    }

    /**
     * This method returns an Optional that describes whether a Player
     * with the given Name is currently online or not.
     * 
     * @param name
     *            The name of the Player
     * @return An Optional describing the player (or an empty Optional)
     */
    public static @Nonnull Optional<Player> findByName(@Nonnull String name) {
        return stream().filter(p -> p.getName().equalsIgnoreCase(name)).findAny();
    }

    /**
     * This method returns a Set of online Players that have
     * the specified Permission.
     * 
     * @param permission
     *            The permission the Players should have
     * @return A Set of Players
     */
    public static @Nonnull Set<Player> findPermitted(@Nonnull String permission) {
        return stream().filter(p -> p.hasPermission(permission)).collect(Collectors.toSet());
    }

    /**
     * This method checks if a Player with the given name is currently
     * online on the server.
     * 
     * @param name
     *            The Name of the Player
     * @return Whether the Player is online
     */
    public static boolean isOnline(@Nonnull String name) {
        return findByName(name).isPresent();
    }

}
