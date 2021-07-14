package io.github.bakedlibs.dough.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;

import javax.annotation.Nonnull;

public class FactionsUUIDProtectionModule implements ProtectionModule {

    private FPlayers api;
    private final Plugin plugin;

    public FactionsUUIDProtectionModule(@Nonnull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void load() {
        api = FPlayers.getInstance();
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, Interaction action) {
        Faction faction = Board.getInstance().getFactionAt(new FLocation(l));

        if (faction == null || faction.getId().equals("0")) {
            return true;
        } else {
            return faction.getId().equals(api.getByOfflinePlayer(p).getFaction().getId());
        }
    }

}
