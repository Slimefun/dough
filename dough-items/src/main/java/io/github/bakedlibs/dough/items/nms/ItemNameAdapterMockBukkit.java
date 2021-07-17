package io.github.bakedlibs.dough.items.nms;

import java.util.Locale;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

class ItemNameAdapterMockBukkit implements ItemNameAdapter {

    @Override
    @ParametersAreNonnullByDefault
    public String getName(ItemStack item) {
        return item.getType().name().toLowerCase(Locale.ROOT).replace('_', ' ');
    }

}
