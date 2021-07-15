package io.github.bakedlibs.dough.updater;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import org.bukkit.plugin.Plugin;

import io.github.bakedlibs.dough.versions.Version;

public interface PluginUpdater<V extends Version> {

    @Nonnull
    Plugin getPlugin();

    @Nonnull
    File getFile();

    @Nonnull
    V getCurrentVersion();

    @Nonnull
    CompletableFuture<V> getLatestVersion();

    int getConnectionTimeout();

    void start();

}
