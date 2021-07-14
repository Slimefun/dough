package io.github.bakedlibs.dough.inventory.builders;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;

import io.github.bakedlibs.dough.inventory.MenuLayout;
import io.github.bakedlibs.dough.inventory.SlotGroup;

public class MenuLayoutBuilder {

    protected final int size;
    protected final Set<SlotGroup> groups = new HashSet<>();
    protected String title;

    public MenuLayoutBuilder(int size) {
        Validate.isTrue(size > 0, "The size must be greater than 0.");
        Validate.isTrue(size % 9 == 0, "The size must be a multiple of 9.");
        Validate.isTrue(size <= 54, "The size must not be greater than 54.");

        this.size = size;
    }

    public @Nonnull MenuLayoutBuilder title(@Nullable String title) {
        this.title = title;
        return this;
    }

    @ParametersAreNonnullByDefault
    public @Nonnull MenuLayoutBuilder addSlotGroup(SlotGroup group) {
        groups.add(group);
        return this;
    }

    public @Nonnull MenuLayout build() {
        Validate.notEmpty(groups, "There are no SlotGroups defined.");

        return new MenuLayoutImpl(this);
    }

}
