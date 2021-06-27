package io.github.thebusybiscuit.dough.tags;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.NamespacedKey;

/**
 * An {@link TagMisconfigurationException} is thrown whenever a {@link CustomMaterialTag}
 * contains illegal, invalid or unknown values.
 * 
 * @author TheBusyBiscuit
 *
 */
public class TagMisconfigurationException extends Exception {

    private static final long serialVersionUID = 5412127960821774280L;

    /**
     * This constructs a new {@link TagMisconfigurationException} for the given
     * {@link CustomMaterialTag}'s {@link NamespacedKey} with the provided context.
     * 
     * @param key
     *            The {@link NamespacedKey} of our {@link CustomMaterialTag}
     * @param message
     *            The message to display
     */
    @ParametersAreNonnullByDefault
    public TagMisconfigurationException(NamespacedKey key, String message) {
        super("Tag '" + key + "' has been misconfigured: " + message);
    }

    /**
     * This constructs a new {@link TagMisconfigurationException} for the given
     * {@link CustomMaterialTag}'s {@link NamespacedKey} with the provided context.
     * 
     * @param key
     *            The {@link NamespacedKey} of our {@link CustomMaterialTag}
     * @param cause
     *            The {@link Throwable} which has caused this to happen
     */
    @ParametersAreNonnullByDefault
    public TagMisconfigurationException(NamespacedKey key, Throwable cause) {
        super("Tag '" + key + "' has been misconfigured (" + cause.getMessage() + ')', cause);
    }

}
