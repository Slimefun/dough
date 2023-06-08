package io.github.bakedlibs.dough.inventory.json;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.InputStream;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.junit.jupiter.api.Test;

import io.github.bakedlibs.dough.inventory.MenuLayout;
import io.github.bakedlibs.dough.inventory.SlotGroup;

class TestJsonParsing {

    @Test
    void testValidFile() {
        assertDoesNotThrow(() -> parse("valid1"));
    }

    @Test
    void testCorrectSlotGroups() throws InvalidLayoutException {
        MenuLayout layout = parse("valid2");

        assertEquals(27, layout.getSize());
        assertEquals(3, layout.getSlotGroups().size());

        assertSlotGroup(layout, 0, '*', "background", false);
        assertSlotGroup(layout, 13, '*', "background", false);
        assertSlotGroup(layout, 26, '*', "background", false);

        assertSlotGroup(layout, 10, '+', "input", true);
        assertSlotGroup(layout, 11, '+', "input", true);

        assertSlotGroup(layout, 14, '-', "output", true);
        assertSlotGroup(layout, 17, '-', "output", true);
    }

    @ParametersAreNonnullByDefault
    private void assertSlotGroup(MenuLayout layout, int slot, char identifier, String name, boolean interactable) {
        SlotGroup group1 = layout.getGroup(slot);
        SlotGroup group2 = layout.getGroup(identifier);
        SlotGroup group3 = layout.getGroup(name);

        assertSame(group1, group2);
        assertSame(group1, group3);
        assertSame(group2, group3);

        // All three are the same, so this should apply to all of them.
        assertEquals(interactable, group1.isInteractable());
    }

    @Test
    void testNonExistingFile() {
        assertThrows(IllegalArgumentException.class, () -> parse("I do not exist"));
    }

    @Test
    void testMalformedJson() {
        assertThrows(InvalidLayoutException.class, () -> LayoutParser.parseString("{ Is this \"\" how ] you json...?: false,"));
    }

    @Test
    void testTooManyRows() {
        assertInvalid("invalid1");
    }

    @Test
    void testNoRows() {
        assertInvalid("invalid2");
    }

    @Test
    void testMissingLayoutObject() {
        assertInvalid("invalid3");
    }

    @Test
    void testMissingGroupsObject() {
        assertInvalid("invalid4");
    }

    @Test
    void testIncompleteGroupsObject() {
        assertInvalid("invalid5");
    }

    @Test
    void testLayoutWrongType() {
        assertInvalid("invalid6");
    }

    @Test
    void testGroupsWrongType() {
        assertInvalid("invalid7");
    }

    @Test
    void testLayoutRowsWrongType() {
        assertInvalid("invalid8");
    }

    @Test
    void testMissingSlotGroupName() {
        assertInvalid("invalid9");
    }

    @Test
    void testMissingSlotGroupInteractable() {
        assertInvalid("invalid10");
    }

    @Test
    void testSlotGroupInteractableWrongType() {
        assertInvalid("invalid11");
    }

    @Test
    void testTooLongRow() {
        assertInvalid("invalid12");
    }

    private void assertInvalid(@Nonnull String name) {
        assertThrows(InvalidLayoutException.class, () -> parse(name));
    }

    private @Nonnull MenuLayout parse(@Nonnull String name) throws InvalidLayoutException {
        InputStream inputStream = getClass().getResourceAsStream("/" + name + ".json");
        return LayoutParser.parseStream(inputStream);
    }

}
