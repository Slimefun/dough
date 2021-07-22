package io.github.bakedlibs.dough.versions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bukkit.Server;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import be.seeseemelk.mockbukkit.MockBukkit;

class TestMockDetection {

    @Test
    void testMockBukkit() throws UnknownServerVersionException {
        MockBukkit.mock();

        assertTrue(MinecraftVersion.isMocked());

        MockBukkit.unmock();
    }

    @Test
    void testNotMockBukkit() throws UnknownServerVersionException {
        /*
         * Technically also mocked.
         * But not MockBukkit ¯\_(ツ)_/¯
         */
        Server server = Mockito.mock(Server.class);

        assertFalse(MinecraftVersion.isMocked(server));
    }

}
