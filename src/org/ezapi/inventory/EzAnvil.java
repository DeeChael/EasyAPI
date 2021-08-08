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
import org.ezapi.function.NonReturnWithTwo;
import org.ezapi.util.Ref;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class EzAnvil implements Listener {

    public enum AnvilSlot {
        LEFT_INPUT(0), RIGHT_INPUT(1), OUTPUT(2);

        private final int slot;

        AnvilSlot(int slot) {
            this.slot = slot;
        }

        public int getSlot() {
            return slot;
        }
    }

    private final Map<Player,Inventory> cache = new HashMap<>();

    private final Map<Integer,Input> inputs = new HashMap<>();

    private NonReturnWithTwo<EzAnvil, Player> onClose = EzAnvil::defaultOnClose;

    private boolean dropped = false;

    private static void defaultOnClose(EzAnvil ezAnvil, Player player) {}

    /**
     * Create a new anvil gui
     * @param plugin your plugin instance
     */
    public EzAnvil(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Get if slot has item
     * @param slot slot
     * @return has item
     */
    public boolean has(AnvilSlot slot) {
        if (isDropped()) return false;
        return inputs.containsKey(slot.getSlot());
    }

    /**
     * Set input in slot
     * @param slot slot
     * @param input input
     */
    public void set(AnvilSlot slot, Input input) {
        if (isDropped()) return;
        this.inputs.put(slot.getSlot(), input);
    }

    /**
     * Get input of the slot
     * @param slot slot
     * @return input
     */
    public Input get(AnvilSlot slot) {
        if (isDropped()) return null;
        return inputs.get(slot.getSlot());
    }

    /**
     * Remove a input
     * @param slot slot
     * @return input
     */
    public Input remove(AnvilSlot slot) {
        if (isDropped()) return null;
        return inputs.remove(slot.getSlot());
    }


    /**
     * Get a player's opening bukkit inventory
     * @param player player
     * @return if player opened this anvil will return an inventory or else null
     */
    public Inventory getBukkit(Player player) {
        if (isDropped()) return null;
        return cache.get(player);
    }

    /**
     * Open the inventory to the player
     * @param player player
     */
    public void openToPlayer(Player player) {
        if (isDropped()) return;
        Class<?> clazz = createFakeAnvilClass();
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(Player.class);
            constructor.setAccessible(true);
            Object anvil = constructor.newInstance(player);
            Inventory inventory = (Inventory) clazz.getMethod("castToBukkit").invoke(anvil);
            for (int i : inputs.keySet()) {
                Input input = inputs.get(i);
                if (input instanceof Drawer) {
                    DrawSetting settings = new DrawSetting(i);
                    ((Drawer) input).onDraw(player, settings);
                    inventory.setItem(i, settings.render(player));
                }
            }
            clazz.getMethod("openToPlayer").invoke(anvil);
            cache.put(player, inventory);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
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
    public void onClick(InventoryClickEvent event) {
        if (isDropped()) return;
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (cache.containsKey(player)) {
                if (event.getView().getTopInventory().equals(cache.get(player))) {
                    if (Objects.equals(event.getClickedInventory(), event.getView().getTopInventory())) {
                        event.setCancelled(true);
                    } else {
                        if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                            event.setCancelled(true);
                        }
                    }
                    if (event.getRawSlot() >= 0 && event.getRawSlot() <= 2) {
                        if (inputs.containsKey(event.getRawSlot())) {
                            Input input = inputs.get(event.getRawSlot());
                            if (input instanceof Click) {
                                ((Click) input).onClick(player, event.getClick(), event.getAction());
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (isDropped()) return;
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (cache.containsKey(player)) {
                if (event.getView().getTopInventory().equals(cache.get(player))) {
                    cache.remove(player);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (isDropped()) return;
        cache.remove(event.getPlayer());
    }

    /**
     * Set event on player close the inventory
     * @param nonReturn function
     */
    public void setOnClose(NonReturnWithTwo<EzAnvil, Player> nonReturn) {
        if (isDropped()) return;
        onClose = nonReturn;
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////                                 Dynamic class generator                                  ///////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////




    private static Class<?> createFakeAnvilClass() {
        String base = "";
        base += "import " + Ref.getNmsOrOld("network.protocol.game.PacketPlayOutOpenWindow", "PacketPlayOutOpenWindow").getName() + ";\n";
        base += "import " + Ref.getNmsOrOld("world.inventory.Containers", "Containers").getName() + ";\n";
        base += "import " + Ref.getNmsOrOld("world.inventory.ContainerAnvil", "ContainerAnvil").getName() + ";\n";
        base += "import " + Ref.getNmsOrOld("world.inventory.ContainerAccess", "ContainerAccess").getName() + ";\n";
        base += "import " + Ref.getNmsOrOld("world.IInventory", "IInventory").getName() + ";\n";
        base += "import " + Ref.getNmsOrOld("world.level.World", "World").getName() + ";\n";
        base += "import " + Ref.getNmsOrOld("world.entity.player.EntityHuman", "EntityHuman").getName() + ";\n";
        base += "import " + Ref.getNmsOrOld("core.BlockPosition", "BlockPosition").getName() + ";\n";
        base += "import " + Ref.getNmsOrOld("network.chat.ChatMessage", "ChatMessage").getName() + ";\n";
        base += "import " + Ref.getObcClass("entity.CraftPlayer").getName() + ";\n";
        base += "import org.bukkit.entity.Player;\n";
        base += "import org.bukkit.inventory.Inventory;\n";
        base += "import org.bukkit.inventory.ItemStack;\n";
        base += "public final class FakeAnvil extends ContainerAnvil {\n";
        base += "private final Player bukkitPlayer;";
        base += "public FakeAnvil(Player player) {\n";
        base += getSuperMethod();
        base += "this.checkReachable = false;\n";
        base += "this.bukkitPlayer = player;\n";
        base += "}\n";
        base += getCostMethod();
        base += "@Override\n";
        base += "public void b(EntityHuman entityhuman) {\n";
        base += "}\n";
        base += getAMethod();
        base += getWindowIdMethod();
        /*
        base += "public void setLeftInput(ItemStack itemStack) {\n";
        base += "castToBukkit().setItem(0, itemStack);\n";
        base += "}\n";
        base += "public void setRightInput(ItemStack itemStack) {\n";
        base += "castToBukkit().setItem(1, itemStack);\n";
        base += "}\n";
        base += "public void setOutput(ItemStack itemStack) {\n";
        base += "castToBukkit().setItem(2, itemStack);\n";
        base += "}\n";
        */
        base += "public Inventory castToBukkit() {\n";
        base += "return this.getBukkitView().getTopInventory();\n";
        base += "}\n";
        base += getOpenMethod();
        base += "}";
        return Ref.createClass("FakeAnvil", base);
    }

    private static String worldFieldName() {
        return Ref.getVersion() >= 16 ? "t" : "world";
    }

    private static String getSuperMethod() {
        if (Ref.getVersion() <= 10) {
            return "super(((CraftPlayer) player).getHandle().inventory, ((CraftPlayer) player).getHandle().world, new BlockPosition(0, 0, 0), ((CraftPlayer) player).getHandle());\n";
        } else {
            return "super(((CraftPlayer) player).getHandle().nextContainerCounter(), ((CraftPlayer) player).getHandle().inventory, ContainerAccess.at(((CraftPlayer) player).getHandle()." + worldFieldName() + ", new BlockPosition(0, 0, 0)));\n";
        }
    }

    private static String getCostMethod() {
        String name = Ref.getVersion() >= 16 ? "i" : (Ref.getVersion() <= 10 ? "d" : "e");
        String levelCost = Ref.getVersion() >= 16 ? "w" : "levelCost";
        String method = Ref.getVersion() >= 12 ? ".set(" : (Ref.getVersion() == 11 ? ".a(" : " = (");
        String base = "";
        base += "@Override\n";
        base += "public void " + name + "() {\n";
        base += "super." + name + "();\n";
        base += "this." + levelCost + method + "0);\n";
        base += "}\n";
        return base;
    }

    private static String getAMethod() {
        String base = "";
        String add = Ref.getVersion() >= 16 ? "" : "World world, ";
        base += "@Override\n";
        base += "protected void a(EntityHuman entityHuman, " + add + "IInventory iInventory) {\n";
        base += "}\n";
        return base;
    }

    private static String getWindowIdMethod() {
        String base = "";
        base += "public int getWindowId() {\n";
        base += "return this." + (Ref.getVersion() >= 16 ? "j" : "windowId") + ";\n";
        base += "}\n";
        return base;
    }

    private static String getOpenMethod() {
        String base = "";
        base += "public void openToPlayer() {\n";
        base += Ref.getVersion() <= 10 ?
                        "PacketPlayOutOpenWindow packetPlayOutOpenWindow = new PacketPlayOutOpenWindow(this.getWindowId(), \"minecraft:anvil\", new ChatMessage(\"\"));\n"
                        : (Ref.getVersion() >= 16 ?
                        "PacketPlayOutOpenWindow packetPlayOutOpenWindow = new PacketPlayOutOpenWindow(this.getWindowId(), Containers.h, new ChatMessage(\"\"));\n" :
                        "PacketPlayOutOpenWindow packetPlayOutOpenWindow = new PacketPlayOutOpenWindow(this.getWindowId(), Containers.ANVIL, new ChatMessage(\"\"));\n"
                        );
        base += "((CraftPlayer) bukkitPlayer).getHandle()." + (Ref.getVersion() >= 16 ? "b" : "playerConnection") + ".sendPacket(packetPlayOutOpenWindow);\n";
        base += "((CraftPlayer) bukkitPlayer).getHandle()." + (Ref.getVersion() >= 16 ? "bV" : "activeContainer") + " = this;\n";
        base += "this.addSlotListener(((CraftPlayer) bukkitPlayer).getHandle());\n";
        base += "}\n";
        return base;
    }

}
