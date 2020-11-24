package io.github.thebusybiscuit.cscorelib2.updater;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class BukkitUpdater implements Updater {

    private static final String API_URL = "https://api.curseforge.com/servermods/files?projectIds=";
    private static final String[] DEV_KEYWORDS = { "DEV", "EXPERIMENTAL", "BETA", "ALPHA", "UNFINISHED" };

    @Getter
    private final Plugin plugin;
    @Getter
    private final File file;
    private final int id;

    private Thread thread;

    @Getter
    private String localVersion;

    @Getter
    @Setter
    protected int timeout = 8000;

    public BukkitUpdater(@NonNull Plugin plugin, @NonNull File file, int id) {
        this.plugin = plugin;
        this.id = id;
        this.file = file;
        localVersion = plugin.getDescription().getVersion();
    }

    @Override
    public void start() {
        // Checking if current Version is a dev-build
        for (String dev : DEV_KEYWORDS) {
            if (localVersion.contains(dev)) {
                plugin.getLogger().log(Level.WARNING, " ");
                plugin.getLogger().log(Level.WARNING, "################## - DEVELOPMENT BUILD - ##################");
                plugin.getLogger().log(Level.WARNING, "You appear to be using an experimental build of {0}", plugin.getName());
                plugin.getLogger().log(Level.WARNING, "Version {0}", localVersion);
                plugin.getLogger().log(Level.WARNING, " ");
                plugin.getLogger().log(Level.WARNING, "Auto-Updates have been disabled. Use at your own risk!");
                plugin.getLogger().log(Level.WARNING, " ");
                return;
            }
        }

        localVersion = localVersion.toLowerCase();
        prepareUpdateFolder();

        try {
            URL url = new URL(API_URL + id);

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                thread = new Thread(new UpdaterTask(this, url) {

                    @Override
                    public boolean hasUpdate(String localVersion, String remoteVersion) {
                        if (localVersion.equals(remoteVersion)) {
                            return false;
                        }

                        String[] localSplit = localVersion.split("\\.");
                        String[] remoteSplit = remoteVersion.split("\\.");

                        for (int i = 0; i < remoteSplit.length; i++) {
                            if ((localSplit.length - 1) < i) {
                                return true;
                            }

                            if (Integer.parseInt(localSplit[i]) > Integer.parseInt(remoteSplit[i])) {
                                return false;
                            }

                            if (Integer.parseInt(remoteSplit[i]) > Integer.parseInt(localSplit[i])) {
                                return true;
                            }
                        }

                        return false;
                    }

                    @Override
                    public UpdateInfo parse(String result) throws MalformedURLException {
                        JsonArray array = (JsonArray) new JsonParser().parse(result);

                        if (array.size() == 0) {
                            plugin.getLogger().log(Level.WARNING, "The Auto-Updater could not connect to dev.bukkit.org, is it down?");
                            return null;
                        }

                        JsonObject latest = array.get(array.size() - 1).getAsJsonObject();

                        URL download = new URL(latest.get("downloadUrl").getAsString());
                        String remoteVersion = latest.getAsJsonObject().get("name").getAsString();
                        remoteVersion = remoteVersion.toLowerCase(Locale.ROOT);

                        return new UpdateInfo(download, remoteVersion);
                    }

                }, "Updater Thread");

                thread.start();
            });
        } catch (MalformedURLException e) {
            plugin.getLogger().log(Level.SEVERE, "Auto-Updater URL is malformed", e);
        }
    }

}
