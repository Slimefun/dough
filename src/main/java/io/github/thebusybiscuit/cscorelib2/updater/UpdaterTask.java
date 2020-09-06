package io.github.thebusybiscuit.cscorelib2.updater;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import lombok.Cleanup;
import lombok.NonNull;

abstract class UpdaterTask implements Runnable {

    private final Plugin plugin;
    private final File file;
    private final URL url;
    private final int timeout;
    private final String localVersion;

    UpdaterTask(@NonNull Updater updater, @NonNull URL url) {
        this.plugin = updater.getPlugin();
        this.file = updater.getFile();
        this.url = url;
        this.timeout = updater.getTimeout();
        this.localVersion = updater.getLocalVersion();
    }

    @Nullable
    public abstract UpdateInfo parse(String result) throws MalformedURLException;

    public abstract boolean hasUpdate(String localVersion, String remoteVersion);

    @Override
    public void run() {
        try {
            UpdateInfo latestVersion = getLatestVersion(url);

            if (latestVersion != null) {
                validateAndInstall(latestVersion);
            }
        }
        catch (NumberFormatException x) {
            plugin.getLogger().log(Level.SEVERE, "Could not auto-update {0}", plugin.getName());
            plugin.getLogger().log(Level.SEVERE, "Unrecognized Version: {0}", localVersion);
        }
    }

    @Nullable
    private UpdateInfo getLatestVersion(@NonNull URL url) {
        try {
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(timeout);
            connection.addRequestProperty("User-Agent", "Auto Updater (by TheBusyBiscuit)");
            connection.setDoOutput(true);

            @Cleanup
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            return parse(reader.readLine());
        }
        catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Could not connect to the updating site, is it down?", e);
            join();

            return null;
        }
    }

    private void validateAndInstall(@NonNull UpdateInfo latestVersion) {
        if (hasUpdate(localVersion, latestVersion.getVersion())) {
            install(latestVersion);
        }
        else {
            plugin.getLogger().log(Level.INFO, "{0} is already up to date!", plugin.getName());
            join();
        }
    }

    private void install(@NonNull UpdateInfo info) {
        plugin.getLogger().log(Level.INFO, "{0} is outdated!", plugin.getName());
        plugin.getLogger().log(Level.INFO, "Downloading {0}, version: {1}", new Object[] { plugin.getName(), info.getVersion() });

        try (BufferedInputStream input = new BufferedInputStream(info.getUrl().openStream()); FileOutputStream output = new FileOutputStream(new File("plugins/" + Bukkit.getUpdateFolder(), file.getName()))) {
            byte[] data = new byte[1024];
            int read;

            while ((read = input.read(data, 0, 1024)) != -1) {
                output.write(data, 0, read);
            }
        }
        catch (Exception x) {
            plugin.getLogger().log(Level.SEVERE, x, () -> "Failed to auto-update " + plugin.getName());
        }
        finally {
            plugin.getLogger().log(Level.INFO, " ");
            plugin.getLogger().log(Level.INFO, "#################### - UPDATE - ####################");
            plugin.getLogger().log(Level.INFO, "{0} was successfully updated ({1} -> {2})", new Object[] { plugin.getName(), localVersion, info.getVersion() });
            plugin.getLogger().log(Level.INFO, "Please restart your Server in order to use the new Version");
            plugin.getLogger().log(Level.INFO, " ");

            join();
        }
    }

    private void join() {
        try {
            Thread.currentThread().join();
        }
        catch (InterruptedException x) {
            plugin.getLogger().log(Level.SEVERE, "The Auto-Updater Thread was interrupted", x);
            Thread.currentThread().interrupt();
        }
    }
}