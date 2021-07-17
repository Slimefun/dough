package io.github.bakedlibs.dough.versions;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;

/**
 * Abstract super class for numeric versions.
 * 
 * @author TheBusyBiscuit
 *
 */
abstract class AbstractNumericVersion implements Version {

    /**
     * The version number.
     */
    private final int versionNumber;

    /**
     * This constructs a new {@link SimpleNumericVersion} with the given version number.
     * The version number cannot be negative.
     * 
     * @param version
     *            The version number
     */
    AbstractNumericVersion(int version) {
        Validate.isTrue(version > 0, "The version must be a positive number.");

        this.versionNumber = version;
    }

    /**
     * This method returns the version number which this numeric {@link Version} represents.
     * 
     * @return The version number
     */
    public final int getVersionNumber() {
        return versionNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSimilar(Version version) {
        // This SHOULD be overridden.
        return version instanceof AbstractNumericVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNewerThan(@Nonnull Version version) {
        if (isSimilar(version)) {
            return getVersionNumber() > ((AbstractNumericVersion) version).getVersionNumber();
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
            return getVersionNumber() == ((AbstractNumericVersion) version).getVersionNumber();
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
            return getVersionNumber() < ((AbstractNumericVersion) version).getVersionNumber();
        } else {
            throw new IncomparableVersionsException(this, version);
        }
    }

}
