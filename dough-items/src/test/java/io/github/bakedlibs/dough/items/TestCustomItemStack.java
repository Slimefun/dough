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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TestCustomItemStack {

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

    private static Stream<Function<ItemStack, CustomItemStack>> itemStackConstructors() {
        return Stream.of(
                CustomItemStack::new,
                item -> new CustomItemStack(item, "test"),
                item -> new CustomItemStack(item, "test", "test"),
                item -> new CustomItemStack(item, List.of("test", "test")),
                item -> new CustomItemStack(item, Color.AQUA, "test", "test"),
                item -> new CustomItemStack(item, meta -> {
                }),
                item -> new CustomItemStack(item, item.getAmount())
        );
    }

    private static Stream<Function<String, CustomItemStack>> displayNameConstructors() {
        Material material = Material.STICK;
        ItemStack itemStack = new ItemStack(material);
        return Stream.of(
                name -> new CustomItemStack(material, name),
                name -> new CustomItemStack(itemStack, name),
                name -> new CustomItemStack(material, name, "test"),
                name -> new CustomItemStack(itemStack, name, "test"),
                name -> new CustomItemStack(material, name, List.of("test")),
                name -> new CustomItemStack(itemStack, Color.AQUA, name, "test")
        );
    }

    private static Stream<Function<Material, CustomItemStack>> materialConstructors() {
        return Stream.of(
                CustomItemStack::new,
                material -> new CustomItemStack(material, "test"),
                material -> new CustomItemStack(material, "test", "test"),
                material -> new CustomItemStack(material, "test", List.of("test")),
                material -> new CustomItemStack(material, List.of("test", "test")),
                material -> new CustomItemStack(material, meta -> {}),
                material -> new CustomItemStack(new ItemStack(Material.AIR), material)
        );
    }

    private static <T> List<T> insertNull(List<T> list) {
        List<T> ret = new ArrayList<>();
        ret.add(null);
        ret.addAll(list);
        return Collections.unmodifiableList(ret);
    }

    private static Stream<Function<List<String>, CustomItemStack>> loreListConstructors() {
        Material material = Material.STICK;
        ItemStack itemStack = new ItemStack(material);
        return Stream.of(
                lore -> new CustomItemStack(material, null, lore),
                lore -> new CustomItemStack(itemStack, insertNull(lore))
        );
    }

    private static Stream<Function<String[], CustomItemStack>> loreVarArgsConstructors() {
        Material material = Material.STICK;
        ItemStack itemStack = new ItemStack(material);
        return Stream.of(
                lore -> new CustomItemStack(material, null, lore),
                lore -> new CustomItemStack(itemStack, null, lore),
                lore -> new CustomItemStack(itemStack, Color.AQUA, null, lore)
        );
    }

    @BeforeAll
    public static void init() {
        MockBukkit.mock();
    }

    @AfterAll
    public static void teardown() {
        MockBukkit.unmock();
    }

    @Test
    void testMaterialIsChanged() {
        Material actual = new CustomItemStack(new ItemStack(Material.AIR), Material.STICK)
                .create().getType();
        Assertions.assertEquals(Material.STICK, actual);
    }

    @ParameterizedTest
    @MethodSource("materialConstructors")
    void testConstructorMaterialIsChanged(Function<Material, CustomItemStack> constructor) {
        Material actual = constructor.apply(Material.STICK).create().getType();
        Assertions.assertEquals(Material.STICK, actual);
    }

    @ParameterizedTest
    @MethodSource("itemMaterials")
    void testCanCreateCustomItemStack(Material material) {
        Assertions.assertDoesNotThrow(() -> new CustomItemStack(material));
    }

    @Test
    void testDefaultConstructorsCreateEquivalentItem() {
        ItemStack expected = new ItemStack(Material.AIR);
        CustomItemStack customItemStack = new CustomItemStack(expected);
        CustomItemStack customMaterial = new CustomItemStack(Material.AIR);
        Assertions.assertEquals(expected, customItemStack.create());
        Assertions.assertEquals(expected, customMaterial.create());
    }

    @Test
    void testCreateAppliesToClone() {
        ItemStack original = new ItemStack(Material.STICK);
        CustomItemStack customItemStack = new CustomItemStack(original);
        original.setAmount(2);
        Assertions.assertNotEquals(original, customItemStack.create());
    }

    @Test
    void testApplyTo() {
        ItemStack itemStack = new ItemStack(Material.STICK);
        ItemStack target = new ItemStack(itemStack);
        new CustomItemStack(itemStack)
                .setAmount(2)
                .applyTo(target);
        Assertions.assertEquals(2, target.getAmount());
    }

    @ParameterizedTest
    @MethodSource("itemStackConstructors")
    void testConstructorClonesInput(Function<ItemStack, CustomItemStack> constructor) {
        ItemStack itemStack = new ItemStack(Material.STICK);
        ItemStack expected = new ItemStack(itemStack);
        constructor.apply(itemStack);
        Assertions.assertEquals(expected, itemStack);
    }

    @Test
    void testNullDisplayNameAllowed() {
        ItemMeta meta = new CustomItemStack(Material.STICK)
                .setDisplayName(null)
                .create().getItemMeta();
        Assertions.assertNotNull(meta);
        Assertions.assertFalse(meta.hasDisplayName());
    }

    @ParameterizedTest
    @MethodSource("displayNameConstructors")
    void testConstructorNullDisplayNameAllowed(Function<String, CustomItemStack> constructor) {
        ItemMeta meta = constructor.apply(null).create().getItemMeta();
        Assertions.assertNotNull(meta);
        Assertions.assertFalse(meta.hasDisplayName());
    }

    @Test
    void testDisplayNameChangedAndSanitized() {
        String name = "&btest";
        String expectedName = ChatColor.translateAlternateColorCodes('&', name);
        ItemMeta meta = new CustomItemStack(Material.STICK)
                .setDisplayName(name)
                .create().getItemMeta();
        Assertions.assertNotNull(meta);
        Assertions.assertEquals(expectedName, meta.getDisplayName());
    }

    @ParameterizedTest
    @MethodSource("displayNameConstructors")
    void testConstructorDisplayNameChangedAndSanitized(Function<String, CustomItemStack> constructor) {
        String name = "&btest";
        String expectedName = ChatColor.translateAlternateColorCodes('&', name);
        ItemMeta meta = constructor.apply(name).create().getItemMeta();
        Assertions.assertNotNull(meta);
        Assertions.assertEquals(expectedName, meta.getDisplayName());
    }

    @Test
    void testLoreAsListChangedAndSanitized() {
        List<String> lore = List.of("&btest");
        List<String> expected = lore.stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
        ItemMeta meta = new CustomItemStack(Material.STICK)
                .setLore(lore)
                .create().getItemMeta();
        Assertions.assertIterableEquals(expected, meta.getLore());
    }

    @ParameterizedTest
    @MethodSource("loreListConstructors")
    void testConstructorLoreAsListChangedAndSanitized(Function<List<String>, CustomItemStack> constructor) {
        List<String> lore = List.of("&btest");
        List<String> expected = lore.stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
        ItemMeta meta = constructor.apply(lore).create().getItemMeta();
        Assertions.assertIterableEquals(expected, meta.getLore());
    }

    @Test
    void testLoreAsVarargsChangedAndSanitized() {
        List<String> lore = List.of("&btest");
        List<String> expected = lore.stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
        ItemMeta meta = new CustomItemStack(Material.STICK)
                .setLore("&btest")
                .create().getItemMeta();
        Assertions.assertIterableEquals(expected, meta.getLore());
    }

    @ParameterizedTest
    @MethodSource("loreVarArgsConstructors")
    void testConstructorLoreAsVarargsChangedAndSanitized(Function<String[], CustomItemStack> constructor) {
        List<String> lore = List.of("&btest");
        List<String> expected = lore.stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
        ItemMeta meta = constructor.apply(new String[]{"&btest"}).create().getItemMeta();
        Assertions.assertIterableEquals(expected, meta.getLore());
    }

    @Test
    void testColorUnchanged() {
        ItemStack expected = new ItemStack(Material.STICK);
        // set the color on an invalid item will still cause
        // an explcit set of the item meta
        expected.setItemMeta(expected.getItemMeta());
        ItemStack actual = new CustomItemStack(expected)
                .setColor(Color.AQUA)
                .create();
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("potionMaterials")
    void testColorChangedForPotionMeta(Material material) {
        ItemMeta meta = new CustomItemStack(material)
                .setColor(Color.AQUA)
                .create()
                .getItemMeta();
        Assertions.assertInstanceOf(PotionMeta.class, meta);
        PotionMeta potionMeta = (PotionMeta) meta;
        Assertions.assertEquals(Color.AQUA, potionMeta.getColor());
    }

    @ParameterizedTest
    @MethodSource("potionMaterials")
    void testConstructorColorChangedForPotionMeta(Material material) {
        ItemMeta meta = new CustomItemStack(new ItemStack(material), Color.AQUA, "test", "test")
                .create().getItemMeta();
        Assertions.assertInstanceOf(PotionMeta.class, meta);
        PotionMeta potionMeta = (PotionMeta) meta;
        Assertions.assertEquals(Color.AQUA, potionMeta.getColor());
    }

    @ParameterizedTest
    @MethodSource("leatherArmorMaterials")
    void testColorChangedForLeatherMeta(Material material) {
        ItemMeta meta = new CustomItemStack(material)
                .setColor(Color.AQUA)
                .create()
                .getItemMeta();
        Assertions.assertInstanceOf(LeatherArmorMeta.class, meta);
        LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
        Assertions.assertEquals(Color.AQUA, armorMeta.getColor());
    }

    @ParameterizedTest
    @MethodSource("leatherArmorMaterials")
    void testConstructorColorChangedForLeatherMeta(Material material) {
        ItemMeta meta = new CustomItemStack(new ItemStack(material), Color.AQUA, "test", "test")
                .create().getItemMeta();
        Assertions.assertInstanceOf(LeatherArmorMeta.class, meta);
        LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
        Assertions.assertEquals(Color.AQUA, armorMeta.getColor());
    }

    @Test
    void testCustomModelDataChanged() {
        ItemMeta itemMeta = new CustomItemStack(Material.STICK)
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
        ItemMeta itemMeta = new CustomItemStack(itemStack)
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
        ItemMeta itemMeta = new CustomItemStack(itemStack)
                .setCustomModel(null)
                .create().getItemMeta();
        Assertions.assertFalse(itemMeta.hasCustomModelData());
    }

    @Test
    void testItemFlagsChanged() {
        ItemMeta meta = new CustomItemStack(Material.STICK)
                .addFlags(ItemFlag.HIDE_ENCHANTS)
                .create().getItemMeta();
        Assertions.assertTrue(meta.getItemFlags().contains(ItemFlag.HIDE_ENCHANTS));
    }

}
