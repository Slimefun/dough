package io.github.bakedlibs.dough.updater;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.google.common.hash.Hashing;

import io.github.bakedlibs.dough.versions.Version;

abstract class UpdaterTask<V extends Version> implements Runnable {

    private final Plugin plugin;
    private final File file;
    private final URL url;
    private final int timeout;
    private final V currentVersion;

    UpdaterTask(@Nonnull PluginUpdater<V> updater, @Nonnull URL url) {
        this.plugin = updater.getPlugin();
        this.file = updater.getFile();
        this.url = url;
        this.timeout = updater.getConnectionTimeout();
        this.currentVersion = updater.getCurrentVersion();
    }

    @Nullable
    public abstract UpdateInfo parse(String result) throws MalformedURLException, URISyntaxException;

    @Override
    public void run() {
        try {
            UpdateInfo latestVersion = getLatestVersion(url);

            if (latestVersion != null) {
                validateAndInstall(latestVersion);
            }
        } catch (NumberFormatException x) {
            plugin.getLogger().log(Level.SEVERE, "Could not auto-update {0}", plugin.getName());
            plugin.getLogger().log(Level.SEVERE, "Unrecognized Version: {0}", currentVersion);
        }
    }

    @Nullable
    private UpdateInfo getLatestVersion(@Nonnull URL url) {
        try {
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(timeout);
            connection.addRequestProperty("User-Agent", "Auto Updater (by TheBusyBiscuit)");
            connection.setDoOutput(true);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                return parse(reader.readLine());
            }
        } catch (IOException | URISyntaxException e) {
            plugin.getLogger().log(Level.WARNING, "Could not connect to the updating site, is it down?", e);
            return null;
        }
    }

    private void validateAndInstall(@Nonnull UpdateInfo updateInfo) {
        if (updateInfo.getVersion().isNewerThan(currentVersion)) {
            install(updateInfo);
        } else {
            plugin.getLogger().log(Level.INFO, "{0} is already up to date!", plugin.getName());
        }
    }

    private void install(@Nonnull UpdateInfo info) {
        plugin.getLogger().log(Level.INFO, "{0} is outdated!", plugin.getName());
        plugin.getLogger().log(Level.INFO, "Downloading {0}, version: {1}", new Object[] { plugin.getName(), info.getVersion() });

        File outputFile = new File("plugins/" + Bukkit.getUpdateFolder(), file.getName());
        try (BufferedInputStream input = new BufferedInputStream(info.getUrl().openStream()); FileOutputStream output = new FileOutputStream(outputFile)) {
            byte[] data = new byte[1024];
            int read;

            while ((read = input.read(data, 0, 1024)) != -1) {
                output.write(data, 0, read);
            }
        } catch (Exception x) {
            plugin.getLogger().log(Level.SEVERE, x, () -> "Failed to auto-update " + plugin.getName());
        } finally {
            try {
                // Check the checksum
                byte[] fileBytes = Files.readAllBytes(outputFile.toPath());
                String checksum = Hashing.sha256().hashBytes(fileBytes).toString();

                if (!checksum.equals(info.getChecksum())) {
                    plugin.getLogger().log(Level.SEVERE, "The downloaded file is corrupted or was tampered with.");
                    return;
                }
            } catch(Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to verify checksum", e);
                return;
            }

            plugin.getLogger().log(Level.INFO, " ");
            plugin.getLogger().log(Level.INFO, "#################### - UPDATE - ####################");
            plugin.getLogger().log(Level.INFO, "{0} was successfully updated ({1} -> {2})", new Object[] { plugin.getName(), currentVersion, info.getVersion() });
            plugin.getLogger().log(Level.INFO, "Please restart your Server in order to use the new Version");
            plugin.getLogger().log(Level.INFO, " ");
        }
    }
}