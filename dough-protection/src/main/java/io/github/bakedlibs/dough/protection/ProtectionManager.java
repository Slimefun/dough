package io.github.bakedlibs.dough.protection;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import io.github.bakedlibs.dough.common.DoughLogger;
import io.github.bakedlibs.dough.protection.loggers.CoreProtectLogger;
import io.github.bakedlibs.dough.protection.loggers.LogBlockLogger;
import io.github.bakedlibs.dough.protection.modules.BentoBoxProtectionModule;
import io.github.bakedlibs.dough.protection.modules.BlockLockerProtectionModule;
import io.github.bakedlibs.dough.protection.modules.ChestProtectProtectionModule;
import io.github.bakedlibs.dough.protection.modules.FactionsUUIDProtectionModule;
import io.github.bakedlibs.dough.protection.modules.FunnyGuildsProtectionModule;
import io.github.bakedlibs.dough.protection.modules.GriefPreventionProtectionModule;
import io.github.bakedlibs.dough.protection.modules.LWCProtectionModule;
import io.github.bakedlibs.dough.protection.modules.LandsProtectionModule;
import io.github.bakedlibs.dough.protection.modules.LocketteProtectionModule;
import io.github.bakedlibs.dough.protection.modules.PlotSquaredProtectionModule;
import io.github.bakedlibs.dough.protection.modules.PreciousStonesProtectionModule;
import io.github.bakedlibs.dough.protection.modules.RedProtectProtectionModule;
import io.github.bakedlibs.dough.protection.modules.TownyProtectionModule;
import io.github.bakedlibs.dough.protection.modules.WorldGuardProtectionModule;

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
     * @param plugin
     *            The plugin that uses dough.
     */
    @SuppressWarnings("java:S1612")
    public ProtectionManager(@Nonnull Server server, @Nonnull Plugin plugin) {
        logger = new DoughLogger(server, "protection");

        logger.log(Level.INFO, "Loading Protection Modules...");
        logger.log(Level.INFO, "This may happen more than once.");

        // We sadly cannot use ModuleName::new as this would load the class into memory prematurely
        registerModule(server, "WorldGuard", modulePlugin -> new WorldGuardProtectionModule(modulePlugin));
        registerModule(server, "Towny", modulePlugin -> new TownyProtectionModule(modulePlugin));
        registerModule(server, "GriefPrevention", modulePlugin -> new GriefPreventionProtectionModule(modulePlugin));
        registerModule(server, "LWC", modulePlugin -> new LWCProtectionModule(modulePlugin));
        registerModule(server, "PreciousStones", modulePlugin -> new PreciousStonesProtectionModule(modulePlugin));
        registerModule(server, "Lockette", modulePlugin -> new LocketteProtectionModule(modulePlugin));

        registerModule(server, "RedProtect", modulePlugin -> new RedProtectProtectionModule(modulePlugin));
        registerModule(server, "BentoBox", modulePlugin -> new BentoBoxProtectionModule(modulePlugin));
        registerModule(server, "BlockLocker", modulePlugin -> new BlockLockerProtectionModule(modulePlugin));
        registerModule(server, "Lands", modulePlugin -> new LandsProtectionModule(modulePlugin, plugin));
        registerModule(server, "ChestProtect", modulePlugin -> new ChestProtectProtectionModule(modulePlugin));
        registerModule(server, "Factions", modulePlugin -> new FactionsUUIDProtectionModule(modulePlugin));
        registerModule(server, "FunnyGuilds", modulePlugin -> new FunnyGuildsProtectionModule(modulePlugin));
        registerModule(server, "PlotSquared", modulePlugin -> new PlotSquaredProtectionModule(modulePlugin));

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

    public void registerLogger(@Nonnull String name, @Nonnull ProtectionLogger module) {
        protectionLoggers.add(module);
        loadModuleMSG(name);
    }

    public void registerModule(@Nonnull Server server, @Nonnull String pluginName, @Nonnull Function<Plugin, ProtectionModule> constructor) {
        Plugin plugin = server.getPluginManager().getPlugin(pluginName);

        if (plugin != null && plugin.isEnabled()) {
            registerModule(plugin, constructor);
        }
    }

    private void registerModule(@Nonnull Plugin plugin, @Nonnull Function<Plugin, ProtectionModule> constructor) {
        try {
            ProtectionModule module = constructor.apply(plugin);
            module.load();

            protectionModules.add(module);
            loadModuleMSG(module.getName() + " v" + module.getVersion());
        } catch (Throwable x) {
            logger.log(Level.SEVERE, x, () -> "An Error occured while registering the Protection Module: \"" + plugin.getName() + "\" v" + plugin.getDescription().getVersion());
        }
    }

    public void registerLogger(@Nonnull ProtectionLogger module) {
        try {
            module.load();
            registerLogger(module.getName(), module);
        } catch (Throwable x) {
            logger.log(Level.SEVERE, x, () -> "An Error occured while registering the Protection Module: \"" + module.getName() + "\"");
        }
    }

    private void loadModuleMSG(String module) {
        logger.log(Level.INFO, "Loaded Protection Module \"{0}\"", module);
    }

    public boolean hasPermission(@Nonnull OfflinePlayer p, @Nonnull Block b, @Nonnull Interaction action) {
        return hasPermission(p, b.getLocation(), action);
    }

    public boolean hasPermission(@Nonnull OfflinePlayer p, @Nonnull Location l, @Nonnull Interaction action) {
        for (ProtectionModule module : protectionModules) {
            try {
                if (!module.hasPermission(p, l, action)) {
                    return false;
                }
            } catch (Exception | LinkageError x) {
                logger.log(Level.SEVERE, x, () -> "An Error occured while querying the Protection Module: \"" + module.getName() + " v" + module.getVersion() + "\"");
                // Fallback will just be "allow".
                return true;
            }
        }

        return true;
    }

    public void logAction(@Nonnull OfflinePlayer p, @Nonnull Block b, @Nonnull Interaction action) {
        for (ProtectionLogger module : protectionLoggers) {
            try {
                module.logAction(p, b, action);
            } catch (Exception | LinkageError x) {
                logger.log(Level.SEVERE, x, () -> "An Error occured while logging for the Protection Module: \"" + module.getName() + "\"");
            }
        }
    }

}
