package io.github.bakedlibs.dough.inventory.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.bakedlibs.dough.inventory.Menu;
import io.github.bakedlibs.dough.inventory.MenuLayout;
import io.github.bakedlibs.dough.inventory.SlotGroup;
import io.github.bakedlibs.dough.inventory.builders.MenuLayoutBuilder;
import io.github.bakedlibs.dough.inventory.builders.SlotGroupBuilder;

import be.seeseemelk.mockbukkit.MockBukkit;

class TestMenuCreation {

    private static MenuFactory factory;

    @BeforeAll
    static void setup() {
        MockBukkit.mock();
        factory = new MockMenuFactory();
    }

    @AfterAll
    static void teardown() {
        MockBukkit.unmock();
    }

    @ParameterizedTest
    @ValueSource(ints = { 9, 18, 27, 36, 45, 54 })
    void testCreationWithSize(int size) {
        // @formatter:off
        MenuLayout layout = new MenuLayoutBuilder(size)
            .title("Awesome Inventory!")
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .withSlotRange(0, size - 1)
                    .build()
            )
            .build();
        // @formatter:on

        Menu inv = factory.createMenu(layout);

        assertNotNull(inv);
        assertEquals(layout, inv.getLayout());
        assertEquals(factory, inv.getFactory());
        assertEquals(layout.getTitle(), inv.getTitle());
        assertEquals(layout.getSize(), inv.getSize());

        assertNotNull(inv.getInventory());
        assertSame(inv, inv.getInventory().getHolder());
    }

    @Test
    void testInventoryValidation() {
        // @formatter:off
        MenuLayout layout = new MenuLayoutBuilder(9)
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .withSlotRange(0, 8)
                    .build()
            )
            .build();
        // @formatter:on

        Menu inv = new CustomMenu(factory, layout);

        assertThrows(UnsupportedOperationException.class, () -> inv.getInventory());
    }

    @Test
    void testInventoryHolderValidation() {
        // @formatter:off
        MenuLayout layout = new MenuLayoutBuilder(9)
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .withSlotRange(0, 8)
                    .build()
            )
            .build();
        // @formatter:on

        CustomMenu inv = new CustomMenu(factory, layout);

        // InventoryHolder == null
        Inventory inventory = Bukkit.createInventory(null, 9);

        assertThrows(IllegalArgumentException.class, () -> inv.setInventory(inventory));

        // Different inventory size
        Inventory inventory2 = Bukkit.createInventory(inv, 18);

        assertThrows(IllegalArgumentException.class, () -> inv.setInventory(inventory2));
    }

    @Test
    void testDefaultItem() {
        // @formatter:off
        MenuLayout layout = new MenuLayoutBuilder(9)
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .withSlots(0, 1)
                    .withDefaultItem(new ItemStack(Material.APPLE))
                    .build()
            )
            .addSlotGroup(
                new SlotGroupBuilder('y', "test2")
                    .withSlotRange(2, 8)
                    .build()
            )
            .build();
        // @formatter:on

        Menu inv = factory.createMenu(layout);

        assertEquals(new ItemStack(Material.APPLE), inv.getItem(0));
        assertEquals(new ItemStack(Material.APPLE), inv.getItem(1));
        assertNull(inv.getItem(2));
        assertNull(inv.getItem(3));
        assertNull(inv.getItem(4));
        assertNull(inv.getItem(5));
        assertNull(inv.getItem(6));
        assertNull(inv.getItem(7));
        assertNull(inv.getItem(8));
    }

    @Test
    void testAddItem() {
        // @formatter:off
        MenuLayout layout = new MenuLayoutBuilder(9)
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .withSlotRange(0, 5)
                    .build()
            )
            .addSlotGroup(
                new SlotGroupBuilder('y', "test2")
                    .withSlotRange(6, 8)
                    .build()
            )
            .build();
        // @formatter:on

        Menu inv = factory.createMenu(layout);
        SlotGroup group = layout.getGroup('y');

        inv.setItem(6, new ItemStack(Material.AIR));

        ItemStack item = new ItemStack(Material.EMERALD);
        inv.addItem(group, item);

        assertNull(inv.getItem(0));
        assertEquals(item, inv.getItem(6));

        inv.addItem(group, item);
        assertEquals(2, inv.getItem(6).getAmount());

        inv.addItem(group, new ItemStack(Material.EMERALD, 63));
        assertEquals(64, inv.getItem(6).getAmount());
        assertEquals(item, inv.getItem(7));

        inv.addItem(group, item);
        assertEquals(2, inv.getItem(7).getAmount());

        inv.addItem(group, new ItemStack(Material.DIAMOND));
        assertEquals(new ItemStack(Material.DIAMOND), inv.getItem(8));

        ItemStack cantFit = new ItemStack(Material.GOLDEN_APPLE);
        assertEquals(cantFit, inv.addItem(group, cantFit));
    }

}
