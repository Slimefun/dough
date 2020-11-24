package io.github.thebusybiscuit.cscorelib2.updater;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class GitHubBuildsUpdater implements Updater {

    private static final String API_URL = "https://thebusybiscuit.github.io/builds/";

    @Getter
    private final Plugin plugin;

    @Getter
    private final File file;

    @Getter
    private final String repository;
    private final String prefix;

    @Getter
    private String localVersion;

    @Getter
    @Setter
    protected int timeout = 10000;

    public GitHubBuildsUpdater(@NonNull Plugin plugin, @NonNull File file, @NonNull String repo) {
        this(plugin, file, repo, "DEV - ");
    }

    public GitHubBuildsUpdater(@NonNull Plugin plugin, @NonNull File file, @NonNull String repo, @NonNull String prefix) {
        this.plugin = plugin;
        this.file = file;
        this.repository = repo;
        this.prefix = prefix;

        localVersion = extractBuild(plugin.getDescription().getVersion());

        prepareUpdateFolder();
    }

    private String extractBuild(String version) {
        if (version.startsWith(prefix)) {
            return version.substring(prefix.length()).split(" ")[0];
        }

        throw new IllegalArgumentException("Not a valid Development-Build Version: " + version);
    }

    @Override
    public void start() {
        try {
            URL versionsURL = new URL(API_URL + getRepository() + "/builds.json");

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                Thread thread = new Thread(new UpdaterTask(this, versionsURL) {

                    @Override
                    public boolean hasUpdate(String localVersion, String remoteVersion) {
                        return Integer.parseInt(remoteVersion) > Integer.parseInt(localVersion);
                    }

                    @Override
                    public UpdateInfo parse(String result) throws MalformedURLException {
                        JsonObject json = (JsonObject) new JsonParser().parse(result);

                        if (json == null) {
                            plugin.getLogger().log(Level.WARNING, "The Auto-Updater could not connect to github.io, is it down?");
                            return null;
                        }

                        String remoteVersion = String.valueOf(json.get("last_successful").getAsInt());
                        URL downloadURL = new URL(API_URL + getRepository() + "/" + getRepository().split("/")[1] + "-" + remoteVersion + ".jar");

                        return new UpdateInfo(downloadURL, remoteVersion);
                    }

                }, "Updater Thread");

                thread.start();
            });
        } catch (MalformedURLException e) {
            plugin.getLogger().log(Level.SEVERE, "Auto-Updater URL is malformed", e);
        }
    }
}
