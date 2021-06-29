package io.github.thebusybiscuit.dough.versions;

import javax.annotation.Nonnull;

public interface Version {

    boolean isNewerThan(@Nonnull Version version);

    boolean isEqualTo(@Nonnull Version version);

    boolean isOlderThan(@Nonnull Version version);

    default boolean isAtLeast(@Nonnull Version version) {
        return isEqualTo(version) || isNewerThan(version);
    }

    @Nonnull
    String getAsString();

}
