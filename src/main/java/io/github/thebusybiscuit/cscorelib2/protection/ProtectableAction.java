package io.github.thebusybiscuit.cscorelib2.protection;

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
public enum ProtectableAction {

    /**
     * This represents a {@link Player} (posibly offline) trying to break a {@link Block}.
     */
    BREAK_BLOCK(true),

    /**
     * This represents a {@link Player} (posibly offline) trying to place a {@link Block}.
     */
    PLACE_BLOCK(true),

    /**
     * This represents a {@link Player} (posibly offline) trying to access an {@link Inventory}.
     */
    INTERACT_BLOCK(true),

    /**
     * This represents a {@link Player} trying to attack another {@link Player}.
     */
    ATTACK_PLAYER(false),

    /**
     * This represents a {@link Player} trying to attack an {@link Entity}, either an {@link Animals} or a
     * {@link Monster}.
     */
    ATTACK_ENTITY(false),

    /**
     * This represents a {@link Player} trying to interact with an {@link Entity}.
     * This could be a {@link Sheep} that is getting dyed, or a {@link Villager} being traded with.
     */
    INTERACT_ENTITY(false);

    private boolean hasBlock;

    private ProtectableAction(boolean hasBlock) {
        this.hasBlock = hasBlock;
    }

    public boolean isBlockAction() {
        return hasBlock;
    }

}
