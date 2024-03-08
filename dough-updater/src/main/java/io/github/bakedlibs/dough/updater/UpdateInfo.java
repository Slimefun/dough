package io.github.bakedlibs.dough.updater;

import java.net.URL;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.bakedlibs.dough.versions.Version;

// TODO: Convert to Java 16 record
class UpdateInfo {

    private final URL url;
    private final Version version;
    private final String checksum;

    @ParametersAreNonnullByDefault
    UpdateInfo(URL url, Version version) {
        this(url, version, null);
    }

    UpdateInfo(URL url, Version version, @Nullable String checksum) {
        this.url = url;
        this.version = version;
        this.checksum = checksum;
    }

    URL getUrl() {
        return url;
    }

    Version getVersion() {
        return version;
    }

    String getChecksum() {
        return checksum;
    }
}
