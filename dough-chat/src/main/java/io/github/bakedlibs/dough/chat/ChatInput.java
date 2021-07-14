package io.github.bakedlibs.dough.chat;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class ChatInput {

    static ChatInputListener listener;

    private ChatInput() {}

    /**
     * This method waits for the Player to write something in chat.
     * Afterwards the given callback will be invoked.
     * 
     * @param plugin
     *            The Plugin performing this action
     * @param p
     *            The Player that we are waiting for
     * @param handler
     *            A callback to invoke when the Player has entered some text
     */
    public static void waitForPlayer(@Nonnull Plugin plugin, @Nonnull Player p, @Nonnull Consumer<String> handler) {
        waitForPlayer(plugin, p, s -> true, handler);
    }

    /**
     * This method waits for the Player to write something in chat.
     * Afterwards the given callback will be invoked.
     * With the predicate you can filter out unwanted inputs.
     * Like commands for example.
     * 
     * @param plugin
     *            The Plugin performing this action
     * @param p
     *            The Player that we are waiting for
     * @param predicate
     *            A Filter for the messages the Player types in
     * @param handler
     *            A callback to invoke when the Player has entered some text
     */
    public static void waitForPlayer(@Nonnull Plugin plugin, @Nonnull Player p, @Nonnull Predicate<String> predicate, @Nonnull Consumer<String> handler) {
        queue(plugin, p, new ChatInputHandler() {

            @Override
            public boolean test(String msg) {
                return predicate.test(msg);
            }

            @Override
            public void onChat(Player p, String msg) {
                handler.accept(msg);
            }

        });
    }

    /**
     * This method waits for the Player to write something in chat.
     * Afterwards the given callback will be invoked.
     * 
     * @param plugin
     *            The Plugin performing this action
     * @param p
     *            The Player that we are waiting for
     * @param handler
     *            A callback to invoke when the Player has entered some text
     */
    public static void waitForPlayer(@Nonnull Plugin plugin, @Nonnull Player p, @Nonnull BiConsumer<Player, String> handler) {
        waitForPlayer(plugin, p, s -> true, handler);
    }

    /**
     * This method waits for the Player to write something in chat.
     * Afterwards the given callback will be invoked.
     * With the predicate you can filter out unwanted inputs.
     * Like commands for example.
     * 
     * @param plugin
     *            The Plugin performing this action
     * @param p
     *            The Player that we are waiting for
     * @param predicate
     *            A Filter for the messages the Player types in
     * @param handler
     *            A callback to invoke when the Player has entered some text
     */
    public static void waitForPlayer(@Nonnull Plugin plugin, @Nonnull Player p, @Nonnull Predicate<String> predicate, @Nonnull BiConsumer<Player, String> handler) {
        queue(plugin, p, new ChatInputHandler() {

            @Override
            public boolean test(String msg) {
                return predicate.test(msg);
            }

            @Override
            public void onChat(Player p, String msg) {
                handler.accept(p, msg);
            }

        });
    }

    public static void queue(@Nonnull Plugin plugin, @Nonnull Player p, @Nonnull ChatInputHandler callback) {
        if (listener == null) {
            listener = new ChatInputListener(plugin);
        }

        listener.addCallback(p.getUniqueId(), callback);
    }

}
