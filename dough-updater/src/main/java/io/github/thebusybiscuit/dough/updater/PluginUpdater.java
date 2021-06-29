package io.github.thebusybiscuit.dough.updater;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.dough.versions.Version;

public interface PluginUpdater {

    @Nonnull
    Plugin getPlugin();

    @Nonnull
    File getFile();

    @Nonnull
    Version getCurrentVersion();

    @Nonnull
    CompletableFuture<Version> getLatestVersion();

    int getTimeout();

    void start();

    default void prepareUpdateFolder() {
        File dir = new File("plugins/" + Bukkit.getUpdateFolder());

        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

}
