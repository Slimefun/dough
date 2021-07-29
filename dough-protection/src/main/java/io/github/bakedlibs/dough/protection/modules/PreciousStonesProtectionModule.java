package io.github.bakedlibs.dough.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;

import net.sacredlabyrinth.Phaed.PreciousStones.PreciousStones;
import net.sacredlabyrinth.Phaed.PreciousStones.api.IApi;
import net.sacredlabyrinth.Phaed.PreciousStones.field.FieldFlag;

import javax.annotation.Nonnull;

public class PreciousStonesProtectionModule implements ProtectionModule {

    private IApi api;
    private final Plugin plugin;

    public PreciousStonesProtectionModule(@Nonnull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void load() {
        api = PreciousStones.API();
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, Interaction action) {
        if (!(p instanceof Player)) {
            return false;
        }

        switch (action) {
            case ATTACK_PLAYER:
                return !api.flagAppliesToPlayer((Player) p, FieldFlag.PREVENT_PVP, l);
            case INTERACT_ENTITY:
            case ATTACK_ENTITY:
                return !api.flagAppliesToPlayer((Player) p, FieldFlag.PREVENT_ENTITY_INTERACT, l);
            case BREAK_BLOCK:
                return api.canBreak((Player) p, l);
            case INTERACT_BLOCK:
            case PLACE_BLOCK:
                return api.canPlace((Player) p, l);
            default:
                return false;
        }
    }
}
