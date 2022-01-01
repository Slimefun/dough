package io.github.bakedlibs.dough.common;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginLogger;

/**
 * A utility {@link Logger} implementation which automatically sets the
 * {@link Server} as its parent {@link Logger}.
 * 
 * This allows us to properly log messages and warnings without the need
 * for a {@link PluginLogger}.
 * 
 * @author TheBusyBiscuit
 *
 */
public class DoughLogger extends Logger {

    public DoughLogger(@Nonnull Server server, @Nonnull String name) {
        super("dough: " + name, null);

        setParent(server.getLogger());
        setLevel(Level.ALL);
    }

    public DoughLogger(@Nonnull String name) {
        this(Bukkit.getServer(), name);
    }

}
