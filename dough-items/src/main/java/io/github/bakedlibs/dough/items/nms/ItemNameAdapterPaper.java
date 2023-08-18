package io.github.bakedlibs.dough.items.nms;

import io.github.bakedlibs.dough.reflection.ReflectionUtils;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ItemNameAdapterPaper implements ItemNameAdapter {

    private final Method getName;

    ItemNameAdapterPaper() {
        super();

        getName = ReflectionUtils.getMethod(ItemStack.class, "getI18NDisplayName");
    }

    public static boolean canUse() {
        try {
            ReflectionUtils.getMethod(ItemStack.class, "getI18NDisplayName");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getName(ItemStack item) throws IllegalAccessException, InvocationTargetException {
        return (String) getName.invoke(item);
    }

}