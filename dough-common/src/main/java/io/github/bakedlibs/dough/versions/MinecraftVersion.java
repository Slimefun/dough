package io.github.bakedlibs.dough.versions;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import io.github.bakedlibs.dough.common.CommonPatterns;

/**
 * This is an extension of {@link SemanticVersion}, specifically designed
 * for Minecraft's versioning system.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SemanticVersion
 *
 */
public class MinecraftVersion extends SemanticVersion {

    /**
     * This method constructs a new {@link MinecraftVersion} with the given
     * version components.
     * 
     * @param major
     *            The "major" version (according to semver)
     * @param minor
     *            The "minor" version (according to semver)
     * @param patch
     *            The "patch" version (according to semver)
     */
    public MinecraftVersion(int major, int minor, int patch) {
        super(major, minor, patch);
    }

    /**
     * Private helper constructor for {@link #of(Server)}.
     * 
     * @param version
     *            The parsed {@link SemanticVersion}
     */
    private MinecraftVersion(@Nonnull SemanticVersion version) {
        this(version.getMajorVersion(), version.getMinorVersion(), version.getPatchVersion());
    }

    /**
     * This attempts to get the {@link MinecraftVersion} on which the given {@link Server}
     * is currently running on.
     * 
     * @param server
     *            The {@link Server} instance
     * 
     * @return The current {@link MinecraftVersion}
     * 
     * @throws UnknownServerVersionException
     *             This exception is thrown when the {@link Server} version could not be identified
     */
    public static @Nonnull MinecraftVersion of(@Nonnull Server server) throws UnknownServerVersionException {
        Validate.notNull(server, "Server should not be null!");
        String bukkitVersion = server.getBukkitVersion();

        try {
            // Strip away the later "-R0.1-SNAPSHOT" part
            String minecraftVersion = CommonPatterns.DASH.split(bukkitVersion)[0];

            // Parse this like any other semantic version
            return new MinecraftVersion(SemanticVersion.parse(minecraftVersion));
        } catch (Exception x) {
            // Something failed.
            throw new UnknownServerVersionException(bukkitVersion, x);
        }
    }

    /**
     * This attempts to get the {@link MinecraftVersion} on which the current {@link Server}
     * is running on.
     * 
     * @return The current {@link MinecraftVersion}
     * 
     * @throws UnknownServerVersionException
     *             This exception is thrown when the {@link Server} version could not be identified
     */
    public static @Nonnull MinecraftVersion get() throws UnknownServerVersionException {
        return of(Bukkit.getServer());
    }

    /**
     * This checks if the current Server instance is a mock (MockBukkit) and
     * whether we are in a Unit Test environment.
     * 
     * @param server
     *            The {@link Server} implementation to investigate
     * 
     * @return Whether the current Server instance is a mock
     */
    public static boolean isMocked(@Nonnull Server server) {
        /**
         * We start our search with the current Server class.
         */
        Class<?> clazz = server.getClass();

        /*
         * Loop through the entire class hierarchy.
         * As of MockBukkit-1.15 v0.30 MockBukkit allows you to
         * pass your custom ServerMock class into MockBukkit.mock().
         * 
         * That's why checking the Server implementation class alone isn't
         * enough, the custom class could be located anywhere and named anything.
         * So we start iterating through the entire class hierarchy to see
         * if we find our ServerMock class within this hierarchy.
         */
        while (clazz != null) {
            // We only check the last part here in case the package name changes.
            if (clazz.getName().endsWith("mockbukkit.ServerMock")) {
                // We found our MockBukkit class
                return true;
            } else {
                // Advance to the next node
                clazz = clazz.getSuperclass();
            }
        }

        // No MockBukkit classes present in the type hierarchy.
        return false;
    }

    /**
     * This checks if the current Server instance is a mock (MockBukkit) and
     * whether we are in a Unit Test environment.
     * 
     * @return Whether the current Server instance is a mock
     */
    public static boolean isMocked() {
        return isMocked(Bukkit.getServer());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nonnull String getAsString() {
        return "Minecraft " + super.getAsString();
    }

}
