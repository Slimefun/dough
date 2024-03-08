package io.github.bakedlibs.dough.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;

import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

import javax.annotation.Nonnull;

public class GriefPreventionProtectionModule implements ProtectionModule {

    private GriefPrevention griefPrevention;
    private boolean useClaimPermission = false;

    private final Plugin plugin;

    public GriefPreventionProtectionModule(@Nonnull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void load() {
        griefPrevention = GriefPrevention.instance;

        try {
            Claim.class.getDeclaredMethod("checkPermission", Player.class, ClaimPermission.class, Event.class);
            useClaimPermission = true;
        } catch (NoSuchMethodException ignored) {
            // Very dated GP version, use legacy methods.
        }
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, Interaction action) {
        World world = l.getWorld();

        // Check if GP is handling the world at all.
        if (world == null || !griefPrevention.claimsEnabledForWorld(world)) {
            return true;
        }

        // Fetch claim. Using player's cached claim is unlikely to speed up the process for a generalized check.
        Claim claim = griefPrevention.dataStore.getClaimAt(l, true, null);

        if (claim == null) {
            return true;
        } else if (action == Interaction.ATTACK_PLAYER) {
            return !griefPrevention.claimIsPvPSafeZone(claim);
        } else if (p.getUniqueId().equals(claim.ownerID)) {
            return true;
        }

        if (useClaimPermission) {
            // If Claim#checkPermission is available, prefer it.
            return checkPermission(claim, p, action);
        }

        // Otherwise, legacy method requires an online player.
        if (!(p instanceof Player)) {
            return false;
        }

        return checkLegacy(claim, (Player) p, action, l);
    }

    private boolean checkPermission(@Nonnull Claim claim, @Nonnull OfflinePlayer offline, @Nonnull Interaction action) {
        // Do our best to translate Interaction to ClaimPermission.
        ClaimPermission permission;
        if (action == Interaction.INTERACT_BLOCK || action == Interaction.ATTACK_ENTITY) {
            permission = ClaimPermission.Inventory;
        } else if (action == Interaction.BREAK_BLOCK || action == Interaction.PLACE_BLOCK) {
            permission = ClaimPermission.Build;
        } else {
            permission = ClaimPermission.Access;
        }

        // If the player is online, support permission-based allowance.
        if (offline instanceof Player) {
            return claim.checkPermission((Player) offline, permission, null) == null;
        }

        // Fall through to explicit allowance for offline players.
        return claim.checkPermission(offline.getUniqueId(), permission, null) == null;
    }

    private boolean checkLegacy(@Nonnull Claim claim, @Nonnull Player player, @Nonnull Interaction action, @Nonnull Location location) {
        switch (action) {
            case INTERACT_BLOCK:
                return claim.allowContainers(player) == null;
            case BREAK_BLOCK:
                return claim.allowBreak(player, location.getBlock().getType()) == null;
            case PLACE_BLOCK:
                return claim.allowBuild(player, location.getBlock().getType()) == null;
            default:
                return claim.allowAccess(player) == null;
        }
    }

}
