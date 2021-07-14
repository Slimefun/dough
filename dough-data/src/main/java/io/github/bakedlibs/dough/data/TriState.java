package io.github.bakedlibs.dough.data;

/**
 * This enum represents the different states of a {@link TriStateOptional}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see TriStateOptional
 *
 */
enum TriState {

    /**
     * The {@link TriStateOptional} has not yet been computed.
     */
    NOT_COMPUTED,

    /**
     * The {@link TriStateOptional} has been computed but is empty.
     */
    EMPTY,

    /**
     * The {@link TriStateOptional} has been computed and is not empty.
     */
    COMPUTED;

}
