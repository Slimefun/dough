package io.github.thebusybiscuit.cscorelib2.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;
import lombok.Setter;

public class ChestMenu implements Cloneable, Iterable<ItemStack> {
	
	private static ChestMenuListener listener;
	
	@Getter
	protected boolean playerInventoryClickable;
	
	@Getter
	protected Plugin plugin;

	@Getter
	protected Consumer<Player> menuOpeningHandler;
	
	@Getter
	protected Consumer<Player> menuClosingHandler;

	@Getter
	protected String title;
	
	protected boolean emptyClickable;
	protected Inventory inv;
	protected List<ItemStack> items;
	protected Map<Integer, MenuClickHandler> handlers;
	protected MenuClickHandler playerclick;
	protected Predicate<ItemStack> predicate;
	
	@Getter @Setter
	protected int size;
	
	protected int timeout;
	protected Runnable deprecationRunnable;
	protected BukkitTask deprecationTask;
	protected Runnable dirtyRunnable;
	
	/**
	 * Creates a new ChestMenu with the specified
	 * Title and a {@link Runnable} for dirty-marking
	 *
	 * @param  plugin The referencing plugin
	 * @param  title The title of the Menu
	 * @param  dirtyRunnable A {@link Runnable} that is run when the Inventory was modified
	 */ 
	public ChestMenu(Plugin plugin, String title, Runnable dirtyRunnable) {
		if (listener == null) listener = new ChestMenuListener(plugin);
		
		this.plugin = plugin;
		this.title = ChatColor.translateAlternateColorCodes('&', title);
		this.playerInventoryClickable = true;
		this.emptyClickable = true;
		this.items = new LinkedList<>();
		this.handlers = new HashMap<>();
		
		this.timeout = -1;
		this.dirtyRunnable = dirtyRunnable;
		
		this.menuOpeningHandler = (p) -> {};
		this.menuClosingHandler = (p) -> {};
		this.playerclick = (p, slot, item, cursor, action) -> isPlayerInventoryClickable();
	}

	/**
	 * Creates a new ChestMenu with a specified Title
	 *
	 * @param plugin The referencing plugin
	 * @param title The title of this Menu
	 */
	public ChestMenu(Plugin plugin, String title) {
		this(plugin, title, () -> {});
	}
	
	protected ChestMenu(ChestMenu menu) {
		this.plugin = menu.plugin;
		this.title = menu.title;
		this.playerInventoryClickable = menu.playerInventoryClickable;
		this.emptyClickable = menu.emptyClickable;
		this.items = menu.items;
		this.handlers = menu.handlers;
		this.timeout = menu.timeout;
		
		this.menuOpeningHandler = menu.menuOpeningHandler;
		this.menuClosingHandler = menu.menuClosingHandler;
		this.playerclick = menu.playerclick;
	}
	
	/**
	 * This method checks whether an Instance of {@link ItemStack} is forbidden to be inserted.
	 * @see ChestMenu#preventItems(Predicate)
	 * 
	 * @param item The {@link ItemStack} to be checked.
	 * @return Whether this type of Item was prevented.
	 */
	public boolean preventsItems(ItemStack item) {
		if (item == null || predicate == null) return false;
		
		return predicate.test(item);
	}
	
	/**
	 * This method forbids Items that pass the Test from Insertion into this Inventory.
	 * If the Predicate returns true, the Item cannot be inserted.
	 * 
	 * @param predicate An {@link ItemStack} {@link Predicate}
	 */
	public void preventItems(Predicate<ItemStack> predicate) {
		this.predicate = predicate;
	}
	
	/**
	 * This method marks the Inventory as dirty.
	 * It will run the {@link Runnable} defined in the constructor.
	 */
	public void markDirty() {
		if (dirtyRunnable != null) dirtyRunnable.run();
	}
	
	/**
	 * Toggles whether Players can access there
	 * Inventory while viewing this Menu
	 *
	 * @param  clickable Whether the Player can access his Inventory
	 */ 
	public void setPlayerInventoryClickable(boolean clickable) {
		this.playerInventoryClickable = clickable;
	}
	
	/**
	 * This method returns all Click Events for this Inventory.
	 * You can clear this Map if you want to remove all {@link MenuClickHandler}
	 * from this {@link ChestMenu}
	 * 
	 * @return A Map containing all Click Events
	 */
	public Map<Integer, MenuClickHandler> getClickHandlers() {
		return handlers;
	}
	
	/**
	 * Toggles whether Players can click the
	 * empty menu slots while viewing this Menu
	 *
	 * @param  emptyClickable Whether the Player can click empty slots
	 */ 
	public void setEmptySlotsClickable(boolean emptyClickable) {
		this.emptyClickable = emptyClickable;
	}
	
	/**
	 * Returns whether the empty menu slots are
	 * clickable while viewing this Menu
	 *
	 * @return      Whether the empty menu slots are clickable
	 */
	public boolean areEmptySlotsClickable() {
		return emptyClickable;
	}
	
	/**
	 * Sets a ClickHandler to ALL Slots of the
	 * Player's Inventory
	 *
	 * @param  handler The MenuClickHandler
	 */ 
	public void setPlayerInventoryClickHandler(MenuClickHandler handler) {
		this.playerclick = handler;
	}
	
	/**
	 * Adds an Item to the Inventory in that Slot
	 *
	 * @param  slot The Slot in the Inventory
	 * @param  item The Item for that Slot
	 * @return      The ChestMenu Instance
	 */ 
	public ChestMenu addItem(int slot, ItemStack item) {
		if (size > slot) {
			this.items.set(slot, item);
			
			if (inv != null) {
				inv.setItem(slot, item);
			}
		}
		else {
			for (int i = 0; i < slot - size; i++) {
				this.items.add(null);
			}
			this.items.add(item);
			
			this.size = slot + 1;
		}
		return this;
	}
	
	/**
	 * Adds an Item to the Inventory in that Slot
	 * as well as a {@link MenuClickHandler}
	 *
	 * @param  slot The Slot in the Inventory
	 * @param  item The Item for that Slot
	 * @param  handler The MenuClickHandler for that Slot
	 * @return      The ChestMenu Instance
	 */ 
	public ChestMenu addItem(int slot, ItemStack item, MenuClickHandler handler) {
		addItem(slot, item);
		addMenuClickHandler(slot, handler);
		return this;
	}
	
	/**
	 * This method clears the {@link ItemStack} and {@link MenuClickHandler} associated
	 * with the specified slot.
	 * 
	 * @param slot The Slot to be removed
	 * @return The current ChestMenu Instance
	 */
	public ChestMenu remove(int slot) {
		items.set(slot, null);
		handlers.remove(slot);
		
		return this;
	}
	
	/**
	 * Returns the {@link ItemStack} in that Slot
	 *
	 * @param  slot The Slot in the Inventory
	 * @return      The ItemStack in that Slot
	 */ 
	public ItemStack getItemInSlot(int slot) {
		setup();
		return this.inv.getItem(slot);
	}
	
	/**
	 * Executes a certain Action upon clicking an
	 * Item in the Menu
	 *
	 * @param  slot The Slot in the Inventory
	 * @param  handler The {@link MenuClickHandler}
	 * @return      The ChestMenu Instance
	 */ 
	public ChestMenu addMenuClickHandler(int slot, MenuClickHandler handler) {
		this.handlers.put(slot, handler);
		return this;
	}
	
	/**
	 * Executes a certain Action upon opening
	 * this Menu
	 *
	 * @param  handler	The Action that shall be run
	 * @return      	The ChestMenu Instance
	 */ 
	public ChestMenu addMenuOpeningHandler(Consumer<Player> handler) {
		this.menuOpeningHandler = handler;
		return this;
	}
	
	/**
	 * Executes a certain Action upon closing
	 * this Menu
	 *
	 * @param  handler 	The Action that shall be run
	 * @return      	The ChestMenu Instance
	 */ 
	public ChestMenu addMenuCloseHandler(Consumer<Player> handler) {
		this.menuClosingHandler = handler;
		return this;
	}
	
	/**
	 * Returns an Array containing the Contents
	 * of this Inventory
	 *
	 * @return      The Contents of this Inventory
	 */ 
	public ItemStack[] getContents() {
		setup();
		return this.inv.getContents();
	}
	
	protected void setup() {
		if (inv != null) return;
		inv = Bukkit.createInventory(null, formToLine(size) * 9, title);
		
		int i = 0;
		for (ItemStack item: items) {
			this.inv.setItem(i, item);
			i++;
		}
	}
	
	/**
	 * Resets this ChestMenu to a Point BEFORE the User interacted with it
	 *
	 * @param update Update current inv
	 */ 
	public void reset(boolean update) {
		if (update) inv.clear();
		else inv = Bukkit.createInventory(null, formToLine(size) * 9, title);

		int i = 0;
		for (ItemStack item: items) {
			this.inv.setItem(i, item);
			i++;
		}
	}
	
	/**
	 * Modifies an ItemStack in an ALREADY OPENED ChestMenu
	 *
	 * @param  slot The Slot of the Item which will be replaced
	 * @param  item The new Item
	 */ 
	public void replaceExistingItem(int slot, ItemStack item) {
		setup();
		this.inv.setItem(slot, item);
	}
	
	/**
	 * Opens this Menu for the specified Player/s
	 * 
	 * @param players The Players who will see this Menu
	 */ 
	public void open(Player... players) {
		if (players.length == 0) return;
		
		setup();
		if (deprecationTask != null) deprecationTask.cancel();
		
		for (Player p: players) {
			p.openInventory(this.inv);
			listener.menus.put(p.getUniqueId(), this);
			
			menuOpeningHandler.accept(p);
		}
	}
	
	/**
	 * Returns the MenuClickHandler which was registered for the specified Slot
	 *
	 * @param  slot The Slot in the Inventory
	 * @return      The MenuClickHandler registered for the specified Slot
	 */ 
	public MenuClickHandler getMenuClickHandler(int slot) {
		return handlers.get(slot);
	}

	/**
	 * Returns the registered MenuClickHandler
	 * for Player Inventories
	 *
	 * @return      The registered MenuClickHandler
	 */ 
	public MenuClickHandler getPlayerInventoryClickHandler() {
		return playerclick;
	}
	
	/**
	 * Converts this ChestMenu Instance into a
	 * normal Inventory
	 *
	 * @return      The converted Inventory
	 */ 
	public Inventory toInventory() {
		setup();
		return this.inv;
	}
	
	/**
	 * This method closes the Inventory for all instances of {@link Player}
	 * currently seeing the ChestMenu
	 */
	public void close() {
		if (inv == null) return;
		
		Iterator<HumanEntity> iterator = new ArrayList<>(inv.getViewers()).iterator();
		
		while (iterator.hasNext()) {
			iterator.next().closeInventory();
		}
	}
	
	protected static int formToLine(int i) {
		return (int) Math.ceil(i / 9f);
	}

	/**
	 * This Method will consume the Item in the specified slot.
	 * See {@link ChestMenu#consumeItem(int, int, boolean)} for further details.
	 * 
	 * @param slot 					The Slot in which the Item should be consumed
	 * @param replaceConsumables 	Whether Consumable Items should be replaced with their "empty" version, see {@link ChestMenu#consumeItem(int, int, boolean)}
	 */
	public void consumeItem(int slot, boolean replaceConsumables) {
		consumeItem(slot, 1, replaceConsumables);
	}
	
	/**
	 * This Method consumes a specified amount of items from the
	 * specified slot.
	 * 
	 * The items will be removed from the slot, if the slot does not hold enough items,
	 * it will be replaced with null.
	 * Note that this does not check whether there are enough Items present,
	 * if you specify a bigger amount than present, it will simply set the Item to null.
	 * 
	 * If replaceConsumables is true, the following things will not be replaced with 'null':
	 * {@code Buckets -> new ItemStack(Material.BUCKET)}
	 * {@code Potions -> new ItemStack(Material.GLASS_BOTTLE)}
	 * 
	 * @param slot					The Slot in which to remove the Item
	 * @param amount				How many Items should be removed
	 * @param replaceConsumables	Whether Items should be replaced with their "empty" version
	 */
	public void consumeItem(int slot, int amount, boolean replaceConsumables) {
		ItemStack item = getItemInSlot(slot);
		
		if (item != null && item.getType() != Material.AIR) {
			InvUtils.consumeItem(toInventory(), slot, amount, replaceConsumables);
			
			markDirty();
		}
	}
	
	/**
	 * This method tries to push the specified {@link ItemStack} into
	 * the specified slots.
	 * 
	 * @param item		The Item that shall be added to the Inventory
	 * @param slots		The Slots in which to try pushing it.
	 * @return			Whether it was successful
	 */
	public boolean pushItem(ItemStack item, int... slots) {
		for (int slot: slots) {
			ItemStack stack = getItemInSlot(slot);
			
			if (stack == null || stack.getType() == Material.AIR) {
				toInventory().setItem(slot, item);
				markDirty();
				return true;
			}
			else if (stack.getAmount() + item.getAmount() <= stack.getMaxStackSize() && ItemUtils.canStack(stack, item)) {
				stack.setAmount(stack.getAmount() + item.getAmount());
				toInventory().setItem(slot, stack);
				markDirty();
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * This method checks if an Item can fit into the specified slots.
	 * Note that this also checks {@link ItemStack#getAmount()}
	 * 
	 * @param item		The Item that shall be tested for
	 * @param slots		The Slots that shall be iterated over
	 * @return			Whether the slots have space for the {@link ItemStack}
	 */
	public boolean fits(ItemStack item, int... slots) {
		return InvUtils.fits(toInventory(), item, slots);
	}

	@Override
	public Iterator<ItemStack> iterator() {
		setup();
		return inv.iterator();
	}
	
	/**
	 * This will run the specified (deprecation-) runnable, if the ChestMenu
	 * has not been opened by anyone for the specified amount of ticks.
	 * 
	 * The Task will restart whenever someone re-opens the Menu.
	 * 
	 * @param ticks		How long to wait after the last Person closed the Inventory
	 * @param runnable	The Runnable to run when this happens
	 */
	public void setTimeout(int ticks, Runnable runnable) {
		if (deprecationTask != null) deprecationTask.cancel();
			
		this.timeout = ticks;
		this.deprecationRunnable = runnable;
	}
	
	protected void runTimeout() {
		if (timeout >= 0) {
			if (toInventory().getViewers().size() <= 1) {
				deprecationTask = plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
					this.close();
					deprecationRunnable.run();
					
					deprecationTask = null;
				}, timeout);
			}
		}
	}

	protected void onClose(Player p) {
		menuClosingHandler.accept(p);
		runTimeout();
	}
	
	@Override
	public ChestMenu clone() {
		return new ChestMenu(this);
	}
}
