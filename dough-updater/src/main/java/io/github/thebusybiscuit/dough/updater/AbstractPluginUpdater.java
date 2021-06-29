package io.github.thebusybiscuit.dough.updater;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.dough.versions.Version;

abstract class AbstractPluginUpdater implements PluginUpdater {

    private final Plugin plugin;
    private final File file;

    private int connectionTimeout = 9000;

    protected final Version currentVersion;
    protected final CompletableFuture<Version> latestVersion = new CompletableFuture<>();

    @ParametersAreNonnullByDefault
    protected AbstractPluginUpdater(Plugin plugin, File file, Version currentVersion) {
        Validate.notNull(plugin, "The plugin cannot be null.");
        Validate.notNull(file, "The plugin file cannot be null.");
        Validate.notNull(currentVersion, "The current version cannot be null.");

        this.plugin = plugin;
        this.file = file;
        this.currentVersion = currentVersion;

        prepareUpdateFolder();
    }

    private void prepareUpdateFolder() {
        File dir = new File("plugins/" + Bukkit.getUpdateFolder());

        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int timeout) {
        Validate.isTrue(timeout > 0, "Timeout must be positive.");

        this.connectionTimeout = timeout;
    }

    public @Nonnull Plugin getPlugin() {
        return plugin;
    }

    public @Nonnull Logger getLogger() {
        return plugin.getLogger();
    }

    public @Nonnull File getFile() {
        return file;
    }

    @Override
    public @Nonnull Version getCurrentVersion() {
        return currentVersion;
    }

    public @Nonnull CompletableFuture<Version> getLatestVersion() {
        return latestVersion;
    }

    protected void scheduleAsyncUpdateTask(@Nonnull UpdaterTask task) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, task);
    }

}
