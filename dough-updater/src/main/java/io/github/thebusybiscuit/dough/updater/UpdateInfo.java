package io.github.thebusybiscuit.dough.updater;

import java.net.URL;

import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.dough.versions.Version;

@ParametersAreNonnullByDefault
record UpdateInfo(URL url, Version version) {

}
