package io.github.bakedlibs.dough.protection.modules;

import de.epiceric.shopchest.ShopChest;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.bakedlibs.dough.protection.ProtectionModule;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;

/**
 * Provides protection handling for ShopChest
 *
 * @author EpicPlayerA10
 */
public class ShopChestProtectionModule implements ProtectionModule {
    private ShopChest shopChest;
    private final Plugin plugin;

    public ShopChestProtectionModule(@Nonnull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        this.shopChest = ShopChest.getInstance();
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public boolean hasPermission(OfflinePlayer p, Location l, Interaction action) {
        if (action == Interaction.BREAK_BLOCK) {
            return !this.shopChest.getShopUtils().isShop(l);
        } else {
            return true;
        }
    }
}
