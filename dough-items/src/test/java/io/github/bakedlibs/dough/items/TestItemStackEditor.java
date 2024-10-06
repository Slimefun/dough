package io.github.bakedlibs.dough.items;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.UnimplementedOperationException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
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
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TestItemStackEditor {

    private static Stream<Material> itemMaterials() {
        return Arrays.stream(Material.values())
                .filter(Predicate.not(Material::isLegacy))
                .filter(Material::isItem);
    }

    private static ItemMeta getItemMeta(Material material) {
        try {
            return Bukkit.getItemFactory().getItemMeta(material);
        } catch (UnimplementedOperationException ex) {
            return null;
        }
    }

    private static Stream<Material> potionMaterials() {
        return itemMaterials().filter(material -> getItemMeta(material) instanceof PotionMeta);
    }

    private static Stream<Material> leatherArmorMaterials() {
        return itemMaterials().filter(material -> getItemMeta(material) instanceof LeatherArmorMeta);
    }

    @BeforeAll
    public static void init() {
        MockBukkit.mock();
    }

    @AfterAll
    public static void teardown() {
        MockBukkit.unmock();
    }

    @ParameterizedTest
    @MethodSource("itemMaterials")
    void testCanCreateItemStackEditor(Material material) {
        Assertions.assertDoesNotThrow(() -> new ItemStackEditor(material));
        Assertions.assertDoesNotThrow(() -> new ItemStackEditor(new ItemStack(material)));
    }

    @Test
    void testDefaultConstructorsCreateEquivalentItem() {
        ItemStack expected = new ItemStack(Material.AIR);
        ItemStackEditor editorItem = new ItemStackEditor(expected);
        ItemStackEditor editorMaterial = new ItemStackEditor(Material.AIR);
        Assertions.assertEquals(expected, editorItem.create());
        Assertions.assertEquals(expected, editorMaterial.create());
    }

    @Test
    void testCreateAppliesToClone() {
        ItemStack original = new ItemStack(Material.STICK);
        ItemStackEditor customItemStack = new ItemStackEditor(original);
        original.setAmount(2);
        Assertions.assertNotEquals(original, customItemStack.create());
    }

    @Test
    void testApplyTo() {
        ItemStack itemStack = new ItemStack(Material.STICK);
        ItemStack target = new ItemStack(itemStack);
        new ItemStackEditor(itemStack)
                .setAmount(2)
                .applyTo(target);
        Assertions.assertEquals(2, target.getAmount());
    }

    @Test
    void testConstructorClonesInput() {
        ItemStack itemStack = new ItemStack(Material.STICK);
        ItemStack expected = new ItemStack(itemStack);
        ItemStackEditor editor = new ItemStackEditor(itemStack);
        itemStack.setAmount(2);
        Assertions.assertEquals(expected, editor.create());
    }

    @Test
    void testNullDisplayNameAllowed() {
        ItemMeta meta = new ItemStackEditor(Material.STICK)
                .setDisplayName(null)
                .create().getItemMeta();
        Assertions.assertNotNull(meta);
        Assertions.assertFalse(meta.hasDisplayName());
    }

    @Test
    void testDisplayNameChangedAndSanitized() {
        String name = "&btest";
        String expectedName = ChatColor.translateAlternateColorCodes('&', name);
        ItemMeta meta = new ItemStackEditor(Material.STICK)
                .setDisplayName(name)
                .create().getItemMeta();
        Assertions.assertNotNull(meta);
        Assertions.assertEquals(expectedName, meta.getDisplayName());
    }

    @Test
    void testLoreAsListChangedAndSanitized() {
        List<String> lore = List.of("&btest");
        List<String> expected = lore.stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
        ItemMeta meta = new ItemStackEditor(Material.STICK)
                .setLore(lore)
                .create().getItemMeta();
        Assertions.assertIterableEquals(expected, meta.getLore());
    }

    @Test
    void testLoreAsVarargsChangedAndSanitized() {
        List<String> lore = List.of("&btest");
        List<String> expected = lore.stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
        ItemMeta meta = new ItemStackEditor(Material.STICK)
                .setLore("&btest")
                .create().getItemMeta();
        Assertions.assertIterableEquals(expected, meta.getLore());
    }

    @Test
    void testColorUnchanged() {
        ItemStack expected = new ItemStack(Material.STICK);
        // set the color on an invalid item will still cause
        // an explicit set of the item meta
        expected.setItemMeta(expected.getItemMeta());
        ItemStack actual = new ItemStackEditor(expected)
                .setColor(Color.AQUA)
                .create();
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("potionMaterials")
    void testColorChangedForPotionMeta(Material material) {
        ItemMeta meta = new ItemStackEditor(material)
                .setColor(Color.AQUA)
                .create()
                .getItemMeta();
        Assertions.assertInstanceOf(PotionMeta.class, meta);
        PotionMeta potionMeta = (PotionMeta) meta;
        Assertions.assertEquals(Color.AQUA, potionMeta.getColor());
    }

    @ParameterizedTest
    @MethodSource("leatherArmorMaterials")
    void testColorChangedForLeatherMeta(Material material) {
        ItemMeta meta = new ItemStackEditor(material)
                .setColor(Color.AQUA)
                .create()
                .getItemMeta();
        Assertions.assertInstanceOf(LeatherArmorMeta.class, meta);
        LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
        Assertions.assertEquals(Color.AQUA, armorMeta.getColor());
    }

    @Test
    void testCustomModelDataChanged() {
        ItemMeta itemMeta = new ItemStackEditor(Material.STICK)
                .setCustomModel(1)
                .create().getItemMeta();
        Assertions.assertEquals(1, itemMeta.getCustomModelData());
    }

    @Test
    void testCustomModelDataNullIfZero() {
        ItemStack itemStack = new ItemStack(Material.STICK);
        itemStack.editMeta(meta -> meta.setCustomModelData(1));
        Assertions.assertNotNull(itemStack.getItemMeta());
        Assertions.assertTrue(itemStack.getItemMeta().hasCustomModelData());
        ItemMeta itemMeta = new ItemStackEditor(itemStack)
                .setCustomModel(0)
                .create().getItemMeta();
        Assertions.assertFalse(itemMeta.hasCustomModelData());
    }

    @Test
    void testCustomModelDataNullIfNull() {
        ItemStack itemStack = new ItemStack(Material.STICK);
        itemStack.editMeta(meta -> meta.setCustomModelData(1));
        Assertions.assertNotNull(itemStack.getItemMeta());
        Assertions.assertTrue(itemStack.getItemMeta().hasCustomModelData());
        ItemMeta itemMeta = new ItemStackEditor(itemStack)
                .setCustomModel(null)
                .create().getItemMeta();
        Assertions.assertFalse(itemMeta.hasCustomModelData());
    }

    @Test
    void testItemFlagsChanged() {
        ItemMeta meta = new ItemStackEditor(Material.STICK)
                .addFlags(ItemFlag.HIDE_ENCHANTS)
                .create().getItemMeta();
        Assertions.assertTrue(meta.getItemFlags().contains(ItemFlag.HIDE_ENCHANTS));
    }

    @Test
    void testItemMetaNotSetIfMetaTransformIsNull() {
        ItemStack expected = new ItemStack(Material.AIR);
        ItemStackEditor editor = new ItemStackEditor(Material.AIR);
        editor.setMetaConsumer(null);
        Assertions.assertEquals(expected, editor.create());
    }

    @Test
    void testItemMetaSetIfMetaTransformIsNotNull() {
        ItemStack expected = new ItemStack(Material.AIR);
        expected.setItemMeta(expected.getItemMeta());
        ItemStackEditor editor = new ItemStackEditor(Material.AIR);
        editor.setMetaConsumer(meta -> {});
        Assertions.assertEquals(expected, editor.create());
    }

}
