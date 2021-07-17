package io.github.bakedlibs.dough.versions;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;

/**
 * This {@link Version} implementation consists of two components.
 * A prefix, e.g. "DEV #" or "pre-" and a numeric version component.
 * Only {@link PrefixedVersion}s with the same prefix can be compared.
 * 
 * @author TheBusyBiscuit
 *
 */
public class PrefixedVersion extends AbstractNumericVersion {

    /**
     * The prefix of this {@link PrefixedVersion}.
     */
    private final String prefix;

    /**
     * This constructs a new {@link PrefixedVersion} with the given prefix and version number.
     * The version number cannot be negative.
     * 
     * @param prefix
     *            The prefix
     * @param version
     *            The version number
     */
    public PrefixedVersion(@Nonnull String prefix, int version) {
        super(version);

        Validate.notNull(prefix, "The prefix cannot be null.");

        this.prefix = prefix;
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
    public @Nonnull String getAsString() {
        return prefix + getVersionNumber();
    }

    /**
     * This returns the prefix for this {@link PrefixedVersion}.
     * 
     * @return The prefix
     */
    public final @Nonnull String getPrefix() {
        return prefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(prefix, getVersionNumber());
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
