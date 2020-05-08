package io.github.thebusybiscuit.cscorelib2.protection.modules;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;

public class FactionsProtectionModule implements ProtectionModule {

    private BoardColl board;

    private final Plugin plugin;

    public FactionsProtectionModule(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void load() {
        board = BoardColl.get();
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, ProtectableAction action) {
        Faction faction = board.getFactionAt(PS.valueOf(l));
        if (faction == null || faction.getId().equals("none")) return true;
        if (!(p instanceof Player)) return false;

        MPlayer mp = MPlayer.get(p);
        return faction.getId().equals(mp.getFaction().getId());
    }

}
