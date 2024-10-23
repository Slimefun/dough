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

    @BeforeAll
    public static void init() {
        MockBukkit.mock();
    }

    @AfterAll
    public static void teardown() {
        MockBukkit.unmock();
    }

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

    private static Stream<Function<ItemStack, ItemStack>> itemStackConstructors() {
        return Stream.of(
                item -> CustomItemStack.create(item, "test"),
                item -> CustomItemStack.create(item, "test", "test"),
                item -> CustomItemStack.create(item, List.of("test", "test")),
                item -> CustomItemStack.create(item, Color.AQUA, "test", "test"),
                item -> CustomItemStack.create(item, meta -> {
                }),
                item -> CustomItemStack.create(item, item.getAmount())
        );
    }

    private static Stream<Function<String, ItemStack>> displayNameConstructors() {
        Material material = Material.STICK;
        ItemStack itemStack = new ItemStack(material);
        return Stream.of(
                name -> CustomItemStack.create(material, name),
                name -> CustomItemStack.create(itemStack, name),
                name -> CustomItemStack.create(material, name, "test"),
                name -> CustomItemStack.create(itemStack, name, "test"),
                name -> CustomItemStack.create(material, name, List.of("test")),
                name -> CustomItemStack.create(itemStack, Color.AQUA, name, "test")
        );
    }

    private static Stream<Function<Material, ItemStack>> materialConstructors() {
        return Stream.of(
                material -> CustomItemStack.create(material, "test"),
                material -> CustomItemStack.create(material, "test", "test"),
                material -> CustomItemStack.create(material, "test", List.of("test")),
                material -> CustomItemStack.create(material, List.of("test", "test")),
                material -> CustomItemStack.create(material, meta -> {}),
                material -> CustomItemStack.create(new ItemStack(Material.AIR), material)
        );
    }

    private static <T> List<T> insertNull(List<T> list) {
        List<T> ret = new ArrayList<>();
        ret.add(null);
        ret.addAll(list);
        return Collections.unmodifiableList(ret);
    }

    private static Stream<Function<List<String>, ItemStack>> loreListConstructors() {
        Material material = Material.STICK;
        ItemStack itemStack = new ItemStack(material);
        return Stream.of(
                lore -> CustomItemStack.create(material, null, lore),
                lore -> CustomItemStack.create(itemStack, insertNull(lore))
        );
    }

    private static Stream<Function<String[], ItemStack>> loreVarArgsConstructors() {
        Material material = Material.STICK;
        ItemStack itemStack = new ItemStack(material);
        return Stream.of(
                lore -> CustomItemStack.create(material, null, lore),
                lore -> CustomItemStack.create(itemStack, null, lore),
                lore -> CustomItemStack.create(itemStack, Color.AQUA, null, lore)
        );
    }

    @Test
    void testMaterialIsChanged() {
        Material actual = CustomItemStack.create(new ItemStack(Material.AIR), Material.STICK).getType();
        Assertions.assertEquals(Material.STICK, actual);
    }

    @ParameterizedTest
    @MethodSource("materialConstructors")
    void testConstructorMaterialIsChanged(Function<Material, ItemStack> constructor) {
        Material actual = constructor.apply(Material.STICK).getType();
        Assertions.assertEquals(Material.STICK, actual);
    }

    @ParameterizedTest
    @MethodSource("itemStackConstructors")
    void testConstructorClonesInput(Function<ItemStack, ItemStack> constructor) {
        ItemStack itemStack = new ItemStack(Material.STICK);
        ItemStack expected = new ItemStack(itemStack);
        constructor.apply(itemStack);
        Assertions.assertEquals(expected, itemStack);
    }

    @ParameterizedTest
    @MethodSource("displayNameConstructors")
    void testConstructorNullDisplayNameAllowed(Function<String, ItemStack> constructor) {
        ItemMeta meta = constructor.apply(null).getItemMeta();
        Assertions.assertNotNull(meta);
        Assertions.assertFalse(meta.hasDisplayName());
    }

    @ParameterizedTest
    @MethodSource("displayNameConstructors")
    void testConstructorDisplayNameChangedAndSanitized(Function<String, ItemStack> constructor) {
        String name = "&btest";
        String expectedName = ChatColor.translateAlternateColorCodes('&', name);
        ItemMeta meta = constructor.apply(name).getItemMeta();
        Assertions.assertNotNull(meta);
        Assertions.assertEquals(expectedName, meta.getDisplayName());
    }

    @ParameterizedTest
    @MethodSource("loreListConstructors")
    void testConstructorLoreAsListChangedAndSanitized(Function<List<String>, ItemStack> constructor) {
        List<String> lore = List.of("&btest");
        List<String> expected = lore.stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
        ItemMeta meta = constructor.apply(lore).getItemMeta();
        Assertions.assertIterableEquals(expected, meta.getLore());
    }

    @ParameterizedTest
    @MethodSource("loreVarArgsConstructors")
    void testConstructorLoreAsVarargsChangedAndSanitized(Function<String[], ItemStack> constructor) {
        List<String> lore = List.of("&btest");
        List<String> expected = lore.stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
        ItemMeta meta = constructor.apply(new String[]{"&btest"}).getItemMeta();
        Assertions.assertIterableEquals(expected, meta.getLore());
    }

    @ParameterizedTest
    @MethodSource("potionMaterials")
    void testConstructorColorChangedForPotionMeta(Material material) {
        ItemMeta meta = CustomItemStack.create(new ItemStack(material), Color.AQUA, "test", "test")
                .getItemMeta();
        Assertions.assertInstanceOf(PotionMeta.class, meta);
        PotionMeta potionMeta = (PotionMeta) meta;
        Assertions.assertEquals(Color.AQUA, potionMeta.getColor());
    }

    @ParameterizedTest
    @MethodSource("leatherArmorMaterials")
    void testConstructorColorChangedForLeatherMeta(Material material) {
        ItemMeta meta = CustomItemStack.create(new ItemStack(material), Color.AQUA, "test", "test")
                .getItemMeta();
        Assertions.assertInstanceOf(LeatherArmorMeta.class, meta);
        LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
        Assertions.assertEquals(Color.AQUA, armorMeta.getColor());
    }

}
