package io.github.bakedlibs.dough.inventory.factory;

import java.io.File;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import be.seeseemelk.mockbukkit.MockBukkit;

public class MockMenuFactory extends MenuFactory {

    public MockMenuFactory() {
        // @formatter:off
        super(MockBukkit.loadWith(MockJavaPlugin.class, new PluginDescriptionFile(
            "MockPlugin",
            "1.0.0",
            MockJavaPlugin.class.getName())
        ));
        // @formatter:on
    }

    public static class MockJavaPlugin extends JavaPlugin {

        @ParametersAreNonnullByDefault
        public MockJavaPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
            super(loader, description, dataFolder, file);
        }

    }

}
