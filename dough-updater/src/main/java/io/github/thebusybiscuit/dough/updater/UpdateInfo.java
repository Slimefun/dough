package io.github.thebusybiscuit.dough.updater;

import java.net.URL;

// TODO: Convert to Java 16 record
class UpdateInfo {

    private final URL url;
    private final String version;

    UpdateInfo(URL url, String version) {
        this.url = url;
        this.version = version;
    }

    URL getUrl() {
        return url;
    }

    String getVersion() {
        return version;
    }

}
