package io.github.bakedlibs.dough.inventory.factory;

import java.util.function.BiFunction;
import java.util.logging.Logger;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import io.github.bakedlibs.dough.inventory.Menu;
import io.github.bakedlibs.dough.inventory.MenuLayout;
import io.github.bakedlibs.dough.inventory.SlotGroup;

/**
 * The {@link MenuFactory} is the core of this system, this is where everything
 * starts. You can use this {@link MenuFactory} to create {@link Menu}s from a
 * {@link MenuLayout}.
 * <p>
 * This class also handles the registration of our {@link Listener}.
 * 
 * @author TheBusyBiscuit
 *
 */
public class MenuFactory {

    /**
     * Our {@link Plugin} instance.
     */
    private final Plugin plugin;

    /**
     * This constructs a new {@link MenuFactory} for the given {@link Plugin}.
     * 
     * @param plugin
     *            The {@link Plugin} instance
     */
    public MenuFactory(@Nonnull Plugin plugin) {
        Validate.notNull(plugin, "The plugin instance cannot be null.");

        this.plugin = plugin;
        registerListener();
    }

    /**
     * This method registers our {@link MenuListener} to the {@link Server}.
     * This way, we can listen to and handle {@link InventoryClickEvent}s and alike.
     * 
     * @return Our registered {@link MenuListener}
     */
    private @Nonnull MenuListener registerListener() {
        MenuListener listener = new MenuListener(this);
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        return listener;
    }

    /**
     * This returns the {@link Plugin} which instantiated this {@link MenuFactory}.
     * 
     * @return The {@link Plugin} instance
     */
    public final @Nonnull Plugin getPlugin() {
        return plugin;
    }

    /**
     * Shortcut method for getting the {@link Logger} from
     * {@link #getPlugin()}.
     * 
     * @return The {@link Logger} of our {@link Plugin}
     */
    public final @Nonnull Logger getLogger() {
        return plugin.getLogger();
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
