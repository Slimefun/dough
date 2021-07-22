package io.github.bakedlibs.dough.items.nms;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.reflection.ReflectionUtils;
import io.github.bakedlibs.dough.versions.UnknownServerVersionException;

class ItemNameAdapterBefore17 implements ItemNameAdapter {

    private final Method getCopy;
    private final Method getName;
    private final Method toString;

    ItemNameAdapterBefore17() throws NoSuchMethodException, SecurityException, ClassNotFoundException, UnknownServerVersionException {
        super();

        getCopy = ReflectionUtils.getOBCClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class);
        getName = ReflectionUtils.getMethod(ReflectionUtils.getNMSClass("ItemStack"), "getName");
        toString = ReflectionUtils.getMethod(ReflectionUtils.getNMSClass("IChatBaseComponent"), "getString");
    }

    @Override
    @ParametersAreNonnullByDefault
    public String getName(ItemStack item) throws IllegalAccessException, InvocationTargetException {
        Object instance = getCopy.invoke(null, item);
        return (String) toString.invoke(getName.invoke(instance));
    }

}
