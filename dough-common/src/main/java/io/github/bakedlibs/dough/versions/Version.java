package io.github.bakedlibs.dough.versions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;

/**
 * A {@link Version} is a unique name or number that describes the condition of
 * an object. Versions can be compared with each other and a {@link Version} is either
 * equal to, newer than or older than another {@link Version}.
 * <p>
 * There are various different formats to describe a {@link Version}, the most popular
 * format being {@link SemanticVersion} {@code (e.g. 1.4.2)}.
 * <p>
 * This interface describes the base characteristics of what a {@link Version} is and
 * means to compare them.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Versioned
 *
 */
public interface Version extends Comparable<Version> {

    /**
     * This method returns whether this {@link Version} is newer than the
     * provided {@link Version}. This also implies that the provided
     * {@link Version} is older than this {@link Version}, see {@link #isOlderThan(Version)}.
     * <p>
     * <strong>This method may throw an {@link IncomparableVersionsException} if
     * the two versions cannot be compared with each other.</strong>
     * 
     * @param version
     *            The {@link Version} to compare this to
     * 
     * @return Whether this {@link Version} is newer than the provided one.
     */
    boolean isNewerThan(@Nonnull Version version);

    /**
     * This method returns whether this {@link Version} is equal to the given
     * {@link Version}.
     * <p>
     * <strong>This method may throw an {@link IncomparableVersionsException} if
     * the two versions cannot be compared with each other.</strong>
     * 
     * @param version
     *            The {@link Version} to compare this to
     * 
     * @return Whether the given {@link Version} is equal to this {@link Version}.
     */
    boolean isEqualTo(@Nonnull Version version);

    /**
     * This method returns whether this {@link Version} is older than the
     * provided {@link Version}. This also implies that the provided
     * {@link Version} is newer than this {@link Version}, see {@link #isNewerThan(Version)}.
     * <p>
     * <strong>This method may throw an {@link IncomparableVersionsException} if
     * the two versions cannot be compared with each other.</strong>
     * 
     * @param version
     *            The {@link Version} to compare this to
     * 
     * @return Whether this {@link Version} is older than the provided one.
     */
    boolean isOlderThan(@Nonnull Version version);

    /**
     * This method returns whether this {@link Version} is equal to or newer
     * than the provided {@link Version}.
     * <p>
     * <strong>This method may throw an {@link IncomparableVersionsException} if
     * the two versions cannot be compared with each other.</strong>
     * 
     * @param version
     *            The {@link Version} to compare this to
     * 
     * @return This returns whether this is equal to or newer than the given {@link Version}
     */
    default boolean isAtLeast(@Nonnull Version version) {
        Validate.notNull(version, "The version to compare must not be null.");

        return isEqualTo(version) || isNewerThan(version);
    }

    /**
     * This returns whether this {@link Version} is "similar" to the given {@link Version}.
     * Two {@link Version}s are considered "similar" when they can be compared to each other
     * without causing an {@link IncomparableVersionsException}.
     * 
     * @param version
     *            The {@link Version} to check
     * 
     * @return Whether the two {@link Version}s can be compared.
     */
    boolean isSimilar(@Nonnull Version version);

    /**
     * This method returns this {@link Version} as a human-readable format.
     * Example: {@code 1.4.2}.
     * 
     * @return A human-readable {@link String} representation of this {@link Version}
     */
    @Nonnull
    String getAsString();

    /**
     * {@inheritDoc}
     */
    @Override
    default int compareTo(@Nullable Version version) {
        if (version == null) {
            // Following Java convention x.compareTo(null) throws a NullPointerException
            throw new NullPointerException("Cannot compare a version that is null");
        }

        if (this.isEqualTo(version)) {
            return 0;
        } else if (this.isNewerThan(version)) {
            return 1;
        } else if (this.isOlderThan(version)) {
            return -1;
        } else {
            /*
             * (should theoretically never be reached)
             * The two versions cannot be compared.
             */
            throw new IncomparableVersionsException(this, version);
        }
    }

}
