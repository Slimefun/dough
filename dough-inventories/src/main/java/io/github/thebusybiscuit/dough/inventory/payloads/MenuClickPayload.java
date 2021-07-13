package io.github.thebusybiscuit.dough.inventory.payloads;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.dough.inventory.Menu;
import io.github.thebusybiscuit.dough.inventory.SlotGroup;

public class MenuClickPayload extends AbstractMenuPayload {

    private final Player player;
    private final int slot;

    @ParametersAreNonnullByDefault
    MenuClickPayload(Menu inventory, Player player, int slot) {
        super(inventory);

        this.player = player;
        this.slot = slot;
    }

    public @Nonnull Player getPlayer() {
        return player;
    }

    public int getClickedSlot() {
        return slot;
    }

    public @Nonnull ItemStack getClickedItemStack() {
        return getInventory().getItem(getClickedSlot());
    }

    public @Nonnull SlotGroup getClickedSlotGroup() {
        return getInventory().getLayout().getGroup(getClickedSlot());
    }

}
