package io.github.bakedlibs.dough.inventory.builders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.bakedlibs.dough.inventory.MenuLayout;
import io.github.bakedlibs.dough.inventory.SlotGroup;

import be.seeseemelk.mockbukkit.MockBukkit;

class TestLayoutBuilder {

    @BeforeAll
    static void setup() {
        MockBukkit.mock();
    }

    @AfterAll
    static void teardown() {
        MockBukkit.unmock();
    }

    @Test
    void testSlotGroups() {
        // @formatter:off
        MenuLayout layout = new MenuLayoutBuilder(9)
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .interactable(false)
                    .withSlot(1)
                    .withSlot(2)
                    .withSlot(3)
                    .build()
            )
            .addSlotGroup(
                new SlotGroupBuilder('y', "test2")
                    .interactable(true)
                    .withSlots(0, 4, 5, 6, 7, 8)
                    .withDefaultItem(new ItemStack(Material.DIAMOND))
                    .build()
            )
            .build();
        // @formatter:on

        assertNotNull(layout);
        assertEquals(9, layout.getSize());

        SlotGroup group = layout.getGroup('x');

        assertNotNull(group);
        assertEquals(3, group.size());
        assertSame(group, layout.getGroup("test"));
        assertSame(group, layout.getGroup(1));
        assertSame(group, layout.getGroup(2));
        assertSame(group, layout.getGroup(3));
        assertFalse(group.isInteractable());

        SlotGroup group2 = layout.getGroup("test2");

        assertNotNull(group2);
        assertSame(group2, layout.getGroup('y'));
        assertNotEquals(group, group2);
        assertEquals(6, group2.size());
        assertSame(group2, layout.getGroup(0));
        assertEquals(new ItemStack(Material.DIAMOND), group2.getDefaultItemStack());

        Set<SlotGroup> groups = layout.getSlotGroups();
        assertEquals(2, groups.size());
        assertTrue(groups.contains(group));
        assertTrue(groups.contains(group2));
    }

    @Test
    void testSlotGroupOverlapping() {
        // @formatter:off
        MenuLayoutBuilder builder = new MenuLayoutBuilder(9)
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .withSlot(1)
                    .build()
            )
            .addSlotGroup(
                new SlotGroupBuilder('y', "test2")
                    .withSlot(1)
                    .build()
            );
        // @formatter:on

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void testSlotGroupIdentifierCollision() {
        // @formatter:off
        MenuLayoutBuilder builder = new MenuLayoutBuilder(9)
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .withSlotRange(0, 3)
                    .build()
            )
            .addSlotGroup(
                new SlotGroupBuilder('x', "test2")
                    .withSlotRange(4, 8)
                    .build()
            );
        // @formatter:on

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void testSlotGroupNameCollision() {
        // @formatter:off
        MenuLayoutBuilder builder = new MenuLayoutBuilder(9)
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .withSlotRange(0, 3)
                    .build()
            )
            .addSlotGroup(
                new SlotGroupBuilder('y', "test")
                    .withSlotRange(4, 8)
                    .build()
            );
        // @formatter:on

        assertThrows(IllegalStateException.class, builder::build);
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, 10 })
    void testOutsideSlotGroups(int slot) {
        // @formatter:off
        MenuLayoutBuilder builder = new MenuLayoutBuilder(9)
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .withSlot(slot)
                    .build()
            );
        // @formatter:on

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    void testUnknownSlotGroups() {
        // @formatter:off
        MenuLayout layout = new MenuLayoutBuilder(9)
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .withSlotRange(0, 8)
                    .build()
            )
            .build();
        // @formatter:on

        assertThrows(IllegalArgumentException.class, () -> layout.getGroup(-1));
        assertThrows(IllegalArgumentException.class, () -> layout.getGroup(10));
        assertThrows(IllegalArgumentException.class, () -> layout.getGroup('a'));
        assertThrows(IllegalArgumentException.class, () -> layout.getGroup("Walshy"));
        assertThrows(IllegalArgumentException.class, () -> layout.getGroup(null));
    }

    @Test
    void testIncompleteSlotGroups() {
        // @formatter:off
        MenuLayoutBuilder builder = new MenuLayoutBuilder(9)
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .withSlotRange(1, 8)
                    .build()
            );
        // @formatter:on

        assertThrows(IllegalStateException.class, builder::build);
    }

    @ParameterizedTest(name = "{0} is not a valid inventory size.")
    @MethodSource("getIllegalInventorySizes")
    void testIllegalInventorySizes(int size) {
        assertThrows(IllegalArgumentException.class, () -> new MenuLayoutBuilder(size));
    }

    private static @Nonnull Stream<Arguments> getIllegalInventorySizes() {
        List<Integer> validArguments = Arrays.asList(9, 18, 27, 36, 45, 54);

        // @formatter:off
        return IntStream.range(-10, 60)
            .filter(arg -> !validArguments.contains(arg))
            .mapToObj(Arguments::of);
        // @formatter:on
    }

}
