package io.github.thebusybiscuit.dough.inventory.factory;

import java.util.function.BiFunction;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.dough.inventory.Menu;
import io.github.thebusybiscuit.dough.inventory.MenuLayout;
import io.github.thebusybiscuit.dough.inventory.SlotGroup;

public class MenuFactory {

    private final Plugin plugin;

    public MenuFactory(@Nonnull Plugin plugin) {
        this.plugin = plugin;
        registerListener(plugin);
    }

    private @Nonnull MenuListener registerListener(@Nonnull Plugin plugin) {
        MenuListener listener = new MenuListener(this);
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        return listener;
    }

    public final @Nonnull Plugin getPlugin() {
        return plugin;
    }

    @OverridingMethodsMustInvokeSuper
    public @Nonnull Menu createMenu(@Nonnull MenuLayout layout) {
        return createMenu(layout, CustomMenu::new);
    }

    @ParametersAreNonnullByDefault
    public @Nonnull <T extends CustomMenu> T createMenu(MenuLayout layout, BiFunction<MenuFactory, MenuLayout, T> constructor) {
        Validate.notNull(layout, "The menu layout cannot be null!");
        Validate.notNull(constructor, "The provided constructor is not allowed to be null!");

        T menu = constructor.apply(this, layout);
        String title = layout.getTitle();
        Inventory inv;

        if (title == null) {
            inv = Bukkit.createInventory(menu, layout.getSize());
        } else {
            inv = Bukkit.createInventory(menu, layout.getSize(), title);
        }

        menu.setInventory(inv);

        // Set all default items
        for (SlotGroup group : layout.getSlotGroups()) {
            menu.setAll(group, group.getDefaultItemStack());
        }

        return menu;
    }

}
