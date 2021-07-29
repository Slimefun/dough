package io.github.bakedlibs.dough.versions;

import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;

import io.github.bakedlibs.dough.common.CommonPatterns;

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

    /**
     * This creates a new {@link SemanticVersion} instance with the given components.
     * 
     * @param major
     *            The major version
     * @param minor
     *            The minor version
     * @param patch
     *            The patch version
     */
    public SemanticVersion(int major, int minor, int patch) {
        Validate.isTrue(major >= 0, "Major version must be positive or zero.");
        Validate.isTrue(minor >= 0, "Minor version must be positive or zero.");
        Validate.isTrue(patch >= 0, "Patch version must be positive or zero.");

        this.majorVersion = major;
        this.minorVersion = minor;
        this.patchVersion = patch;
    }

    /**
     * This creates a new {@link SemanticVersion} instance with the given components.
     * <p>
     * The patch version will be automatically assumed to be zero.
     * 
     * @param major
     *            The major version
     * @param minor
     *            The minor version
     */
    public SemanticVersion(int major, int minor) {
        this(major, minor, 0);
    }

    /**
     * This returns the "major" version component of this {@link SemanticVersion}.
     * 
     * @return The major version
     */
    public final int getMajorVersion() {
        return majorVersion;
    }

    /**
     * This returns the "minor" version component of this {@link SemanticVersion}.
     * 
     * @return The minor version
     */
    public final int getMinorVersion() {
        return minorVersion;
    }

    /**
     * This returns the "patch" version component of this {@link SemanticVersion}.
     * 
     * @return The patch version
     */
    public final int getPatchVersion() {
        return patchVersion;
    }

    /**
     * This method returns whether this is a patch version.
     * A {@link SemanticVersion} is considered a "patch" when their
     * last component ({@link #getPatchVersion()}) is greater than zero.
     * 
     * @return Whether this is a patch version
     */
    public final boolean isPatch() {
        return getPatchVersion() > 0;
    }

    public boolean isAtLeast(int major, int minor, int patch) {
        return isAtLeast(new SemanticVersion(major, minor, patch));
    }

    public boolean isAtLeast(int major, int minor) {
        return isAtLeast(major, minor, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSimilar(Version version) {
        return version instanceof SemanticVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNewerThan(@Nonnull Version version) {
        if (isSimilar(version)) {
            SemanticVersion semver = (SemanticVersion) version;
            int major = semver.getMajorVersion();

            if (getMajorVersion() > major) {
                return true;
            } else if (major > getMajorVersion()) {
                return false;
            }

            int minor = semver.getMinorVersion();

            if (getMinorVersion() > minor) {
                return true;
            } else if (minor > getMinorVersion()) {
                return false;
            }

            return getPatchVersion() > semver.getPatchVersion();
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
            SemanticVersion semver = (SemanticVersion) version;
            int major = semver.getMajorVersion();

            if (major != getMajorVersion()) {
                return false;
            }

            int minor = semver.getMinorVersion();

            if (minor != getMinorVersion()) {
                return false;
            }

            return getPatchVersion() == semver.getPatchVersion();
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
            SemanticVersion semver = (SemanticVersion) version;
            int major = semver.getMajorVersion();

            if (major > getMajorVersion()) {
                return true;
            } else if (getMajorVersion() > major) {
                return false;
            }

            int minor = semver.getMinorVersion();

            if (minor > getMinorVersion()) {
                return true;
            } else if (getMinorVersion() > minor) {
                return false;
            }

            return semver.getPatchVersion() > getPatchVersion();
        } else {
            throw new IncomparableVersionsException(this, version);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull String getAsString() {
        if (isPatch()) {
            return majorVersion + "." + minorVersion + "." + patchVersion;
        } else {
            return majorVersion + "." + minorVersion;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(majorVersion, minorVersion, patchVersion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SemanticVersion) {
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
        return "SemanticVersion [" + getAsString() + "]";
    }

    /**
     * This method returns whether this {@link SemanticVersion} equals the provided version
     * in all aspects except their patch number.
     * <p>
     * This will only compare the major and minor versions.
     * 
     * @param version
     *            The {@link SemanticVersion} to compare this to
     * 
     * @return Whether the two versions are equal (ignoring their patch version)
     */
    public boolean equalsIgnorePatch(@Nonnull SemanticVersion version) {
        Validate.notNull(version, "Version cannot be null.");
        return equalsIgnorePatch(version.getMajorVersion(), version.getMinorVersion());
    }

    /**
     * This method returns whether this {@link SemanticVersion} equals the provided version
     * numbers (ignoring the patch version of this {@link SemanticVersion}).
     * 
     * @param majorVersion
     *            The major version
     * @param minorVersion
     *            The minor version
     * 
     * @return Whether the two versions are equal (ignoring their patch version)
     */
    public boolean equalsIgnorePatch(int majorVersion, int minorVersion) {
        return this.majorVersion == majorVersion && this.minorVersion == minorVersion;
    }

    /**
     * This method attempts to parse the given {@link String} as a {@link SemanticVersion}.
     * If the {@link String} could not be parsed effectively, an {@link IllegalArgumentException}
     * will be thrown.
     * <p>
     * Note that the String should follow the {@link SemanticVersion} convention, such as
     * {@literal "1.0.2"}, {@literal "1.2.10"}, {@literal "1.3.0"} or {@literal "1.1"} to name
     * a few examples.
     * 
     * @param version
     *            The version string to parse
     * 
     * @return The resulting {@link SemanticVersion}
     */
    public static @Nonnull SemanticVersion parse(@Nonnull String version) {
        Validate.notNull(version, "The version should not be null.");

        // Create a Matcher from our semver regex
        Matcher matcher = CommonPatterns.SEMANTIC_VERSIONS.matcher(version);

        // Check if the Matcher has found a match
        if (matcher.matches()) {
            MatchResult result = matcher.toMatchResult();

            // Matcher groups start at 1. Group 0 is the "global" match.
            int majorVersion = Integer.parseInt(result.group(1), 10);
            int minorVersion = Integer.parseInt(result.group(2), 10);
            int patchVersion = 0;

            // Check if a patch version was provided.
            if (result.group(3) != null) {
                patchVersion = Integer.parseInt(result.group(3), 10);
            }

            return new SemanticVersion(majorVersion, minorVersion, patchVersion);
        } else {
            throw new IllegalArgumentException("Could not parse \"" + version + "\" as a semantic version.");
        }
    }

}
