package io.github.thebusybiscuit.cscorelib2.chat.json;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
/**
 * 
 * @deprecated Honestly, Kyori-adventure is infinite times better than this, please use that.
 *
 */
@Deprecated
public class CustomBookInterface {

    private static CustomBookListener listener;

    private static Field pagesField;
    private static Method playerHandle;
    private static Method openBook;
    private static Method copyBook;
    private static Object mainHand;

    static {
        try {
            Class<?> metaClass = ReflectionUtils.getOBCClass("inventory.CraftMetaBook");
            pagesField = ReflectionUtils.getField(metaClass, "pages");
            pagesField.setAccessible(true);

            if (ReflectionUtils.isVersion("v1_13_")) {
                copyBook = ReflectionUtils.getMethod(ReflectionUtils.getOBCClass("inventory.CraftItemStack"), "asNMSCopy", ItemStack.class);
                playerHandle = ReflectionUtils.getOBCClass("entity.CraftPlayer").getMethod("getHandle");

                Class<?> enumhand = ReflectionUtils.getNMSClass("EnumHand");
                openBook = ReflectionUtils.getMethod(ReflectionUtils.getNMSClass("EntityPlayer"), "a", ReflectionUtils.getNMSClass("ItemStack"), enumhand);
                mainHand = ReflectionUtils.getEnumConstant(enumhand, "MAIN_HAND");
            }
        } catch (ClassNotFoundException | SecurityException | NoSuchFieldException | NoSuchMethodException e) {
            System.err.println("Perhaps you forgot to shade CS-CoreLib's \"reflection\" package?");
            e.printStackTrace();
        }
    }

    private final Plugin plugin;

    @Getter
    @Setter
    private String author;

    @Getter
    @Setter
    private String title = "Book";

    @Getter
    private final List<ChatComponent> pages = new LinkedList<>();

    @Getter
    private final Map<NamespacedKey, Consumer<Player>> clickables = new HashMap<>();

    public CustomBookInterface(@NonNull Plugin plugin) {
        if (listener == null) {
            listener = new CustomBookListener(plugin);
        }

        this.plugin = plugin;
        author = plugin.getName();
    }

    public void addPage(@NonNull ChatComponent page) {
        pages.add(page);
        clickables.putAll(page.getClickables());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ItemStack getItem() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);

        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setTitle(title);
        meta.setAuthor(author);

        List field;
        try {
            field = (List) pagesField.get(meta);
            Validate.notNull(field, "'pages' field seems to be null for BookMeta");

            for (ChatComponent page : pages) {
                field.add(page.getAsNMSComponent());
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not access pages of a book", e);
        }

        book.setItemMeta(meta);
        return book;
    }

    public void open(@NonNull Player p) {
        if (listener.getPlayers().contains(p.getUniqueId()))
            return;

        ItemStack book = getItem();

        listener.getBooks().put(p.getUniqueId(), this);

        if (!ReflectionUtils.isVersion("v1_13_")) {
            p.openBook(book);
        } else {
            int slot = p.getInventory().getHeldItemSlot();
            ItemStack item = p.getInventory().getItem(slot);

            listener.getPlayers().add(p.getUniqueId());
            p.getInventory().setItem(slot, book);

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                try {
                    Object copy = copyBook.invoke(null, book);
                    openBook.invoke(playerHandle.invoke(p), copy, mainHand);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    plugin.getLogger().log(Level.SEVERE, "Could not open a Written Book", e);
                }

                // If something goes wrong, still give the item back and release the Player
                p.getInventory().setItem(slot, item);
                listener.getPlayers().remove(p.getUniqueId());
            }, 1L);
        }
    }

}
