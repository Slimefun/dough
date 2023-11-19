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

//TODO add a way to get the current version.
//TODO checksum checking.
public class BlobBuildUpdater extends AbstractPluginUpdater<PrefixedVersion> {
    private static final String SITE_URL = "https://blob.build";
    private static final String API_URL = SITE_URL + "/api/projects";

    private final String project;
    private final String releaseChannel;

    public BlobBuildUpdater(@Nonnull Plugin plugin, @Nonnull File file, @Nonnull String project) {
        this(plugin, file, project, "Dev");
    }

    public BlobBuildUpdater(@Nonnull Plugin plugin, @Nonnull File file, @Nonnull String project, @Nonnull String releaseChannel) {
        super(plugin, file, new PrefixedVersion(releaseChannel, 0));

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

                    int latestVersion = json.get("build_id").getAsInt();
                    URL downloadURL = new URI(json.get("file_download_url").getAsString()).toURL();

                    return new UpdateInfo(downloadURL, new PrefixedVersion(releaseChannel, latestVersion));
                }

            });
        } catch (MalformedURLException | URISyntaxException e ) {
            getLogger().log(Level.SEVERE, "Auto-Updater URL is malformed", e);
        }
    }
}
