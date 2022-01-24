package io.github.bakedlibs.dough.items.nms;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.reflection.ReflectionUtils;
import io.github.bakedlibs.dough.versions.UnknownServerVersionException;

class ItemNameAdapter18 implements ItemNameAdapter {

    private final Method getCopy;
    private final Method getName;
    private final Method toString;

    ItemNameAdapter18() throws NoSuchMethodException, SecurityException, ClassNotFoundException, UnknownServerVersionException {
        super();

        getCopy = ReflectionUtils.getOBCClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class);
        // Spigot has re-obf'd names however you can get mapped jar... so we do this
        getName = ReflectionUtils.getMethodOrAlternative(ReflectionUtils.getNetMinecraftClass("world.item.ItemStack"), "getDisplayName", "G");
        toString = ReflectionUtils.getMethod(ReflectionUtils.getNetMinecraftClass("network.chat.IChatBaseComponent"), "getString");
    }

    @Override
    @ParametersAreNonnullByDefault
    public String getName(ItemStack item) throws IllegalAccessException, InvocationTargetException {
        Object instance = getCopy.invoke(null, item);
        return (String) toString.invoke(getName.invoke(instance));
    }

}
