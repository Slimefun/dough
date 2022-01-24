package io.github.bakedlibs.dough.protection;

/**
 * A very basic enum to denote whether an {@link Interaction} involved
 * blocks, entities or other objects.
 * 
 * @author TheBusyBiscuit
 *
 * @see Interaction
 */
public enum ActionType {

    /**
     * This constant represents an {@link Interaction} with blocks.
     */
    BLOCK,

    /**
     * This constant represents an {@link Interaction} with entities.
     */
    ENTITY;

}
