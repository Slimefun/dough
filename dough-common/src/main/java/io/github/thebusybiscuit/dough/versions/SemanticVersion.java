package io.github.thebusybiscuit.dough.versions;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;

public class SemanticVersion implements Version {

    private final int majorVersion;
    private final int minorVersion;
    private final int patchVersion;

    public SemanticVersion(int major, int minor, int patch) {
        Validate.isTrue(major >= 0, "Major version must be positive or zero.");
        Validate.isTrue(minor >= 0, "Minor version must be positive or zero.");
        Validate.isTrue(patch >= 0, "Patch version must be positive or zero.");

        this.majorVersion = major;
        this.minorVersion = minor;
        this.patchVersion = patch;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public int getPatchVersion() {
        return patchVersion;
    }

    public boolean isPatch() {
        return getPatchVersion() > 0;
    }

    @Override
    public boolean isNewerThan(@Nonnull Version version) {
        // TODO Implement semver check
        return false;
    }

    @Override
    public boolean isEqualTo(@Nonnull Version version) {
        // TODO Implement semver check
        return false;
    }

    @Override
    public boolean isOlderThan(@Nonnull Version version) {
        // TODO Implement semver check
        return false;
    }

    @Override
    public @Nonnull String getAsString() {
        if (isPatch()) {
            return majorVersion + "." + minorVersion + "." + patchVersion;
        } else {
            return majorVersion + "." + minorVersion;
        }
    }

}
