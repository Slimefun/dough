package io.github.thebusybiscuit.cscorelib2.updater;

import java.net.URL;

import lombok.Data;

@Data
class UpdateInfo {

    private final URL url;
    private final String version;

}
