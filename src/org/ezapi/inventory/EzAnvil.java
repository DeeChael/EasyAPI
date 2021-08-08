package org.ezapi.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.ezapi.util.Ref;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class EzAnvil implements Listener {

    private final Plugin plugin;

    private final Map<Player,Inventory> cache = new HashMap<>();

    private Input left = null;

    private Input right = null;

    private Input output = null;

    public EzAnvil(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void setLeftInput(Input left) {
        this.left = left;
    }

    public void setRightInput(Input right) {
        this.right = right;
    }

    public void setOutput(Input output) {
        this.output = output;
    }

    public Inventory getBukkit(Player player) {
        return cache.get(player);
    }

    public void openToPlayer(Player player) {
        Class<?> clazz = createFakeAnvilClass();
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(Player.class);
            constructor.setAccessible(true);
            Object anvil = constructor.newInstance(player);
            if (left instanceof Drawer) {
                DrawSetting settings = new DrawSetting(0);
                ((Drawer) left).onDraw(player, settings);
                clazz.getMethod("setLeftInput", ItemStack.class).invoke(anvil, settings.render(player));
            }
            if (right instanceof Drawer) {
                DrawSetting settings = new DrawSetting(1);
                ((Drawer) right).onDraw(player, settings);
                clazz.getMethod("setRightInput", ItemStack.class).invoke(anvil, settings.render(player));
            }
            if (output instanceof Drawer) {
                DrawSetting settings = new DrawSetting(2);
                ((Drawer) output).onDraw(player, settings);
                clazz.getMethod("setOutput", ItemStack.class).invoke(anvil, settings.render(player));
            }
            clazz.getMethod("openToPlayer").invoke(anvil);
            cache.put(player, (Inventory) clazz.getMethod("castToBukkit").invoke(anvil));
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
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
                    if (event.getRawSlot() == 0) {
                        if (left instanceof Click) {
                            ((Click) left).onClick(player, event.getClick(), event.getAction());
                        }
                    } else if (event.getRawSlot() == 1) {
                        if (right instanceof Click) {
                            ((Click) right).onClick(player, event.getClick(), event.getAction());
                        }
                    } else if (event.getRawSlot() == 2) {
                        if (output instanceof Click) {
                            ((Click) output).onClick(player, event.getClick(), event.getAction());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
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
        cache.remove(event.getPlayer());
    }

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
        base += "public void setLeftInput(ItemStack itemStack) {\n";
        base += "castToBukkit().setItem(0, itemStack);\n";
        base += "}\n";
        base += "public void setRightInput(ItemStack itemStack) {\n";
        base += "castToBukkit().setItem(1, itemStack);\n";
        base += "}\n";
        base += "public void setOutput(ItemStack itemStack) {\n";
        base += "castToBukkit().setItem(2, itemStack);\n";
        base += "}\n";
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
