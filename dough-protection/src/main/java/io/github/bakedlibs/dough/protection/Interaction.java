package io.github.bakedlibs.dough.protection;

import javax.annotation.Nonnull;

import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;

/**
 * This enum contains the different types of actions that a {@link ProtectionModule}
 * can protect against.
 * 
 * @author TheBusyBiscuit
 * 
 * @see ProtectionManager
 * @see ProtectionModule
 *
 */
public enum Interaction {

    /**
     * This represents a {@link Player} (posibly offline) trying to break a {@link Block}.
     */
    BREAK_BLOCK(ActionType.BLOCK),

    /**
     * This represents a {@link Player} (posibly offline) trying to place a {@link Block}.
     */
    PLACE_BLOCK(ActionType.BLOCK),

    /**
     * This represents a {@link Player} (posibly offline) trying to access an {@link Inventory}.
     */
    INTERACT_BLOCK(ActionType.BLOCK),

    /**
     * This represents a {@link Player} trying to attack another {@link Player}.
     */
    ATTACK_PLAYER(ActionType.ENTITY),

    /**
     * This represents a {@link Player} trying to attack an {@link Entity}, either an {@link Animals} or a
     * {@link Monster}.
     */
    ATTACK_ENTITY(ActionType.ENTITY),

    /**
     * This represents a {@link Player} trying to interact with an {@link Entity}.
     * This could be a {@link Sheep} that is getting dyed, or a {@link Villager} being traded with.
     */
    INTERACT_ENTITY(ActionType.ENTITY);

    private final ActionType type;

    /**
     * This constructs a new {@link Interaction}.
     * 
     * @param type
     *            The type of action that this should cover.
     */
    Interaction(@Nonnull ActionType type) {
        this.type = type;
    }

    /**
     * This checks whether this {@link Interaction} involves a {@link Block} or not.
     * 
     * @return Whether this {@link Interaction} involves a {@link Block}
     */
    public @Nonnull ActionType getType() {
        return type;
    }

}
