package io.github.thebusybiscuit.dough.protection;

import javax.annotation.Nonnull;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;

public interface ProtectionLogger {

    void load();

    @Nonnull
    String getName();

    void logAction(@Nonnull OfflinePlayer p, @Nonnull Block b, @Nonnull Interaction action);

}
