package io.github.bakedlibs.dough.versions;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;

public class PrefixedVersion implements Version {

    private final String prefix;
    private final int numericVersion;

    public PrefixedVersion(@Nonnull String prefix, int version) {
        Validate.notNull(prefix, "The prefix cannot be null.");
        Validate.isTrue(version > 0, "The version must be a positive number.");

        this.prefix = prefix;
        this.numericVersion = version;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSimilar(@Nonnull Version version) {
        return version instanceof PrefixedVersion && prefix.equals(((PrefixedVersion) version).getPrefix());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNewerThan(@Nonnull Version version) {
        if (isSimilar(version)) {
            return getNumericVersion() > ((PrefixedVersion) version).getNumericVersion();
        } else {
            throw new IncomparableVersionsException(this, version);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEqualTo(@Nonnull Version version) {
        if (isSimilar(version)) {
            return getNumericVersion() == ((PrefixedVersion) version).getNumericVersion();
        } else {
            throw new IncomparableVersionsException(this, version);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOlderThan(@Nonnull Version version) {
        if (isSimilar(version)) {
            return getNumericVersion() < ((PrefixedVersion) version).getNumericVersion();
        } else {
            throw new IncomparableVersionsException(this, version);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull String getAsString() {
        return prefix + numericVersion;
    }

    public final @Nonnull String getPrefix() {
        return prefix;
    }

    public final @Nonnull int getNumericVersion() {
        return numericVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(prefix, numericVersion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Version && isSimilar((Version) obj)) {
            return isEqualTo((Version) obj);
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "PrefixedVersion [" + getAsString() + "]";
    }
}
