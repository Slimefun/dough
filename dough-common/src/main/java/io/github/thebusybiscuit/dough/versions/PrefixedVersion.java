package io.github.thebusybiscuit.dough.versions;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;

public class PrefixedVersion implements Version {

    private final String prefix;
    private final int version;

    public PrefixedVersion(@Nonnull String prefix, int version) {
        Validate.notNull(prefix, "The prefix cannot be null.");
        Validate.isTrue(version > 0, "The version must be a positive number.");

        this.prefix = prefix;
        this.version = version;
    }

    @Override
    public boolean isNewerThan(@Nonnull Version version) {
        // TODO Implement prefixed version logic
        return false;
    }

    @Override
    public boolean isEqualTo(@Nonnull Version version) {
        // TODO Implement prefixed version logic
        return false;
    }

    @Override
    public boolean isOlderThan(@Nonnull Version version) {
        // TODO Implement prefixed version logic
        return false;
    }

    @Override
    public @Nonnull String getAsString() {
        return prefix + version;
    }

    // TODO Unit tests

}
