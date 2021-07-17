package io.github.bakedlibs.dough.versions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TestSimpleNumericVersion {

    @Test
    void testConstructor() {
        SimpleNumericVersion version = new SimpleNumericVersion(4);

        assertEquals(4, version.asInteger());
        assertEquals("4", version.getAsString());
        assertNotNull(version.toString());
    }

    @Test
    void testInvalidConstructorArguments() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleNumericVersion(-10));
    }

    @Test
    void testIsSimilarSameClass() {
        SimpleNumericVersion version1 = new SimpleNumericVersion(5);
        SimpleNumericVersion version2 = new SimpleNumericVersion(8);

        assertTrue(version1.isSimilar(version2));
    }

    @Test
    void testIsSimilarDifferentClass() {
        SimpleNumericVersion version1 = new SimpleNumericVersion(5);
        SemanticVersion version2 = new SemanticVersion(1, 5, 10);

        assertFalse(version1.isSimilar(version2));
    }

    @Test
    void testIsSimilarNull() {
        SimpleNumericVersion version = new SimpleNumericVersion(3);

        assertFalse(version.isSimilar(null));
    }

    @Test
    void testNewerThan() {
        SimpleNumericVersion version = new SimpleNumericVersion(120);
        SimpleNumericVersion version2 = new SimpleNumericVersion(42);

        assertTrue(version.isNewerThan(version2));
        assertFalse(version2.isNewerThan(version));
    }

    @Test
    void testOlderThan() {
        SimpleNumericVersion version = new SimpleNumericVersion(120);
        SimpleNumericVersion version2 = new SimpleNumericVersion(42);

        assertFalse(version.isOlderThan(version2));
        assertTrue(version2.isOlderThan(version));
    }

    @Test
    void testIsEqualTo() {
        SimpleNumericVersion version = new SimpleNumericVersion(10);
        SimpleNumericVersion version2 = new SimpleNumericVersion(10);

        assertEquals(version, version2);
        assertEquals(version.hashCode(), version2.hashCode());

        assertTrue(version.isEqualTo(version2));
        assertFalse(version.isNewerThan(version2));
        assertFalse(version.isOlderThan(version2));
    }

    @Test
    void testIsEqualToDifferentVersions() {
        SimpleNumericVersion version = new SimpleNumericVersion(5);
        SimpleNumericVersion version2 = new SimpleNumericVersion(2);
        SimpleNumericVersion version3 = new SimpleNumericVersion(8);

        assertNotEquals(version, version2);
        assertNotEquals(version, version3);

        assertNotEquals(version.hashCode(), version2.hashCode());
        assertNotEquals(version.hashCode(), version3.hashCode());

        assertFalse(version.isEqualTo(version2));
        assertFalse(version.isEqualTo(version3));
    }

    @Test
    void testInvalidNewerThan() {
        SimpleNumericVersion version = new SimpleNumericVersion(10);
        PrefixedVersion version2 = new PrefixedVersion("PROD #", 10);
        SemanticVersion version3 = new SemanticVersion(2, 0, 5);

        assertThrows(IncomparableVersionsException.class, () -> version.isNewerThan(version2));
        assertThrows(IncomparableVersionsException.class, () -> version.isNewerThan(version3));
        assertThrows(IncomparableVersionsException.class, () -> version.isNewerThan(null));
    }

    @Test
    void testInvalidOlderThan() {
        SimpleNumericVersion version = new SimpleNumericVersion(10);
        PrefixedVersion version2 = new PrefixedVersion("PROD #", 10);
        SemanticVersion version3 = new SemanticVersion(2, 0, 5);

        assertThrows(IncomparableVersionsException.class, () -> version.isOlderThan(version2));
        assertThrows(IncomparableVersionsException.class, () -> version.isOlderThan(version3));
        assertThrows(IncomparableVersionsException.class, () -> version.isOlderThan(null));
    }

    @Test
    void testInvalidEqualTo() {
        SimpleNumericVersion version = new SimpleNumericVersion(10);
        PrefixedVersion version2 = new PrefixedVersion("PROD #", 10);
        SemanticVersion version3 = new SemanticVersion(2, 0, 5);

        assertThrows(IncomparableVersionsException.class, () -> version.isEqualTo(version2));
        assertThrows(IncomparableVersionsException.class, () -> version.isEqualTo(version3));
        assertThrows(IncomparableVersionsException.class, () -> version.isEqualTo(null));
    }

    @Test
    void testInvalidEquals() {
        SimpleNumericVersion version = new SimpleNumericVersion(10);
        SemanticVersion version2 = new SemanticVersion(2, 0, 5);
        Object otherObject = "TEST #10";

        assertNotEquals(version, otherObject);
        assertNotEquals(version, version2);
    }

    @Test
    void testCompare() {
        SimpleNumericVersion version = new SimpleNumericVersion(6);
        SimpleNumericVersion version2 = new SimpleNumericVersion(10);
        SimpleNumericVersion version3 = new SimpleNumericVersion(3);
        SemanticVersion version4 = new SemanticVersion(1, 2);

        assertThrows(NullPointerException.class, () -> version.compareTo(null));

        assertEquals(0, version.compareTo(version));
        assertEquals(-1, version.compareTo(version2));
        assertEquals(1, version.compareTo(version3));

        assertThrows(IncomparableVersionsException.class, () -> version.compareTo(version4));
    }
}
