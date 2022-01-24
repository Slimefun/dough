package io.github.bakedlibs.dough.protection;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import io.github.bakedlibs.dough.common.DoughLogger;
import io.github.bakedlibs.dough.protection.loggers.CoreProtectLogger;
import io.github.bakedlibs.dough.protection.loggers.LogBlockLogger;
import io.github.bakedlibs.dough.protection.modules.BentoBoxProtectionModule;
import io.github.bakedlibs.dough.protection.modules.BlockLockerProtectionModule;
import io.github.bakedlibs.dough.protection.modules.ChestProtectProtectionModule;
import io.github.bakedlibs.dough.protection.modules.FactionsUUIDProtectionModule;
import io.github.bakedlibs.dough.protection.modules.FunnyGuildsProtectionModule;
import io.github.bakedlibs.dough.protection.modules.GriefPreventionProtectionModule;
import io.github.bakedlibs.dough.protection.modules.HuskTownsProtectionModule;
import io.github.bakedlibs.dough.protection.modules.LWCProtectionModule;
import io.github.bakedlibs.dough.protection.modules.LandsProtectionModule;
import io.github.bakedlibs.dough.protection.modules.LocketteProtectionModule;
import io.github.bakedlibs.dough.protection.modules.PlotSquaredProtectionModule;
import io.github.bakedlibs.dough.protection.modules.PreciousStonesProtectionModule;
import io.github.bakedlibs.dough.protection.modules.RedProtectProtectionModule;
import io.github.bakedlibs.dough.protection.modules.ShopChestProtectionModule;
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
     * @param plugin
     *            The plugin instance that integrates dough.
     */
    public ProtectionManager(@Nonnull Plugin plugin) {
        logger = new DoughLogger(plugin.getServer(), "protection");

        logger.log(Level.INFO, "Loading Protection Modules...");
        logger.log(Level.INFO, "This may happen more than once.");

        /*
         * The following plugins are protection plugins.
         */
        loadModuleImplementations(plugin);

        /*
         * The following Plugins are logging plugins, not protection plugins.
         */
        loadLoggerImplementations(plugin);
    }

    @ParametersAreNonnullByDefault
    @SuppressWarnings("java:S1612")
    private void loadModuleImplementations(Plugin plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();

        // We sadly cannot use ModuleName::new as this would load the class into memory prematurely
        registerModule(pm, "WorldGuard", worldGuard -> new WorldGuardProtectionModule(worldGuard));
        registerModule(pm, "Towny", towny -> new TownyProtectionModule(towny));
        registerModule(pm, "GriefPrevention", griefPrevention -> new GriefPreventionProtectionModule(griefPrevention));
        registerModule(pm, "LWC", lwc -> new LWCProtectionModule(lwc));
        registerModule(pm, "PreciousStones", preciousStones -> new PreciousStonesProtectionModule(preciousStones));
        registerModule(pm, "Lockette", lockette -> new LocketteProtectionModule(lockette));
        registerModule(pm, "RedProtect", redProtect -> new RedProtectProtectionModule(redProtect));
        registerModule(pm, "BentoBox", bentoBox -> new BentoBoxProtectionModule(bentoBox));
        registerModule(pm, "BlockLocker", blockLocker -> new BlockLockerProtectionModule(blockLocker));
        registerModule(pm, "Lands", lands -> new LandsProtectionModule(lands, plugin));
        registerModule(pm, "ChestProtect", chestProtect -> new ChestProtectProtectionModule(chestProtect));
        registerModule(pm, "Factions", factions -> new FactionsUUIDProtectionModule(factions));
        registerModule(pm, "FunnyGuilds", funnyGuilds -> new FunnyGuildsProtectionModule(funnyGuilds));
        registerModule(pm, "PlotSquared", plotSquared -> new PlotSquaredProtectionModule(plotSquared));
        registerModule(pm, "HuskTowns", huskTowns -> new HuskTownsProtectionModule(huskTowns));
        registerModule(pm, "ShopChest", shopChest -> new ShopChestProtectionModule(shopChest));

        /*
         * The following Plugins work by utilising one of the above listed
         * Plugins in the background.
         * We do not need a module for them, but let us make the server owner
         * aware that this compatibility exists.
         */
        if (pm.isPluginEnabled("ProtectionStones")) {
            printModuleLoaded("ProtectionStones");
        }

        if (pm.isPluginEnabled("uSkyblock")) {
            printModuleLoaded("uSkyblock");
        }
    }

    @ParametersAreNonnullByDefault
    private void loadLoggerImplementations(Plugin plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();

        if (pm.isPluginEnabled("CoreProtect")) {
            registerLogger(new CoreProtectLogger());
        }
        if (pm.isPluginEnabled("LogBlock")) {
            registerLogger(new LogBlockLogger());
        }
    }

    @ParametersAreNonnullByDefault
    public void registerLogger(String name, ProtectionLogger module) {
        protectionLoggers.add(module);
        printModuleLoaded(name);
    }

    @ParametersAreNonnullByDefault
    public void registerModule(PluginManager pm, String pluginName, Function<Plugin, ProtectionModule> constructor) {
        Plugin plugin = pm.getPlugin(pluginName);

        if (plugin != null && plugin.isEnabled()) {
            registerModule(plugin, constructor);
        }
    }

    @ParametersAreNonnullByDefault
    private void registerModule(Plugin plugin, Function<Plugin, ProtectionModule> constructor) {
        try {
            ProtectionModule module = constructor.apply(plugin);
            module.load();

            protectionModules.add(module);
            printModuleLoaded(module.getName() + " v" + module.getVersion());
        } catch (Throwable x) {
            logger.log(Level.SEVERE, x, () -> "An Error occured while registering the Protection Module: \"" + plugin.getName() + "\" v" + plugin.getDescription().getVersion());
        }
    }

    @ParametersAreNonnullByDefault
    public void registerLogger(ProtectionLogger module) {
        try {
            module.load();
            registerLogger(module.getName(), module);
        } catch (Throwable x) {
            logger.log(Level.SEVERE, x, () -> "An Error occured while registering the Protection Module: \"" + module.getName() + "\"");
        }
    }

    @ParametersAreNonnullByDefault
    private void printModuleLoaded(String module) {
        logger.log(Level.INFO, "Loaded Protection Module \"{0}\"", module);
    }

    @ParametersAreNonnullByDefault
    public boolean hasPermission(OfflinePlayer p, Block b, Interaction action) {
        return hasPermission(p, b.getLocation(), action);
    }

    @ParametersAreNonnullByDefault
    public boolean hasPermission(OfflinePlayer p, Location l, Interaction action) {
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

    @ParametersAreNonnullByDefault
    public void logAction(OfflinePlayer p, Block b, Interaction action) {
        for (ProtectionLogger module : protectionLoggers) {
            try {
                module.logAction(p, b, action);
            } catch (Exception | LinkageError x) {
                logger.log(Level.SEVERE, x, () -> "An Error occured while logging for the Protection Module: \"" + module.getName() + "\"");
            }
        }
    }

}
