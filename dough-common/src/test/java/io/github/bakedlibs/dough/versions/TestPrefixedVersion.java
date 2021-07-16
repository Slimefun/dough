package io.github.bakedlibs.dough.versions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TestPrefixedVersion {

    @Test
    void testConstructor() {
        PrefixedVersion version = new PrefixedVersion("TEST #", 12);

        assertEquals("TEST #", version.getPrefix());
        assertEquals(12, version.getNumericVersion());
        assertEquals("TEST #12", version.getAsString());
        assertNotNull(version.toString());
    }

    @Test
    void testInvalidConstructorArguments() {
        assertThrows(IllegalArgumentException.class, () -> new PrefixedVersion(null, 1));
        assertThrows(IllegalArgumentException.class, () -> new PrefixedVersion("prefix", -10));
    }

    @Test
    void testIsSimilarSamePrefix() {
        PrefixedVersion version1 = new PrefixedVersion("TEST #", 5);
        PrefixedVersion version2 = new PrefixedVersion("TEST #", 8);

        assertTrue(version1.isSimilar(version2));
    }

    @Test
    void testIsSimilarDifferentPrefix() {
        PrefixedVersion version1 = new PrefixedVersion("TEST #", 5);
        PrefixedVersion version2 = new PrefixedVersion("PROD #", 8);

        assertFalse(version1.isSimilar(version2));
    }

    @Test
    void testIsSimilarDifferentClass() {
        PrefixedVersion version1 = new PrefixedVersion("TEST #", 5);
        SemanticVersion version2 = new SemanticVersion(1, 5, 10);

        assertFalse(version1.isSimilar(version2));
    }

    @Test
    void testIsSimilarNull() {
        PrefixedVersion version = new PrefixedVersion("TEST #", 5);

        assertFalse(version.isSimilar(null));
    }

    @Test
    void testNewerThan() {
        PrefixedVersion version = new PrefixedVersion("TEST #", 120);
        PrefixedVersion version2 = new PrefixedVersion("TEST #", 42);

        assertTrue(version.isNewerThan(version2));
        assertFalse(version2.isNewerThan(version));
    }

    @Test
    void testOlderThan() {
        PrefixedVersion version = new PrefixedVersion("TEST #", 120);
        PrefixedVersion version2 = new PrefixedVersion("TEST #", 42);

        assertFalse(version.isOlderThan(version2));
        assertTrue(version2.isOlderThan(version));
    }

    @Test
    void testIsEqualTo() {
        PrefixedVersion version = new PrefixedVersion("TEST #", 10);
        PrefixedVersion version2 = new PrefixedVersion("TEST #", 10);

        assertEquals(version, version2);
        assertEquals(version.hashCode(), version2.hashCode());

        assertTrue(version.isEqualTo(version2));
        assertFalse(version.isNewerThan(version2));
        assertFalse(version.isOlderThan(version2));
    }

    @Test
    void testIsEqualToDifferentVersions() {
        PrefixedVersion version = new PrefixedVersion("TEST #", 5);
        PrefixedVersion version2 = new PrefixedVersion("TEST #", 2);
        PrefixedVersion version3 = new PrefixedVersion("TEST #", 8);

        assertNotEquals(version, version2);
        assertNotEquals(version, version3);

        assertNotEquals(version.hashCode(), version2.hashCode());
        assertNotEquals(version.hashCode(), version3.hashCode());

        assertFalse(version.isEqualTo(version2));
        assertFalse(version.isEqualTo(version3));
    }

    @Test
    void testInvalidNewerThan() {
        PrefixedVersion version = new PrefixedVersion("TEST #", 10);
        PrefixedVersion version2 = new PrefixedVersion("PROD #", 10);
        SemanticVersion version3 = new SemanticVersion(2, 0, 5);

        assertThrows(IncomparableVersionsException.class, () -> version.isNewerThan(version2));
        assertThrows(IncomparableVersionsException.class, () -> version.isNewerThan(version3));
        assertThrows(IncomparableVersionsException.class, () -> version.isNewerThan(null));
    }

    @Test
    void testInvalidOlderThan() {
        PrefixedVersion version = new PrefixedVersion("TEST #", 10);
        PrefixedVersion version2 = new PrefixedVersion("PROD #", 10);
        SemanticVersion version3 = new SemanticVersion(2, 0, 5);

        assertThrows(IncomparableVersionsException.class, () -> version.isOlderThan(version2));
        assertThrows(IncomparableVersionsException.class, () -> version.isOlderThan(version3));
        assertThrows(IncomparableVersionsException.class, () -> version.isOlderThan(null));
    }

    @Test
    void testInvalidEqualTo() {
        PrefixedVersion version = new PrefixedVersion("TEST #", 10);
        PrefixedVersion version2 = new PrefixedVersion("PROD #", 10);
        SemanticVersion version3 = new SemanticVersion(2, 0, 5);

        assertThrows(IncomparableVersionsException.class, () -> version.isEqualTo(version2));
        assertThrows(IncomparableVersionsException.class, () -> version.isEqualTo(version3));
        assertThrows(IncomparableVersionsException.class, () -> version.isEqualTo(null));
    }

    @Test
    void testInvalidEquals() {
        PrefixedVersion version = new PrefixedVersion("TEST #", 10);
        SemanticVersion version2 = new SemanticVersion(2, 0, 5);
        Object otherObject = "TEST #10";

        assertNotEquals(version, otherObject);
        assertNotEquals(version, version2);
    }

    @Test
    void testCompare() {
        PrefixedVersion version = new PrefixedVersion("TEST #", 6);
        PrefixedVersion version2 = new PrefixedVersion("TEST #", 10);
        PrefixedVersion version3 = new PrefixedVersion("TEST #", 3);
        SemanticVersion version4 = new SemanticVersion(1, 2);

        assertThrows(NullPointerException.class, () -> version.compareTo(null));

        assertEquals(0, version.compareTo(version));
        assertEquals(-1, version.compareTo(version2));
        assertEquals(1, version.compareTo(version3));

        assertThrows(IncomparableVersionsException.class, () -> version.compareTo(version4));
    }
}
