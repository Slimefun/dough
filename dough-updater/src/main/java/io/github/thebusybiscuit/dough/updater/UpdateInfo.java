package io.github.thebusybiscuit.dough.updater;

import java.net.URL;

import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.dough.versions.Version;

// TODO: Convert to Java 16 record
class UpdateInfo {

    private final URL url;
    private final Version version;

    @ParametersAreNonnullByDefault
    UpdateInfo(URL url, Version version) {
        this.url = url;
        this.version = version;
    }

    URL getUrl() {
        return url;
    }

    Version getVersion() {
        return version;
    }

}
