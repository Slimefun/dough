package io.github.bakedlibs.dough.versions;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;

/**
 * A {@link SemanticVersion} follows the semantic version convention.
 * The version itself consists of three components, a major version number,
 * a minor version number and a patch version number.
 * <p>
 * The components are read and compared in that exact order.
 * If the patch version number is zero, it may be omitted from the version string.
 * 
 * @author TheBusyBiscuit
 *
 */
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

    public final int getMajorVersion() {
        return majorVersion;
    }

    public final int getMinorVersion() {
        return minorVersion;
    }

    public final int getPatchVersion() {
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

    @Override
    public int hashCode() {
        return Objects.hash(majorVersion, minorVersion, patchVersion);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Version) {
            return isEqualTo((Version) obj);
        } else {
            return false;
        }
    }

    public static @Nonnull SemanticVersion parse(@Nonnull String version) {
        // TODO Implement version parser
        return null;
    }

    // TODO Unit tests

}
