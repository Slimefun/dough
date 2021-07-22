package io.github.bakedlibs.dough.versions;

import javax.annotation.Nonnull;

/**
 * This {@link Version} implementation consists of only one component, the version number.
 * 
 * @author TheBusyBiscuit
 *
 */
public class SimpleNumericVersion extends AbstractNumericVersion {

    /**
     * This constructs a new {@link SimpleNumericVersion} with the given version number.
     * The version number cannot be negative.
     * 
     * @param version
     *            The version number
     */
    public SimpleNumericVersion(int version) {
        super(version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSimilar(@Nonnull Version version) {
        return version instanceof SimpleNumericVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull String getAsString() {
        return String.valueOf(getVersionNumber());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getVersionNumber();
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
        return "NumericVersion [" + getAsString() + "]";
    }
}
