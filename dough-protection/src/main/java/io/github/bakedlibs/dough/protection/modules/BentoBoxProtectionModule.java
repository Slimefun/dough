package io.github.bakedlibs.dough.protection.modules;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;

import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.flags.Flag;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.lists.Flags;
import world.bentobox.bentobox.managers.IslandWorldManager;
import world.bentobox.bentobox.managers.IslandsManager;

/**
 * Provides protection handling using the BentoBox API.
 * 
 * @author Poslovitch
 * @author TheBusyBiscuit
 */
public class BentoBoxProtectionModule implements ProtectionModule {

    private IslandsManager manager;
    private IslandWorldManager iwm;

    private final Plugin plugin;

    public BentoBoxProtectionModule(@Nonnull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void load() {
        manager = BentoBox.getInstance().getIslands();
        iwm = BentoBox.getInstance().getIWM();
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, Interaction action) {
        Optional<Island> island = manager.getIslandAt(l);

        if (iwm.inWorld(l)) {
            Flag flag = convert(action, l.getWorld());

            if (island.isPresent()) {
                Island is = island.get();
                User user = User.getInstance(p);

                return is.isAllowed(user, flag);
            } else {
                return flag.isSetForWorld(l.getWorld());
            }
        }

        return true;
    }

    private Flag convert(Interaction action, World world) {
        switch (action) {
            case INTERACT_BLOCK:
                return Flags.CONTAINER;
            case ATTACK_PLAYER:
                if (world.getEnvironment() == World.Environment.NETHER) {
                    return Flags.PVP_NETHER;
                } else if (world.getEnvironment() == World.Environment.THE_END) {
                    return Flags.PVP_END;
                } else {
                    return Flags.PVP_OVERWORLD;
                }
            case BREAK_BLOCK:
                return Flags.BREAK_BLOCKS;
            case ATTACK_ENTITY:
                return Flags.HURT_ANIMALS;
            case PLACE_BLOCK:
            default:
                return Flags.PLACE_BLOCKS;
        }
    }
}
