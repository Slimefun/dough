package io.github.bakedlibs.dough.protection.modules;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import io.github.bakedlibs.dough.protection.ActionType;
import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;

import javax.annotation.Nonnull;

public class WorldGuardProtectionModule implements ProtectionModule {

    private WorldGuardPlugin worldguard;
    private WorldGuardPlatform platform;
    private RegionContainer container;

    private final Plugin plugin;

    public WorldGuardProtectionModule(@Nonnull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void load() {
        worldguard = WorldGuardPlugin.inst();
        platform = WorldGuard.getInstance().getPlatform();
        container = platform.getRegionContainer();
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, Interaction action) {
        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(l);
        com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(l.getWorld());
        LocalPlayer player = worldguard.wrapOfflinePlayer(p);

        /*
         * if (platform.getSessionManager().hasBypass(player, world)) {
         * return true;
         * }
         */

        if (action.getType() != ActionType.BLOCK) {
            Set<ProtectedRegion> regions = container.get(world).getApplicableRegions(BlockVector3.at(l.getX(), l.getY(), l.getZ())).getRegions();

            if (regions.isEmpty()) {
                return true;
            } else {
                return container.createQuery().testState(loc, player, convert(action));
            }
        } else {
            return container.createQuery().testBuild(loc, player, convert(action));
        }
    }

    private StateFlag convert(Interaction action) {
        switch (action) {
            case ATTACK_PLAYER:
                return Flags.PVP;
            case ATTACK_ENTITY:
                return Flags.DAMAGE_ANIMALS;
            case INTERACT_BLOCK:
            case INTERACT_ENTITY:
                return Flags.USE;
            case BREAK_BLOCK:
                return Flags.BLOCK_BREAK;
            case PLACE_BLOCK:
                return Flags.BLOCK_PLACE;
            default:
                return Flags.BUILD;
        }
    }
}
