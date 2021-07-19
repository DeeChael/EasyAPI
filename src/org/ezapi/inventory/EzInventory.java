package org.ezapi.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.ezapi.EasyAPI;
import org.ezapi.chat.ChatMessage;
import org.ezapi.function.NonReturnWithOne;

import java.util.*;
import java.util.function.Function;

public class EzInventory implements Listener {

    private Plugin plugin;

    private final int lines;

    private final String id;

    private boolean enableTitle = false;

    private ChatMessage title = new ChatMessage("", false);

    private final Map<Integer,Input> items = new HashMap<>();

    private Function<Player, List<Input>> dynamicInput = null;

    private final Map<Player,Map<Integer,Input>> cache = new HashMap<>();

    private NonReturnWithOne<EzInventory> onClose = EzInventory::defaultOnClose;

    public EzInventory(Plugin plugin, int lineAmount) {
        if (lineAmount < 1) lineAmount = 1;
        if (lineAmount > 6) lineAmount = 6;
        this.lines = lineAmount;
        String id = plugin.getName() + "_" + new Random().nextInt(1000000);
        this.id = id;
        Bukkit.getPluginManager().registerEvents(this, EasyAPI.getInstance());
    }

    public EzInventory(Plugin plugin, int lineAmount, ChatMessage title) {
        if (lineAmount < 1) lineAmount = 1;
        if (lineAmount > 6) lineAmount = 6;
        this.enableTitle = true;
        this.title = title;
        this.lines = lineAmount;
        String id = plugin.getName() + "_" + new Random().nextInt(1000000);
        this.id = id;
        Bukkit.getPluginManager().registerEvents(this, EasyAPI.getInstance());
    }

    public void openToPlayer(Player player) {
        EzHolder ezHolder = new EzHolder(id);
        Inventory inventory = Bukkit.createInventory(ezHolder, lines * 9);
        if (enableTitle) inventory = Bukkit.createInventory(ezHolder, lines * 9, title.getText(player));
        ezHolder.setInventory(inventory);
        cache.put(player, new HashMap<>());
        for (int i = 0; i < 9 * lines; i ++) {
            if (items.containsKey(i)) {
                DrawSetting drawSetting = new DrawSetting(i);
                items.get(i).onDraw(player, drawSetting);
                inventory.setItem(i, drawSetting.render(player));
                cache.get(player).put(i, items.get(i));
            }
        }
        if (dynamicInput != null) {
            List<Input> list = dynamicInput.apply(player);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Input input = list.get(i);
                    if (input != null) {
                        DrawSetting drawSetting = new DrawSetting(-1);
                        input.onDraw(player, drawSetting);
                        inventory.setItem(i, drawSetting.render(player));
                        cache.get(player).put(i, input);
                    }
                }
            }
        }
        player.openInventory(inventory);
    }

    public void add(int slot, Input input) {
        items.put(slot, input);
    }

    public void setDynamicInput(Function<Player, List<Input>> function) {
        this.dynamicInput = function;
    }

    public void removeDynamicInput() {
        this.dynamicInput = null;
    }

    public void fill(Input input) {
        for (int i = 0; i < lines * 9; i++) {
            add(i, input);
        }
    }

    public void drawALine(int witchLine, Input input) {
        if (witchLine < 1) witchLine = 1;
        if (witchLine > lines) witchLine = lines;
        for (int i = (witchLine * 9) - 9; i < (witchLine * 9); i++) {
            add(i, input);
        }
    }

    public void drawAVerticalLine(int witchVerticalLine, Input input) {
        if (witchVerticalLine < 1) witchVerticalLine = 1;
        if (witchVerticalLine > 9) witchVerticalLine = 9;
        for (int i = witchVerticalLine - 1; i < lines * 9; i += 9) {
            add(i, input);
        }
    }

    public void drawACircle(Input input) {
        if (lines >= 3) {
            drawALine(1, input);
            drawALine(lines, input);
            drawAVerticalLine(1, input);
            drawAVerticalLine(9, input);
        } else {
            fill(input);
        }
    }

    public void drop() {
        if (!this.cache.isEmpty()) {
            for (Player player : cache.keySet()) {
                player.closeInventory();
            }
        }
        InventoryClickEvent.getHandlerList().unregister(this);
        InventoryCloseEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (Objects.equals(event.getClickedInventory(), event.getView().getTopInventory())) {
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                if (event.getInventory().getHolder() instanceof EzHolder) {
                    EzHolder ezHolder = (EzHolder) event.getInventory().getHolder();
                    if (ezHolder.getId().equalsIgnoreCase(this.id)) {
                        if (cache.get(player).containsKey(event.getRawSlot())) {
                            Input input = cache.get(player).get(event.getRawSlot());
                            if (input instanceof Button) {
                                ((Button) input).onClick(player, event.getClick(), event.getAction());
                            }
                        }
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            if (event.getInventory().getHolder() instanceof EzHolder) {
                EzHolder ezHolder = (EzHolder) event.getInventory().getHolder();
                if (ezHolder.getId().equalsIgnoreCase(this.id)) {
                    this.cache.remove((Player) event.getPlayer());
                    this.onClose.apply(this);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (this.cache.containsKey(event.getPlayer())) {
            this.cache.remove(event.getPlayer());
            this.onClose.apply(this);
        }
    }

    private static void defaultOnClose(EzInventory ezInventory) {}

    public void setOnClose(NonReturnWithOne<EzInventory> nonReturn) {
        onClose = nonReturn;
    }

}
