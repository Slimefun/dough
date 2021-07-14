package io.github.bakedlibs.dough.inventory.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.bakedlibs.dough.inventory.Menu;
import io.github.bakedlibs.dough.inventory.MenuLayout;
import io.github.bakedlibs.dough.inventory.builders.SlotGroupBuilder;
import io.github.bakedlibs.dough.inventory.factory.MenuFactory;
import io.github.bakedlibs.dough.inventory.factory.MockMenuFactory;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

class TestSlotGroupInjection {

    private static ServerMock server;
    private static MenuFactory factory;

    @BeforeAll
    static void setup() {
        server = MockBukkit.mock();
        factory = new MockMenuFactory();
    }

    @AfterAll
    static void teardown() {
        MockBukkit.unmock();
    }

    @Test
    void testClickEvent() throws InvalidLayoutException {
        AtomicInteger clicks = new AtomicInteger();

        MenuLayout layout = parse("valid3", builder -> {
            if (!builder.name().equals("background")) {
                builder.onClick(payload -> clicks.incrementAndGet());
            }
        });

        Menu menu = factory.createMenu(layout);
        Player player = server.addPlayer();
        InventoryView view = menu.open(player);

        simulateClick(player, view, 1);
        simulateClick(player, view, 11);

        // Only slot 11 should have triggered this
        assertEquals(1, clicks.get());
    }

    @Test
    void testItemInjection() throws InvalidLayoutException {
        MenuLayout layout = parse("valid3", builder -> {
            if (builder.name().equals("background")) {
                builder.withDefaultItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }
        });

        Menu menu = factory.createMenu(layout);
        Player player = server.addPlayer();
        InventoryView view = menu.open(player);

        InventoryClickEvent clickEvent = simulateClick(player, view, 1);

        assertEquals(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), menu.getItem(clickEvent.getSlot()));
        assertTrue(clickEvent.isCancelled());

        InventoryClickEvent clickEvent2 = simulateClick(player, view, 11);

        assertEquals(null, menu.getItem(clickEvent2.getSlot()));
        assertFalse(clickEvent2.isCancelled());
    }

    @ParametersAreNonnullByDefault
    private @Nonnull InventoryClickEvent simulateClick(Player player, InventoryView view, int slot) {
        InventoryClickEvent event = new InventoryClickEvent(view, SlotType.CONTAINER, slot, ClickType.LEFT, InventoryAction.PICKUP_ONE);

        Bukkit.getPluginManager().callEvent(event);
        player.closeInventory();
        return event;
    }

    @ParametersAreNonnullByDefault
    private @Nonnull MenuLayout parse(String name, Consumer<SlotGroupBuilder> consumer) throws InvalidLayoutException {
        InputStream inputStream = getClass().getResourceAsStream("/" + name + ".json");
        return LayoutParser.parseStream(inputStream, consumer);
    }

}
