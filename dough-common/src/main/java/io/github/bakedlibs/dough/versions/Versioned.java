package io.github.bakedlibs.dough.versions;

import javax.annotation.Nonnull;

/**
 * This interface marks something as "versioned" which means that it has a {@link Version}
 * that may change over time. Either at runtime or inbetween runs.
 * 
 * @author TheBusyBiscuit
 *
 */
@FunctionalInterface
public interface Versioned {

    /**
     * This returns the current {@link Version} of this object.
     * 
     * @return The {@link Version} of this object
     */
    @Nonnull
    Version getVersion();

}
