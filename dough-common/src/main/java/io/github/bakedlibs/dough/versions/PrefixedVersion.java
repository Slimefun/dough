package io.github.bakedlibs.dough.versions;

import java.util.Objects;

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

    public final @Nonnull String getPrefix() {
        return prefix;
    }

    public final @Nonnull int getNumericVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefix, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Version) {
            return isEqualTo((Version) obj);
        } else {
            return false;
        }
    }

    // TODO Unit tests

}
