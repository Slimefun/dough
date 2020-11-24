package io.github.thebusybiscuit.cscorelib2.protection.loggers;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;

import de.diddiz.LogBlock.Actor;
import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.LogBlock;
import de.diddiz.util.LoggingUtil;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionLogger;

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
    public void logAction(OfflinePlayer p, Block b, ProtectableAction action) {
        if (action == ProtectableAction.BREAK_BLOCK) {
            Actor actor = new Actor(p.getName(), p.getUniqueId());

            LoggingUtil.smartLogBlockBreak(consumer, actor, b);
        }
    }

}
