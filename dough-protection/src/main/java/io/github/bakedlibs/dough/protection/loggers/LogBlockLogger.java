package io.github.bakedlibs.dough.protection.loggers;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionLogger;

import de.diddiz.LogBlock.Actor;
import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.LogBlock;
import de.diddiz.LogBlock.util.LoggingUtil;

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
