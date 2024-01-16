package io.github.bakedlibs.dough.updater;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.bakedlibs.dough.versions.PrefixedVersion;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;

public class BlobBuildUpdater extends AbstractPluginUpdater<PrefixedVersion> {

    private static final String SITE_URL = "https://blob.build";
    private static final String API_URL = SITE_URL + "/api/builds";

    private final String project;
    private final String releaseChannel;

    public BlobBuildUpdater(@Nonnull Plugin plugin, @Nonnull File file, @Nonnull String project) {
        this(plugin, file, project, "Dev");
    }

    public BlobBuildUpdater(@Nonnull Plugin plugin, @Nonnull File file, @Nonnull String project, @Nonnull String releaseChannel) {
        super(plugin, file, extractBuild(releaseChannel + " - ", plugin));

        this.project = project;
        this.releaseChannel = releaseChannel;
    }

    @Override
    public void start() {
        try {
            URL versionsURL = new URI(API_URL + "/" + project + "/" + releaseChannel + "/latest").toURL();

            scheduleAsyncUpdateTask(new UpdaterTask<PrefixedVersion>(this, versionsURL) {

                @Override
                public UpdateInfo parse(String result) throws MalformedURLException, URISyntaxException {
                    JsonObject json = (JsonObject) JsonParser.parseString(result);

                    if (json == null) {
                        getLogger().log(Level.WARNING, "The Auto-Updater could not connect to Blob.build, is it down?");
                        return null;
                    }

                    JsonObject data = json.getAsJsonObject("data");
                    int latestVersion = data.get("buildId").getAsInt();
                    URL downloadURL = new URI(data.get("fileDownloadUrl").getAsString()).toURL();
                    String checksum = data.get("checksum").getAsString();
                    PrefixedVersion latest = new PrefixedVersion(releaseChannel + " - ", latestVersion);
                    getLatestVersion().complete(latest);

                    return new UpdateInfo(downloadURL, latest, checksum);
                }
            });
        } catch (MalformedURLException | URISyntaxException e ) {
            getLogger().log(Level.SEVERE, "Auto-Updater URL is malformed", e);
        }
    }
}
