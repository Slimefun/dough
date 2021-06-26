package io.github.thebusybiscuit.dough.updater;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public interface Updater {

    Plugin getPlugin();

    File getFile();

    String getLocalVersion();

    int getTimeout();

    void start();

    default void prepareUpdateFolder() {
        File dir = new File("plugins/" + Bukkit.getUpdateFolder());

        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

}
