package io.github.thebusybiscuit.dough.inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.thebusybiscuit.dough.inventory.builders.InventoryLayoutBuilder;
import io.github.thebusybiscuit.dough.inventory.builders.SlotGroupBuilder;

import be.seeseemelk.mockbukkit.MockBukkit;

class TestInventoryCreation {

    @BeforeAll
    static void setup() {
        MockBukkit.mock();
    }

    @AfterAll
    static void teardown() {
        MockBukkit.unmock();
    }

    @ParameterizedTest
    @ValueSource(ints = { 9, 18, 27, 36, 45, 54 })
    void testInventorySize(int size) {
        // @formatter:off
        InventoryLayout layout = new InventoryLayoutBuilder(size)
            .title("Awesome Inventory!")
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .withSlots(IntStream.range(0, size).toArray())
                    .build()
            )
            .build();
        // @formatter:on

        CustomInventory inv = layout.createInventory();

        assertNotNull(inv);
        assertEquals(layout, inv.getLayout());
        assertEquals(layout.getTitle(), inv.getTitle());
        assertEquals(layout.getSize(), inv.getSize());

        assertNotNull(inv.getInventory());
        assertSame(inv, inv.getInventory().getHolder());
    }

    @Test
    void testInventoryValidation() {
        // @formatter:off
        InventoryLayout layout = new InventoryLayoutBuilder(9)
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .withSlots(IntStream.range(0, 9).toArray())
                    .build()
            )
            .build();
        // @formatter:on

        CustomInventory inv = new CustomInventoryImpl(layout);

        assertThrows(UnsupportedOperationException.class, () -> inv.getInventory());
    }

    @Test
    void testInventoryHolderValidation() {
        // @formatter:off
        InventoryLayout layout = new InventoryLayoutBuilder(9)
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .withSlots(IntStream.range(0, 9).toArray())
                    .build()
            )
            .build();
        // @formatter:on

        CustomInventoryImpl inv = new CustomInventoryImpl(layout);

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
        InventoryLayout layout = new InventoryLayoutBuilder(9)
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .withSlots(0, 1)
                    .withDefaultItem(new ItemStack(Material.APPLE))
                    .build()
            )
            .addSlotGroup(
                new SlotGroupBuilder('y', "test2")
                    .withSlots(2, 3, 4, 5, 6, 7, 8)
                    .build()
            )
            .build();
        // @formatter:on

        CustomInventory inv = layout.createInventory();

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
    @Disabled("Not yet implemented")
    void testAddItem() {
        // @formatter:off
        InventoryLayout layout = new InventoryLayoutBuilder(9)
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .withSlots(0, 1, 2, 3)
                    .build()
            )
            .addSlotGroup(
                new SlotGroupBuilder('y', "test2")
                    .withSlots(4, 5, 6, 7, 8)
                    .build()
            )
            .build();
        // @formatter:on

        CustomInventory inv = layout.createInventory();
        SlotGroup group = layout.getGroup('y');

        ItemStack item = new ItemStack(Material.EMERALD);
        inv.addItem(group, item);

        assertNull(inv.getItem(0));
        assertEquals(item, inv.getItem(4));

        inv.addItem(group, item);
        assertEquals(2, inv.getItem(4).getAmount());

        inv.addItem(group, new ItemStack(Material.EMERALD, 63));
        assertEquals(64, inv.getItem(4).getAmount());
        assertEquals(item, inv.getItem(5));
    }

}
