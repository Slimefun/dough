package io.github.bakedlibs.dough.inventory.factory;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.bakedlibs.dough.inventory.Menu;
import io.github.bakedlibs.dough.inventory.MenuLayout;
import io.github.bakedlibs.dough.inventory.builders.MenuLayoutBuilder;
import io.github.bakedlibs.dough.inventory.builders.SlotGroupBuilder;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

class TestMenuClosing {

    private static MenuFactory factory;
    private static ServerMock server;

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
    void testCloseAll() {
        // @formatter:off
        MenuLayout layout = new MenuLayoutBuilder(9)
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .withSlotRange(0, 8)
                    .build()
            )
            .build();
        // @formatter:on

        Menu inv = factory.createMenu(layout);

        Player player = server.addPlayer();
        InventoryView view = inv.open(player);

        Player player2 = server.addPlayer();
        InventoryView view2 = inv.open(player2);

        assertSame(view, player.getOpenInventory());
        assertSame(view2, player2.getOpenInventory());

        inv.closeAllViews();

        assertNull(player.getOpenInventory());
        assertNull(player2.getOpenInventory());
    }

}
