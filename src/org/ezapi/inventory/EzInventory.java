package org.ezapi.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import org.ezapi.chat.ChatMessage;
import org.ezapi.function.NonReturnWithOne;
import org.ezapi.function.NonReturnWithTwo;

import java.util.*;
import java.util.function.Function;

public final class EzInventory implements Listener {

    private final int lines;

    private final String id;

    private boolean enableTitle = false;

    private ChatMessage title = new ChatMessage("", false);

    private final Map<Integer,Input> items = new HashMap<>();

    private Function<Player, List<Input>> dynamicInput = null;

    private final Map<Player,Map<Integer,Input>> cache = new HashMap<>();

    private NonReturnWithTwo<EzInventory, Player> onClose = EzInventory::defaultOnClose;

    private boolean dropped = false;

    /**
     * Inventory API inventory
     *
     * @param plugin your plugin instance
     * @param lineAmount how many lines does the inventory have, from 1 to 6
     */
    public EzInventory(Plugin plugin, int lineAmount) {
        if (lineAmount < 1) lineAmount = 1;
        if (lineAmount > 6) lineAmount = 6;
        this.lines = lineAmount;
        String id = plugin.getName() + "_" + new Random().nextInt(1000000);
        this.id = id;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Inventory API inventory
     *
     * @param plugin your plugin instance
     * @param lineAmount how many lines does the inventory have, from 1 to 6
     * @param title inventory title
     */
    public EzInventory(Plugin plugin, int lineAmount, ChatMessage title) {
        if (lineAmount < 1) lineAmount = 1;
        if (lineAmount > 6) lineAmount = 6;
        this.enableTitle = true;
        this.title = title;
        this.lines = lineAmount;
        String id = plugin.getName() + "_" + new Random().nextInt(1000000);
        this.id = id;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Open the inventory to the player
     * @param player player
     */
    public void openToPlayer(Player player) {
        if (isDropped()) return;
        EzHolder ezHolder = new EzHolder(id);
        Inventory inventory = Bukkit.createInventory(ezHolder, lines * 9);
        if (enableTitle) inventory = Bukkit.createInventory(ezHolder, lines * 9, title.getText(player));
        ezHolder.setInventory(inventory);
        cache.put(player, new HashMap<>());
        for (int i = 0; i < 9 * lines; i ++) {
            if (items.get(i) instanceof Drawer) {
                DrawSetting drawSetting = new DrawSetting(i);
                ((Drawer) items.get(i)).onDraw(player, drawSetting);
                inventory.setItem(i, drawSetting.render(player));
                cache.get(player).put(i, items.get(i));
            }
        }
        if (dynamicInput != null) {
            List<Input> list = new ArrayList<>(dynamicInput.apply(player));
            List<Integer> empty = new ArrayList<>();
            for (int i = 0; i < 9 * lines; i++) {
                if (!items.containsKey(i)) {
                    empty.add(i);
                }
            }
            for (int i = 0; i < (Math.min(empty.size(), list.size())); i++) {
                Input input = list.get(i);
                if (input instanceof Drawer) {
                    DrawSetting drawSetting = new DrawSetting(-1);
                    ((Drawer) input).onDraw(player, drawSetting);
                    ItemStack itemStack = drawSetting.render(player);
                    inventory.setItem(empty.get(i), itemStack);
                }
                cache.get(player).put(empty.get(i), input);
            }
        }
        player.openInventory(inventory);
    }

    /**
     * Set slot to input
     *
     * @param slot slot
     * @param input input
     */
    public void set(int slot, Input input) {
        if (isDropped()) return;
        items.put(slot, input);
    }

    /**
     * Get if slot has item
     * @param slot slot
     * @return has item
     */
    public boolean has(int slot) {
        if (isDropped()) return false;
        return items.containsKey(slot);
    }

    /**
     * Get input of the slot
     * @param slot slot
     * @return input
     */
    public Input get(int slot) {
        if (isDropped()) return null;
        return items.get(slot);
    }

    /**
     * Remove a input
     * @param slot slot
     * @return input
     */
    public Input remove(int slot) {
        if (isDropped()) return null;
        return items.remove(slot);
    }

    /**
     * Set dynamic input
     * <p>Return different input list by different players</p>
     * <p>All inputs will be put on slots witch are empty</p>
     * <p>It's used to make multi pages gui normally</p>
     *
     * @param function provide a player to get a inputs list
     */
    public void setDynamicInput(Function<Player, List<Input>> function) {
        if (isDropped()) return;
        this.dynamicInput = function;
    }

    /**
     * Remove and cancel dynamic inputs
     */
    public void removeDynamicInput() {
        if (isDropped()) return;
        this.dynamicInput = null;
    }

    /**
     * Fill the inventory with input
     * @param input input
     */
    public void fill(Input input) {
        if (isDropped()) return;
        for (int i = 0; i < lines * 9; i++) {
            set(i, input);
        }
    }

    /**
     * Draw a line with input
     * @param witchLine witch line
     * @param input input
     */
    public void drawALine(int witchLine, Input input) {
        if (isDropped()) return;
        if (witchLine < 1) witchLine = 1;
        if (witchLine > lines) witchLine = lines;
        for (int i = (witchLine * 9) - 9; i < (witchLine * 9); i++) {
            set(i, input);
        }
    }

    /**
     * Draw a vertical line with input
     * @param witchVerticalLine witch vertical line
     * @param input input
     */
    public void drawAVerticalLine(int witchVerticalLine, Input input) {
        if (isDropped()) return;
        if (witchVerticalLine < 1) witchVerticalLine = 1;
        if (witchVerticalLine > 9) witchVerticalLine = 9;
        for (int i = witchVerticalLine - 1; i < lines * 9; i += 9) {
            set(i, input);
        }
    }

    /**
     * Draw a circle with input
     * @param input input
     */
    public void drawACircle(Input input) {
        if (isDropped()) return;
        if (lines >= 3) {
            drawALine(1, input);
            drawALine(lines, input);
            drawAVerticalLine(1, input);
            drawAVerticalLine(9, input);
        } else {
            fill(input);
        }
    }

    /**
     * Drop the inventory
     */
    public void drop() {
        if (!dropped) {
            if (!this.cache.isEmpty()) {
                for (Player player : cache.keySet()) {
                    player.closeInventory();
                }
            }
            HandlerList.unregisterAll(this);
            dropped = true;
        }
    }

    /**
     * Get if the inventory is dropped
     * @return is dropped
     */
    public boolean isDropped() {
        return dropped;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (isDropped()) return;
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (event.getInventory().getHolder() instanceof EzHolder) {
                EzHolder ezHolder = (EzHolder) event.getInventory().getHolder();
                if (ezHolder.getId().equalsIgnoreCase(this.id)) {
                    if (Objects.equals(event.getClickedInventory(), event.getView().getTopInventory())) {
                        event.setCancelled(true);
                        if (cache.get(player).containsKey(event.getRawSlot())) {
                            Input input = cache.get(player).get(event.getRawSlot());
                            if (input instanceof Click) {
                                ((Click) input).onClick(player, event.getClick(), event.getAction());
                            }
                        }
                    }
                    if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (isDropped()) return;
        if (event.getPlayer() instanceof Player) {
            if (event.getInventory().getHolder() instanceof EzHolder) {
                EzHolder ezHolder = (EzHolder) event.getInventory().getHolder();
                if (ezHolder.getId().equalsIgnoreCase(this.id)) {
                    this.cache.remove((Player) event.getPlayer());
                    this.onClose.apply(this, (Player) event.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (isDropped()) return;
        if (this.cache.containsKey(event.getPlayer())) {
            this.cache.remove(event.getPlayer());
            this.onClose.apply(this, event.getPlayer());
        }
    }

    private static void defaultOnClose(EzInventory ezInventory, Player player) {}

    /**
     * Set event on player close the inventory
     * @param nonReturn function
     */
    public void setOnClose(NonReturnWithTwo<EzInventory, Player> nonReturn) {
        if (isDropped()) return;
        onClose = nonReturn;
    }

}
