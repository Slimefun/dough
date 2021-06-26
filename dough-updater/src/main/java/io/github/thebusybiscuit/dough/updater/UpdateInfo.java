package io.github.thebusybiscuit.dough.updater;

import java.net.URL;

import lombok.Data;

@Data
class UpdateInfo {

    private final URL url;
    private final String version;

}
