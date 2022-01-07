package io.github.bakedlibs.dough.updater;

import java.net.URL;

import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;

import io.github.bakedlibs.dough.versions.Version;


// TODO: Convert to Java 16 record
class UpdateInfo {

    private final URL url;
    private final Version version;

    @ParametersAreNonnullByDefault
    UpdateInfo(URL url, Version version) {
        Validate.notNull(url, "The URL cannot be null");
        Validate.notNull(version, "The version cannot be null");

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
