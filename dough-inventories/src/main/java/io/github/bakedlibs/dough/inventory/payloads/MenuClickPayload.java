package io.github.bakedlibs.dough.inventory.payloads;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.inventory.Menu;
import io.github.bakedlibs.dough.inventory.SlotGroup;

public class MenuClickPayload extends AbstractMenuPayload {

    private final int slot;

    @ParametersAreNonnullByDefault
    MenuClickPayload(Menu inventory, Player player, int slot) {
        super(inventory, player);

        this.slot = slot;
    }

    public int getClickedSlot() {
        return slot;
    }

    public @Nonnull ItemStack getClickedItemStack() {
        return getMenu().getItem(getClickedSlot());
    }

    public @Nonnull SlotGroup getClickedSlotGroup() {
        return getMenu().getLayout().getGroup(getClickedSlot());
    }

}
