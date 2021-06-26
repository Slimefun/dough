package io.github.thebusybiscuit.dough.protection.loggers;

import io.github.thebusybiscuit.dough.protection.Interaction;
import io.github.thebusybiscuit.dough.protection.ProtectionLogger;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;

import de.diddiz.LogBlock.Actor;
import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.LogBlock;
import de.diddiz.util.LoggingUtil;

public class LogBlockLogger implements ProtectionLogger {

    private Consumer consumer;

    @Override
    public void load() {
        consumer = LogBlock.getInstance().getConsumer();
    }

    @Override
    public String getName() {
        return "LogBlock";
    }

    @Override
    public void logAction(OfflinePlayer p, Block b, Interaction action) {
        if (action == Interaction.BREAK_BLOCK) {
            Actor actor = new Actor(p.getName(), p.getUniqueId());

            LoggingUtil.smartLogBlockBreak(consumer, actor, b);
        }
    }

}
