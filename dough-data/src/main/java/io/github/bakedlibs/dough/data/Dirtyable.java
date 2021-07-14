package io.github.bakedlibs.dough.data;

/**
 * This interface can be implemented to mark an object as dirty-able.
 * An object which is considered "dirty" needs to be saved.
 * 
 * @author TheBusyBiscuit
 *
 */
public interface Dirtyable {

    /**
     * This method sets the dirty state.
     * <p>
     * An object is considered "dirty" if the state of the object
     * has been modified since the last time the object has been saved.
     * A dirty object is an object which needs to be saved again.
     * Once the object was saved, it can be marked as non-dirty again.
     * 
     * @param dirty
     *            Whether this object should be dirty or not
     */
    void markDirty(boolean dirty);

    /**
     * This method reads the dirty state.
     * It will return true if the object is currently dirty and false if
     * the object is not dirty.
     * 
     * @return Whether this object is dirty
     */
    boolean isDirty();

}
