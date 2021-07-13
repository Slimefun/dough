package io.github.thebusybiscuit.dough.inventory.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.thebusybiscuit.dough.inventory.CustomInventory;
import io.github.thebusybiscuit.dough.inventory.InventoryLayout;
import io.github.thebusybiscuit.dough.inventory.builders.InventoryLayoutBuilder;
import io.github.thebusybiscuit.dough.inventory.builders.SlotGroupBuilder;
import io.github.thebusybiscuit.dough.inventory.payloads.InventoryClickPayload;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

class TestClickEvents {

    private static ServerMock server;
    private static CustomInventoryFactory factory;
    private static CustomInventoryFactory factory2;

    @BeforeAll
    static void setup() {
        server = MockBukkit.mock();
        factory = new MockInventoryFactory();
        factory2 = new MockInventoryFactory();
    }

    @AfterAll
    static void teardown() {
        MockBukkit.unmock();
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void testClickItemInSlot(boolean interactable) {
        AtomicReference<InventoryClickPayload> payloadRef = new AtomicReference<>();

        // @formatter:off
        InventoryLayout layout = new InventoryLayoutBuilder(9)
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .withSlots(0, 1)
                    .interactable(interactable)
                    .withDefaultItem(new ItemStack(Material.APPLE))
                    .onClick(payloadRef::set)
                    .build()
            )
            .addSlotGroup(
                new SlotGroupBuilder('y', "test2")
                    .withSlots(2, 3, 4, 5, 6, 7, 8)
                    .build()
            )
            .build();
        // @formatter:on

        assertListenerRegistered();

        CustomInventory inv = factory.createInventory(layout);
        Player player = server.addPlayer();
        int slot = 1;

        InventoryView view = inv.open(player);
        InventoryClickEvent event = new InventoryClickEvent(view, SlotType.CONTAINER, slot, ClickType.LEFT, InventoryAction.PICKUP_ONE);

        Bukkit.getPluginManager().callEvent(event);
        InventoryClickPayload payload = payloadRef.get();

        assertNotNull(payload);
        assertSame(inv, payload.getInventory());
        assertSame(player, payload.getPlayer());

        assertEquals(slot, payload.getClickedSlot());
        assertEquals(layout.getGroup("test"), payload.getClickedSlotGroup());
        assertEquals(new ItemStack(Material.APPLE), payload.getClickedItemStack());

        assertNotEquals(interactable, event.isCancelled());
    }

    @Test
    void testMultipleFactories() {
        Map<CustomInventoryFactory, Integer> eventsFired = new HashMap<>();

        // @formatter:off
        InventoryLayout layout = new InventoryLayoutBuilder(9)
            .addSlotGroup(
                new SlotGroupBuilder('x', "test")
                    .withSlots(0, 1, 2, 3, 4, 5, 6, 7, 8)
                    .interactable(true)
                    .onClick(payload -> {
                        CustomInventoryFactory factory = payload.getInventory().getFactory();
                        eventsFired.merge(factory, 1, Integer::sum);
                    })
                    .build()
            )
            .build();
        // @formatter:on

        CustomInventory inv = factory.createInventory(layout);
        CustomInventory inv2 = factory2.createInventory(layout);

        simulateClickEvents(inv, 2);
        simulateClickEvents(inv2, 4);

        assertEquals(2, eventsFired.get(factory));
        assertEquals(4, eventsFired.get(factory2));
    }

    @ParametersAreNonnullByDefault
    private void simulateClickEvents(CustomInventory inv, int amount) {
        for (int i = 0; i < amount; i++) {
            Player player = server.addPlayer();
            InventoryView view = inv.open(player);
            InventoryClickEvent event = new InventoryClickEvent(view, SlotType.CONTAINER, 1, ClickType.LEFT, InventoryAction.PICKUP_ONE);

            Bukkit.getPluginManager().callEvent(event);
            player.closeInventory();
        }
    }

    /**
     * This method asserts that the {@link Listener} for our {@link CustomInventoryFactory}
     * is properly registered to the {@link Server} and listens to the {@link InventoryClickEvent}.
     */
    private void assertListenerRegistered() {
        // @formatter:off
        assertEquals(1,Arrays.stream(InventoryClickEvent.getHandlerList().getRegisteredListeners())
            .filter(listener -> {
                Plugin plugin = listener.getPlugin();
                Listener clickListener = listener.getListener();
    
                if (plugin.equals(factory.getPlugin()) && clickListener instanceof CustomInventoryListener) {
                    return factory.equals(((CustomInventoryListener) clickListener).getInventoryFactory());
                } else {
                    return false;
                }
            }).count()
        );
        // @formatter:off
    }

}
