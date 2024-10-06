package io.github.bakedlibs.dough.items;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.inventory.meta.ItemMetaMock;
import be.seeseemelk.mockbukkit.inventory.meta.LeatherArmorMetaMock;
import be.seeseemelk.mockbukkit.inventory.meta.PotionMetaMock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Collectors;

class TestItemStackUtil {

    @BeforeAll
    public static void init() {
        MockBukkit.mock();
    }

    @AfterAll
    public static void teardown() {
        MockBukkit.unmock();
    }

    @Test
    void testConsumerIsInvokedOnceIfMetaIsNotNull() {
        ItemStack itemStack = new ItemStack(Material.AIR);
        itemStack.setItemMeta(new ItemMetaMock());
        ConsumerInvocationCounter<ItemMeta> counter = new ConsumerInvocationCounter<>();
        ItemStackUtil.editMeta(itemStack, counter);
        Assertions.assertTrue(counter.wasInvoked());
        Assertions.assertEquals(1, counter.getInvokeCount());
    }

    @Test
    void testDisplayNameChangedAndSanitized() {
        String name = "&btest";
        String expected = ChatColor.translateAlternateColorCodes('&', name);
        ItemMeta meta = new ItemMetaMock();
        ItemStackUtil.editDisplayName(name).accept(meta);
        Assertions.assertEquals(expected, meta.getDisplayName());
    }

    @Test
    void testLoreAsListChangedAndSanitized() {
        List<String> lore = List.of("&btest");
        List<String> expected = lore.stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
        ItemMeta meta = new ItemMetaMock();
        ItemStackUtil.editLore(lore).accept(meta);
        Assertions.assertIterableEquals(expected, meta.getLore());
    }

    @Test
    void testLoreAsVarargsChangedAndSanitized() {
        List<String> expected = List.of(ChatColor.translateAlternateColorCodes('&', "&btest"));
        ItemMeta meta = new ItemMetaMock();
        ItemStackUtil.editLore("&btest").accept(meta);
        Assertions.assertIterableEquals(expected, meta.getLore());
    }

    @Test
    void testColorUnchanged() {
        ItemMeta original = new ItemMetaMock();
        ItemMeta unchanged = new ItemMetaMock();
        ItemStackUtil.editColor(Color.AQUA).accept(unchanged);
        Assertions.assertEquals(original, unchanged);
    }

    @Test
    void testColorChangedForPotionMeta() {
        PotionMeta meta = new PotionMetaMock();
        ItemStackUtil.editColor(Color.AQUA).accept(meta);
        Assertions.assertEquals(Color.AQUA, meta.getColor());
    }

    @Test
    void testColorChangedForLeatherMeta() {
        LeatherArmorMeta meta = new LeatherArmorMetaMock();
        ItemStackUtil.editColor(Color.AQUA).accept(meta);
        Assertions.assertEquals(Color.AQUA, meta.getColor());
    }

    @Test
    void testCustomModelDataChanged() {
        ItemMeta itemMeta = new ItemMetaMock();
        ItemStackUtil.editCustomModelData(1).accept(itemMeta);
        Assertions.assertEquals(1, itemMeta.getCustomModelData());
    }

    @Test
    void testCustomModelDataNullIfZero() {
        ItemMeta meta = new ItemMetaMock();
        meta.setCustomModelData(1);
        ItemStackUtil.editCustomModelData(0).accept(meta);
        Assertions.assertFalse(meta.hasCustomModelData());
    }

    @Test
    void testCustomModelDataNullIfNull() {
        ItemMeta meta = new ItemMetaMock();
        meta.setCustomModelData(1);
        ItemStackUtil.editCustomModelData(null).accept(meta);
        Assertions.assertFalse(meta.hasCustomModelData());
    }

    @Test
    void testItemFlagsChanged() {
        ItemMeta meta = new ItemMetaMock();
        Assertions.assertFalse(meta.getItemFlags().contains(ItemFlag.HIDE_ENCHANTS));
        ItemStackUtil.appendItemFlags(ItemFlag.HIDE_ENCHANTS).accept(meta);
        Assertions.assertTrue(meta.getItemFlags().contains(ItemFlag.HIDE_ENCHANTS));
    }

}
