package io.github.thebusybiscuit.cscorelib2.protection;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.protection.loggers.CoreProtectLogger;
import io.github.thebusybiscuit.cscorelib2.protection.loggers.LogBlockLogger;
import io.github.thebusybiscuit.cscorelib2.protection.modules.ASkyBlockProtectionModule;
import io.github.thebusybiscuit.cscorelib2.protection.modules.BentoBoxProtectionModule;
import io.github.thebusybiscuit.cscorelib2.protection.modules.BlockLockerProtectionModule;
import io.github.thebusybiscuit.cscorelib2.protection.modules.FactionsProtectionModule;
import io.github.thebusybiscuit.cscorelib2.protection.modules.FactionsUUIDProtectionModule;
import io.github.thebusybiscuit.cscorelib2.protection.modules.GriefPreventionProtectionModule;
import io.github.thebusybiscuit.cscorelib2.protection.modules.LWCProtectionModule;
import io.github.thebusybiscuit.cscorelib2.protection.modules.LandsProtectionModule;
import io.github.thebusybiscuit.cscorelib2.protection.modules.LocketteProtectionModule;
import io.github.thebusybiscuit.cscorelib2.protection.modules.PlotSquared4ProtectionModule;
import io.github.thebusybiscuit.cscorelib2.protection.modules.PlotSquared5ProtectionModule;
import io.github.thebusybiscuit.cscorelib2.protection.modules.PreciousStonesProtectionModule;
import io.github.thebusybiscuit.cscorelib2.protection.modules.RedProtectProtectionModule;
import io.github.thebusybiscuit.cscorelib2.protection.modules.TownyProtectionModule;
import io.github.thebusybiscuit.cscorelib2.protection.modules.WorldGuardProtectionModule;
import io.github.thebusybiscuit.cscorelib2.protection.modules.ChestProtectProtectionModule;
import lombok.NonNull;

/**
 * This Class provides a nifty API for plugins to query popular protection plugins.
 *
 * @author TheBusyBiscuit
 *
 */
public final class ProtectionManager {

    private final Set<ProtectionModule> protectionModules = new HashSet<>();
    private final Set<ProtectionLogger> protectionLoggers = new HashSet<>();
    private final Logger logger;

    /**
     * This creates a new instance of {@link ProtectionManager}, you can see this
     * as a "Snapshot" of your plugins too.
     *
     * @param server
     *            The Server your plugin is running on.
     */
    public ProtectionManager(@NonNull Server server) {
        logger = getLogger(server);

        logger.log(Level.INFO, "Loading Protection Modules...");
        logger.log(Level.INFO, "This may happen more than once.");

        // We sadly cannot use ModuleName::new as this would load the class into memory prematurely
        registerModule(server, "WorldGuard", plugin -> new WorldGuardProtectionModule(plugin));
        registerModule(server, "Towny", plugin -> new TownyProtectionModule(plugin));
        registerModule(server, "GriefPrevention", plugin -> new GriefPreventionProtectionModule(plugin));
        registerModule(server, "ASkyBlock", plugin -> new ASkyBlockProtectionModule(plugin));
        registerModule(server, "LWC", plugin -> new LWCProtectionModule(plugin));
        registerModule(server, "PreciousStones", plugin -> new PreciousStonesProtectionModule(plugin));
        registerModule(server, "Lockette", plugin -> new LocketteProtectionModule(plugin));

        registerModule(server, "RedProtect", plugin -> new RedProtectProtectionModule(plugin));
        registerModule(server, "BentoBox", plugin -> new BentoBoxProtectionModule(plugin));
        registerModule(server, "BlockLocker", plugin -> new BlockLockerProtectionModule(plugin));
        registerModule(server, "Lands", plugin -> new LandsProtectionModule(plugin));
        registerModule(server, "ChestProtect", plugin -> new ChestProtectProtectionModule(plugin));

        if (server.getPluginManager().isPluginEnabled("Factions")) {
            if (server.getPluginManager().getPlugin("Factions").getDescription().getDepend().contains("MassiveCore")) {
                registerModule(server, "Factions", plugin -> new FactionsProtectionModule(plugin));
            }
            else {
                registerModule(server, "FactionsUUID", plugin -> new FactionsUUIDProtectionModule(plugin));
            }
        }

        if (server.getPluginManager().isPluginEnabled("PlotSquared")) {
            Plugin plotSquared = server.getPluginManager().getPlugin("PlotSquared");

            if (plotSquared.getDescription().getVersion().startsWith("4.")) {
                registerModule(plotSquared, plugin -> new PlotSquared4ProtectionModule(plugin));
            }
            else {
                registerModule(plotSquared, plugin -> new PlotSquared5ProtectionModule(plugin));
            }
        }

        /*
         * The following Plugins are logging plugins, not protection plugins
         */

        if (server.getPluginManager().isPluginEnabled("CoreProtect")) {
            registerLogger(new CoreProtectLogger());
        }
        if (server.getPluginManager().isPluginEnabled("LogBlock")) {
            registerLogger(new LogBlockLogger());
        }

        /*
         * The following Plugins work by utilising one of the above listed
         * Plugins in the background.
         * We do not need a module for them, but let us make the server owner
         * aware that this compatibility exists.
         */

        if (server.getPluginManager().isPluginEnabled("ProtectionStones")) {
            loadModuleMSG("ProtectionStones");
        }
        if (server.getPluginManager().isPluginEnabled("uSkyblock")) {
            loadModuleMSG("uSkyblock");
        }
    }

    /**
     * This is our custom Logger for this class.
     *
     * @param server
     *            The Server this is logging to
     * @return A new instance of {@link Logger}
     */
    private Logger getLogger(Server server) {
        Logger customLogger = new Logger("CS-CoreLib2", null) {

            @Override
            public void log(@NonNull LogRecord logRecord) {
                logRecord.setMessage("[CS-CoreLib2 - Protection] " + logRecord.getMessage());
                super.log(logRecord);
            }

        };

        customLogger.setParent(server.getLogger());
        customLogger.setLevel(Level.ALL);

        return customLogger;
    }

    public void registerLogger(@NonNull String name, @NonNull ProtectionLogger module) {
        protectionLoggers.add(module);
        loadModuleMSG(name);
    }

    public void registerModule(@NonNull Server server, @NonNull String pluginName, @NonNull Function<Plugin, ProtectionModule> constructor) {
        Plugin plugin = server.getPluginManager().getPlugin(pluginName);

        if (plugin != null && plugin.isEnabled()) {
            registerModule(plugin, constructor);
        }
    }

    private void registerModule(@NonNull Plugin plugin, @NonNull Function<Plugin, ProtectionModule> constructor) {
        try {
            ProtectionModule module = constructor.apply(plugin);
            module.load();

            protectionModules.add(module);
            loadModuleMSG(module.getName() + " v" + module.getVersion());
        }
        catch (Throwable x) {
            logger.log(Level.SEVERE, x, () -> "An Error occured while registering the Protection Module: \"" + plugin.getName() + "\" v" + plugin.getDescription().getVersion());
        }
    }

    public void registerLogger(@NonNull ProtectionLogger module) {
        try {
            module.load();
            registerLogger(module.getName(), module);
        }
        catch (Throwable x) {
            logger.log(Level.SEVERE, x, () -> "An Error occured while registering the Protection Module: \"" + module.getName() + "\"");
        }
    }

    private void loadModuleMSG(String module) {
        logger.log(Level.INFO, "Loaded Protection Module \"{0}\"", module);
    }

    public boolean hasPermission(@NonNull OfflinePlayer p, @NonNull Block b, @NonNull ProtectableAction action) {
        return hasPermission(p, b.getLocation(), action);
    }

    public boolean hasPermission(@NonNull OfflinePlayer p, @NonNull Location l, @NonNull ProtectableAction action) {
        for (ProtectionModule module : protectionModules) {
            try {
                if (!module.hasPermission(p, l, action)) {
                    return false;
                }
            }
            catch (Exception x) {
                logger.log(Level.SEVERE, x, () -> "An Error occured while querying the Protection Module: \"" + module.getName() + " v" + module.getVersion() + "\"");
                // Fallback will just be "allow".
                return true;
            }
        }

        return true;
    }

    public void logAction(@NonNull OfflinePlayer p, @NonNull Block b, @NonNull ProtectableAction action) {
        for (ProtectionLogger module : protectionLoggers) {
            try {
                module.logAction(p, b, action);
            }
            catch (Exception x) {
                logger.log(Level.SEVERE, x, () -> "An Error occured while logging for the Protection Module: \"" + module.getName() + "\"");
            }
        }
    }

}
