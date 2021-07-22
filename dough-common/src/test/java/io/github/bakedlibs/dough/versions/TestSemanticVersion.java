package io.github.bakedlibs.dough.versions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TestSemanticVersion {

    @Test
    void testConstructor() {
        SemanticVersion version = new SemanticVersion(1, 2);

        assertEquals(1, version.getMajorVersion());
        assertEquals(2, version.getMinorVersion());
        assertEquals(0, version.getPatchVersion());

        assertFalse(version.isPatch());

        assertEquals("1.2", version.getAsString());
        assertNotNull(version.toString());
    }

    @Test
    void testConstructorPatch() {
        SemanticVersion version = new SemanticVersion(1, 2, 3);

        assertEquals(1, version.getMajorVersion());
        assertEquals(2, version.getMinorVersion());
        assertEquals(3, version.getPatchVersion());

        assertEquals("1.2.3", version.getAsString());
    }

    @Test
    void testParsing() {
        SemanticVersion version = new SemanticVersion(1, 4);
        SemanticVersion version2 = new SemanticVersion(1, 4, 0);

        assertEquals(version, SemanticVersion.parse("1.4.0"));
        assertEquals(version, SemanticVersion.parse("1.4"));
        assertEquals(version, version2);
    }

    @Test
    void testParsingIllegalValues() {
        assertThrows(IllegalArgumentException.class, () -> SemanticVersion.parse(null));
        assertThrows(IllegalArgumentException.class, () -> SemanticVersion.parse("I am not a version"));
        assertThrows(IllegalArgumentException.class, () -> SemanticVersion.parse("1.almost.version.20.5"));
        assertThrows(IllegalArgumentException.class, () -> SemanticVersion.parse("1"));
        assertThrows(IllegalArgumentException.class, () -> SemanticVersion.parse(".1"));
        assertThrows(IllegalArgumentException.class, () -> SemanticVersion.parse(" 1 .2"));
    }

    @Test
    void testInvalidConstructorArguments() {
        assertThrows(IllegalArgumentException.class, () -> new SemanticVersion(-1, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> new SemanticVersion(1, -1, 1));
        assertThrows(IllegalArgumentException.class, () -> new SemanticVersion(1, 1, -1));
    }

    @Test
    void testIsSimilarDifferentClass() {
        SemanticVersion version1 = new SemanticVersion(1, 5, 10);
        PrefixedVersion version2 = new PrefixedVersion("TEST #", 5);

        assertFalse(version1.isSimilar(version2));
    }

    @Test
    void testIsPatch() {
        SemanticVersion version = new SemanticVersion(1, 0, 0);
        SemanticVersion version2 = new SemanticVersion(1, 0, 1);

        assertFalse(version.isPatch());
        assertTrue(version2.isPatch());
    }

    @Test
    void testIsSimilarNull() {
        SemanticVersion version = new SemanticVersion(1, 0, 4);

        assertFalse(version.isSimilar(null));
    }

    @Test
    void testEqualsIgnorePatch() {
        SemanticVersion version = new SemanticVersion(1, 0, 4);
        SemanticVersion version2 = new SemanticVersion(1, 0, 2);
        SemanticVersion version3 = new SemanticVersion(1, 0, 8);

        assertTrue(version.equalsIgnorePatch(1, 0));
        assertTrue(version.equalsIgnorePatch(version2));
        assertTrue(version.equalsIgnorePatch(version3));
    }

    @Test
    void testEqualsIgnorePatchNotMatching() {
        SemanticVersion version = new SemanticVersion(1, 2, 4);
        SemanticVersion version2 = new SemanticVersion(1, 3, 2);
        SemanticVersion version3 = new SemanticVersion(1, 1, 8);

        assertFalse(version.equalsIgnorePatch(2, 0));
        assertFalse(version.equalsIgnorePatch(version2));
        assertFalse(version.equalsIgnorePatch(version3));
    }

    @Test
    void testNewerThanMajor() {
        SemanticVersion version = new SemanticVersion(2, 0, 4);
        SemanticVersion version2 = new SemanticVersion(1, 1, 6);

        assertTrue(version.isNewerThan(version2));
        assertFalse(version2.isNewerThan(version));
    }

    @Test
    void testNewerThanMinor() {
        SemanticVersion version = new SemanticVersion(1, 2, 4);
        SemanticVersion version2 = new SemanticVersion(1, 0, 6);

        assertTrue(version.isNewerThan(version2));
        assertFalse(version2.isNewerThan(version));
    }

    @Test
    void testNewerThanPatch() {
        SemanticVersion version = new SemanticVersion(1, 2, 5);
        SemanticVersion version2 = new SemanticVersion(1, 2, 4);

        assertTrue(version.isNewerThan(version2));
        assertFalse(version2.isNewerThan(version));
    }

    @Test
    void testOlderThanMajor() {
        SemanticVersion version = new SemanticVersion(1, 1, 5);
        SemanticVersion version2 = new SemanticVersion(2, 0, 3);

        assertTrue(version.isOlderThan(version2));
        assertFalse(version2.isOlderThan(version));
    }

    @Test
    void testOlderThanMinor() {
        SemanticVersion version = new SemanticVersion(1, 1, 6);
        SemanticVersion version2 = new SemanticVersion(1, 4, 0);

        assertTrue(version.isOlderThan(version2));
        assertFalse(version2.isOlderThan(version));
    }

    @Test
    void testOlderThanPatch() {
        SemanticVersion version = new SemanticVersion(1, 2, 2);
        SemanticVersion version2 = new SemanticVersion(1, 2, 7);

        assertTrue(version.isOlderThan(version2));
        assertFalse(version2.isOlderThan(version));
    }

    @Test
    void testEqualTo() {
        SemanticVersion version = new SemanticVersion(1, 2, 5);
        SemanticVersion version2 = new SemanticVersion(1, 2, 5);

        assertEquals(version, version2);
        assertEquals(version.hashCode(), version2.hashCode());

        assertTrue(version.isEqualTo(version2));
        assertTrue(version2.isEqualTo(version));
    }

    @Test
    void testAtLeastVersion() {
        SemanticVersion version = new SemanticVersion(1, 2, 5);
        SemanticVersion version2 = new SemanticVersion(1, 2, 5);
        SemanticVersion version3 = new SemanticVersion(1, 2);

        assertTrue(version.isAtLeast(version2));
        assertTrue(version.isAtLeast(version3));
    }

    @Test
    void testAtLeastNumeric() {
        SemanticVersion version = new SemanticVersion(1, 2, 5);

        assertTrue(version.isAtLeast(0, 1));
        assertTrue(version.isAtLeast(1, 2));
        assertTrue(version.isAtLeast(1, 2, 1));
        assertTrue(version.isAtLeast(1, 2, 5));

        assertFalse(version.isAtLeast(2, 0));
        assertFalse(version.isAtLeast(2, 1, 2));
        assertFalse(version.isAtLeast(1, 2, 6));
        assertFalse(version.isAtLeast(1, 4));
    }

    @Test
    void testInvalidEqualTo() {
        SemanticVersion version = new SemanticVersion(1, 12, 4);
        PrefixedVersion version2 = new PrefixedVersion("DEV #", 120);

        assertThrows(IncomparableVersionsException.class, () -> version.isEqualTo(null));
        assertThrows(IncomparableVersionsException.class, () -> version.isEqualTo(version2));
    }

    @Test
    void testInvalidNewerThan() {
        SemanticVersion version = new SemanticVersion(1, 12, 4);
        PrefixedVersion version2 = new PrefixedVersion("DEV #", 120);

        assertThrows(IncomparableVersionsException.class, () -> version.isNewerThan(null));
        assertThrows(IncomparableVersionsException.class, () -> version.isNewerThan(version2));
    }

    @Test
    void testInvalidOlderThan() {
        SemanticVersion version = new SemanticVersion(1, 12, 4);
        PrefixedVersion version2 = new PrefixedVersion("DEV #", 120);

        assertThrows(IncomparableVersionsException.class, () -> version.isOlderThan(null));
        assertThrows(IncomparableVersionsException.class, () -> version.isOlderThan(version2));
    }

    @Test
    void testEqualToDifferentVersions() {
        SemanticVersion version = new SemanticVersion(1, 2, 5);
        SemanticVersion version2 = new SemanticVersion(1, 2, 6);
        SemanticVersion version3 = new SemanticVersion(1, 1, 5);
        SemanticVersion version4 = new SemanticVersion(2, 2, 5);

        assertNotEquals(version, version2);
        assertNotEquals(version, version3);
        assertNotEquals(version, version4);

        assertNotEquals(version.hashCode(), version2.hashCode());
        assertNotEquals(version.hashCode(), version3.hashCode());
        assertNotEquals(version.hashCode(), version4.hashCode());

        assertFalse(version.isEqualTo(version2));
        assertFalse(version.isEqualTo(version3));
        assertFalse(version.isEqualTo(version4));
    }

    @Test
    void testInvalidEquals() {
        SemanticVersion version = new SemanticVersion(1, 14, 4);
        PrefixedVersion version2 = new PrefixedVersion("TEST #", 5);
        Object otherObject = "1.14.4";

        assertNotEquals(version, otherObject);
        assertNotEquals(version, version2);
    }

    @Test
    void testCompare() {
        SemanticVersion version = new SemanticVersion(1, 14);
        SemanticVersion version2 = new SemanticVersion(1, 15);
        SemanticVersion version3 = new SemanticVersion(1, 13);
        PrefixedVersion version4 = new PrefixedVersion("TEST #", 5);

        assertThrows(NullPointerException.class, () -> version.compareTo(null));

        assertEquals(0, version.compareTo(version));
        assertEquals(-1, version.compareTo(version2));
        assertEquals(1, version.compareTo(version3));

        assertThrows(IncomparableVersionsException.class, () -> version.compareTo(version4));
    }

}
