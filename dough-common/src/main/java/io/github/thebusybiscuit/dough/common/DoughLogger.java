package io.github.thebusybiscuit.dough.common;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import org.bukkit.Server;

public class DoughLogger extends Logger {

    public DoughLogger(@Nonnull Server server, @Nonnull String name) {
        super("dough: " + name, null);

        setParent(server.getLogger());
        setLevel(Level.ALL);
    }

    @Override
    public void log(@Nonnull LogRecord logRecord) {
        logRecord.setMessage(logRecord.getMessage());
        super.log(logRecord);
    }

}
