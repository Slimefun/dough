package io.github.bakedlibs.dough.versions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An {@link IncomparableVersionsException} is thrown when two objects of the type {@link Version}
 * could not be compared.
 * <p>
 * In general: Two versions of different formats are not able to be compared.
 * A {@link SemanticVersion} like {@code 1.5.2} for example cannot be compared to
 * a {@link Version} like {@code nightly-2021-10-25} as there is no definitive criteria to
 * mediate between the two formats.
 * 
 * @author TheBusyBiscuit
 *
 */
public class IncomparableVersionsException extends RuntimeException {

    private static final long serialVersionUID = -4276437450741965941L;

    /**
     * This constructs a new {@link IncomparableVersionsException} for the given {@link Version}s.
     * 
     * @param version1
     *            {@link Version} 1
     * @param version2
     *            {@link Version} 2
     */
    IncomparableVersionsException(@Nonnull Version version1, @Nullable Version version2) {
        super("Unable to compare " + toString(version1) + " with " + toString(version2));
    }

    /**
     * Utility method to format a {@link Version} to be as verbose as possible.
     * 
     * @param version
     *            The {@link Version} to format
     * 
     * @return A verbose {@link String} representation of this {@link Version}
     */
    private static @Nonnull String toString(@Nullable Version version) {
        if (version == null) {
            return "<null>";
        } else {
            return version.getClass().getSimpleName() + " (" + version.getAsString() + ')';
        }
    }

}
