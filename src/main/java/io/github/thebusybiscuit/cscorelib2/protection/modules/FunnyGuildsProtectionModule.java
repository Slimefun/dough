package io.github.thebusybiscuit.cscorelib2.protection.modules;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;
import lombok.NonNull;
import net.dzikoysk.funnyguilds.system.protection.ProtectionSystem;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Provides protection handling for FunnyGuilds
 *
 * @author barpec12 on 05.04.2021
 */
public class FunnyGuildsProtectionModule implements ProtectionModule {

	private final Plugin plugin;

	public FunnyGuildsProtectionModule(@NonNull Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public Plugin getPlugin() {
		return plugin;
	}

	@Override
	public void load() {
		// We don't need to load any APIs, everything is static
	}

	@Override
	public boolean hasPermission(OfflinePlayer p, Location l, ProtectableAction action) {
		return p instanceof Player && !ProtectionSystem.isProtected((Player) p, l, convert(action));
	}

	private boolean convert(ProtectableAction protectableAction) {
		return protectableAction == ProtectableAction.PLACE_BLOCK;
	}

}
