package io.github.bakedlibs.dough.versions;

import javax.annotation.Nonnull;

public class MockVersioned implements Versioned {

    private final SemanticVersion version;

    public MockVersioned(@Nonnull String version) {
        this.version = SemanticVersion.parse(version);
    }

    @Override
    public @Nonnull Version getVersion() {
        return version;
    }

}
