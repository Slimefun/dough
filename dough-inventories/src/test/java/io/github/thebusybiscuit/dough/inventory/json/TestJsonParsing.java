package io.github.thebusybiscuit.dough.inventory.json;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.InputStream;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.dough.inventory.MenuLayout;

class TestJsonParsing {

    @Test
    void testValidFile() {
        assertDoesNotThrow(() -> parse("valid1"));
    }

    @Test
    void testNonExistingFile() {
        assertThrows(IllegalArgumentException.class, () -> parse("I do not exist"));
    }

    @Test
    void testMalformedJson() {
        assertThrows(InvalidLayoutException.class, () -> MenuLayoutParser.parseString("{ Is this \"\" how ] you json...?: false,"));
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
        return MenuLayoutParser.parseStream(inputStream);
    }

}
