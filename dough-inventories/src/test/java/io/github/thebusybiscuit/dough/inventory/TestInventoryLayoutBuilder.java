package io.github.thebusybiscuit.dough.inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.thebusybiscuit.dough.inventory.builders.InventoryLayoutBuilder;
import io.github.thebusybiscuit.dough.inventory.builders.SlotGroupBuilder;

class TestInventoryLayoutBuilder {

    @Test
    void testSlotGroups() {
        // @formatter:off
        InventoryLayout layout = new InventoryLayoutBuilder(9)
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .interactable(false)
                    .withSlots(1, 2, 3)
                    .build()
            )
            .addSlotGroup(
                new SlotGroupBuilder('y', "test2")
                    .interactable(true)
                    .withSlots(0, 4, 5, 6, 7, 8)
                    .build()
            )
            .build();
        // @formatter:on

        assertNotNull(layout);

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
    }

    @ParameterizedTest(name = "{0} is not a valid inventory size.")
    @MethodSource("getIllegalInventorySizes")
    void testIllegalInventorySizes(int size) {
        assertThrows(IllegalArgumentException.class, () -> new InventoryLayoutBuilder(size));
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
