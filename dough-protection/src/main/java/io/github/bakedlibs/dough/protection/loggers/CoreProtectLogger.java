package io.github.bakedlibs.dough.protection.loggers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionLogger;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;

public class CoreProtectLogger implements ProtectionLogger {

    private CoreProtectAPI coreprotect;

    @Override
    public void load() {
        coreprotect = ((CoreProtect) Bukkit.getPluginManager().getPlugin("CoreProtect")).getAPI();
    }

    @Override
    public String getName() {
        return "CoreProtect";
    }

    @Override
    public void logAction(OfflinePlayer p, Block b, Interaction action) {
        switch (action) {
            case INTERACT_BLOCK:
                coreprotect.logContainerTransaction(p.getName(), b.getLocation());
                break;
            case BREAK_BLOCK:
                coreprotect.logRemoval(p.getName(), b.getLocation(), b.getType(), b.getBlockData());
                break;
            case PLACE_BLOCK:
                coreprotect.logPlacement(p.getName(), b.getLocation(), b.getType(), b.getBlockData());
                break;
            default:
                break;
        }
    }

}
