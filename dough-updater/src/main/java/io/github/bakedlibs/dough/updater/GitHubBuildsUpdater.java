package io.github.bakedlibs.dough.updater;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.plugin.Plugin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.bakedlibs.dough.common.CommonPatterns;
import io.github.bakedlibs.dough.versions.PrefixedVersion;
import io.github.bakedlibs.dough.versions.Version;

public class GitHubBuildsUpdater extends AbstractPluginUpdater {

    private static final String API_URL = "https://thebusybiscuit.github.io/builds/";

    private final String repository;
    private final String prefix;

    public GitHubBuildsUpdater(@Nonnull Plugin plugin, @Nonnull File file, @Nonnull String repo) {
        this(plugin, file, repo, "DEV - ");
    }

    public GitHubBuildsUpdater(@Nonnull Plugin plugin, @Nonnull File file, @Nonnull String repo, @Nonnull String prefix) {
        super(plugin, file, extractBuild(prefix, plugin));

        this.repository = repo;
        this.prefix = prefix;
    }

    @ParametersAreNonnullByDefault
    private static @Nonnull Version extractBuild(String prefix, Plugin plugin) {
        String pluginVersion = plugin.getDescription().getVersion();

        if (pluginVersion.startsWith(prefix)) {
            int version = Integer.parseInt(pluginVersion.substring(prefix.length()).split(" ")[0], 10);
            return new PrefixedVersion(prefix, version);
        } else {
            throw new IllegalArgumentException("Not a valid build version: " + pluginVersion);
        }
    }

    @Override
    public void start() {
        try {
            URL versionsURL = new URL(API_URL + repository + "/builds.json");

            scheduleAsyncUpdateTask(new UpdaterTask(this, versionsURL) {

                @Override
                public UpdateInfo parse(String result) throws MalformedURLException {
                    JsonObject json = (JsonObject) new JsonParser().parse(result);

                    if (json == null) {
                        getLogger().log(Level.WARNING, "The Auto-Updater could not connect to github.io, is it down?");
                        return null;
                    }

                    int latestVersion = json.get("last_successful").getAsInt();
                    URL downloadURL = new URL(API_URL + repository + '/' + CommonPatterns.SLASH.split(repository)[1] + '-' + latestVersion + ".jar");

                    return new UpdateInfo(downloadURL, new PrefixedVersion(prefix, latestVersion));
                }

            });
        } catch (MalformedURLException e) {
            getLogger().log(Level.SEVERE, "Auto-Updater URL is malformed", e);
        }
    }
}
