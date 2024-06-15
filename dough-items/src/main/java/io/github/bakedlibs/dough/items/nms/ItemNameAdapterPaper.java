package io.github.bakedlibs.dough.items.nms;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.InvocationTargetException;

public class ItemNameAdapterPaper implements ItemNameAdapter {
    @Override
    @ParametersAreNonnullByDefault
    public String getName(ItemStack item) throws IllegalAccessException, InvocationTargetException {
        return PlainTextComponentSerializer.plainText().serialize(Bukkit.getItemFactory().displayName(item));
    }
}
