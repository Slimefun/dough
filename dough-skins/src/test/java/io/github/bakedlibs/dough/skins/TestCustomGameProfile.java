package io.github.bakedlibs.dough.skins;

import org.junit.jupiter.api.BeforeAll;

import be.seeseemelk.mockbukkit.MockBukkit;

class TestCustomGameProfile {

    @BeforeAll
    static void setup() {
        MockBukkit.mock();
    }

    // Test for issue Slimefun#4097
    // TODO: Enable - needs MockBukkit updated to 1.20
    /*
    @Test
    void testUuidIsSameAsProfile() throws NoSuchFieldException, IllegalAccessException, UnknownServerVersionException {
        UUID expectedUUID = UUID.randomUUID();

        CustomGameProfile gameProfile = new CustomGameProfile(
            expectedUUID,
            "test",
            null
        );

        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = itemStack.getItemMeta();
        SkullMeta skullMeta = (SkullMeta) meta;

        gameProfile.apply(skullMeta);
        itemStack.setItemMeta(skullMeta);

        // Assert the applied skin has the same UUID
        Assertions.assertEquals(expectedUUID, skullMeta.getOwningPlayer().getUniqueId());
    }
    */
}
