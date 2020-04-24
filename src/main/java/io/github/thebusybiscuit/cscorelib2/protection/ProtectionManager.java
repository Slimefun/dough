package io.github.thebusybiscuit.cscorelib2.protection;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
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

        registerModule(server, "WorldGuard", WorldGuardProtectionModule::new);
        registerModule(server, "Towny", TownyProtectionModule::new);
        registerModule(server, "GriefPrevention", GriefPreventionProtectionModule::new);
        registerModule(server, "ASkyBlock", ASkyBlockProtectionModule::new);
        registerModule(server, "LWC", LWCProtectionModule::new);
        registerModule(server, "PreciousStones", PreciousStonesProtectionModule::new);
        registerModule(server, "Lockette", LocketteProtectionModule::new);

        registerModule(server, "RedProtect", RedProtectProtectionModule::new);
        registerModule(server, "BentoBox", BentoBoxProtectionModule::new);
        registerModule(server, "BlockLocker", BlockLockerProtectionModule::new);
        registerModule(server, "Lands", LandsProtectionModule::new);

        if (server.getPluginManager().isPluginEnabled("Factions")) {
            if (server.getPluginManager().getPlugin("Factions").getDescription().getDepend().contains("MassiveCore")) {
                registerModule(server, "Factions", FactionsProtectionModule::new);
            }
            else {
                registerModule(server, "FactionsUUID", FactionsUUIDProtectionModule::new);
            }
        }

        if (server.getPluginManager().isPluginEnabled("PlotSquared")) {
            Plugin plotSquared = server.getPluginManager().getPlugin("PlotSquared");

            if (plotSquared.getDescription().getVersion().startsWith("4.")) {
                registerModule(plotSquared, "PlotSquared v4", PlotSquared4ProtectionModule::new);
            }
            else {
                registerModule(plotSquared, "PlotSquared v5", PlotSquared5ProtectionModule::new);
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

    public void registerModule(@NonNull Server server, @NonNull String pluginName, @NonNull Supplier<ProtectionModule> supplier) {
        Plugin plugin = server.getPluginManager().getPlugin(pluginName);

        if (plugin.isEnabled()) {
            registerModule(plugin, pluginName, supplier);
        }
    }

    private void registerModule(@NonNull Plugin plugin, @NonNull String name, @NonNull Supplier<ProtectionModule> supplier) {
        try {
            ProtectionModule module = supplier.get();
            module.load();

            protectionModules.add(module);
            loadModuleMSG(name + " v" + plugin.getDescription().getVersion());
        }
        catch (Throwable x) {
            logger.log(Level.SEVERE, x, () -> "An Error occured while registering the Protection Module: \"" + name + "\" v" + plugin.getDescription().getVersion());
        }
    }

    public void registerLogger(@NonNull ProtectionLogger module) {
        try {
            module.load();
            registerLogger(module.getName(), module);
        }
        catch (Throwable x) {
            logger.log(Level.SEVERE, "An Error occured while registering the Protection Module: \"" + module.getName() + "\"", x);
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
                logger.log(Level.SEVERE, "An Error occured while querying the Protection Module: \"" + module.getName() + "\"", x);
                return false;
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
                logger.log(Level.SEVERE, "An Error occured while logging for the Protection Module: \"" + module.getName() + "\"", x);
            }
        }
    }

}
