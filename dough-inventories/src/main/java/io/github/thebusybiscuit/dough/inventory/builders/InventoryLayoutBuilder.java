package io.github.thebusybiscuit.dough.inventory.builders;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;

import io.github.thebusybiscuit.dough.inventory.InventoryLayout;
import io.github.thebusybiscuit.dough.inventory.SlotGroup;

public class InventoryLayoutBuilder {

    protected final int size;
    protected final Set<SlotGroup> groups = new HashSet<>();
    protected String title;

    public InventoryLayoutBuilder(int size) {
        Validate.isTrue(size > 0, "The size must be greater than 0.");
        Validate.isTrue(size % 9 == 0, "The size must be a multiple of 9.");
        Validate.isTrue(size <= 54, "The size must not be greater than 54.");

        this.size = size;
    }

    public @Nonnull InventoryLayoutBuilder title(@Nullable String title) {
        this.title = title;
        return this;
    }

    @ParametersAreNonnullByDefault
    public @Nonnull InventoryLayoutBuilder addSlotGroup(SlotGroup group) {
        groups.add(group);
        return this;
    }

    public @Nonnull InventoryLayout build() {
        Validate.notEmpty(groups, "There are no SlotGroups defined.");

        return new InventoryLayoutBuilderResult(this);
    }

}
