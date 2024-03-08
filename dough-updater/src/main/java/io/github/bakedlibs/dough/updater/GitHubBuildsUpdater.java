package io.github.bakedlibs.dough.updater;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.plugin.Plugin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.bakedlibs.dough.common.CommonPatterns;
import io.github.bakedlibs.dough.versions.PrefixedVersion;

public class GitHubBuildsUpdater extends AbstractPluginUpdater<PrefixedVersion> {

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

    @Override
    public void start() {
        try {
            URL versionsURL = new URL(API_URL + repository + "/builds.json");

            scheduleAsyncUpdateTask(new UpdaterTask<PrefixedVersion>(this, versionsURL) {

                @Override
                public UpdateInfo parse(String result) throws MalformedURLException {
                    JsonObject json = (JsonObject) new JsonParser().parse(result);

                    if (json == null) {
                        getLogger().log(Level.WARNING, "The Auto-Updater could not connect to github.io, is it down?");
                        return null;
                    }

                    int latestVersion = json.get("last_successful").getAsInt();
                    URL downloadURL = new URL(API_URL + repository + '/' + CommonPatterns.SLASH.split(repository)[1] + '-' + latestVersion + ".jar");
                    PrefixedVersion latest = new PrefixedVersion(prefix, latestVersion);
                    getLatestVersion().complete(latest);

                    return new UpdateInfo(downloadURL, latest);
                }

            });
        } catch (MalformedURLException e) {
            getLogger().log(Level.SEVERE, "Auto-Updater URL is malformed", e);
        }
    }
}
