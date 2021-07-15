package io.github.bakedlibs.dough.inventory.builders;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;

import io.github.bakedlibs.dough.inventory.Menu;
import io.github.bakedlibs.dough.inventory.MenuLayout;
import io.github.bakedlibs.dough.inventory.SlotGroup;

/**
 * The {@link MenuLayoutBuilder} allows you to construct a {@link MenuLayout}
 * easily via the builder pattern.
 * 
 * @author TheBusyBiscuit
 *
 */
public class MenuLayoutBuilder {

    protected final int size;
    protected final Set<SlotGroup> groups = new HashSet<>();
    protected String title;

    /**
     * This creates a new {@link MenuLayoutBuilder} with the given inventory size.
     * 
     * @param size
     *            The inventory size for this {@link MenuLayout}
     */
    public MenuLayoutBuilder(int size) {
        Validate.isTrue(size > 0, "The size must be greater than 0.");
        Validate.isTrue(size % 9 == 0, "The size must be a multiple of 9.");
        Validate.isTrue(size <= 54, "The size must not be greater than 54.");

        this.size = size;
    }

    /**
     * This sets an optional title for the resulting {@link Menu}.
     * 
     * @param title
     *            The title or null
     * 
     * @return Our {@link MenuLayoutBuilder} instance
     */
    public @Nonnull MenuLayoutBuilder title(@Nullable String title) {
        this.title = title;
        return this;
    }

    /**
     * This adds the given {@link SlotGroup} to this {@link MenuLayout}.
     * 
     * @param group
     *            The {@link SlotGroup} to add
     * 
     * @return Our {@link MenuLayoutBuilder} instance
     */
    @ParametersAreNonnullByDefault
    public @Nonnull MenuLayoutBuilder addSlotGroup(SlotGroup group) {
        groups.add(group);
        return this;
    }

    /**
     * This creates the final {@link MenuLayout} object from this {@link MenuLayoutBuilder}.
     * 
     * @return The resulting {@link MenuLayout}
     */
    public @Nonnull MenuLayout build() {
        Validate.notEmpty(groups, "There are no SlotGroups defined.");

        return new MenuLayoutImpl(this);
    }

}
