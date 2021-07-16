package io.github.bakedlibs.dough.versions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TestVersioned {

    @Test
    void testVersioned() {
        Versioned versioned = new MockVersioned("1.12.3");
        assertEquals(new SemanticVersion(1, 12, 3), versioned.getVersion());
    }

}
