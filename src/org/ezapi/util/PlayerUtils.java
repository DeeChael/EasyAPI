package org.ezapi.util;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ezapi.chat.ChatMessage;
import org.ezapi.reflect.EzClass;
import org.ezapi.reflect.EzEnum;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public final class PlayerUtils {

    public static void title(String title) {

    }

    public static void reloadCommands(Player player, Object nmsCommandDispatcher) {
        EzClass craftPlayer = new EzClass(ReflectionUtils.getObcClass("entity.CraftPlayer"));
        craftPlayer.setInstance(player);
        Object entityPlayer = craftPlayer.invokeMethod("getHandle", new Class[0], new Object[0]);
        EzClass entityPlayerClass = new EzClass(entityPlayer.getClass());
        try {
            nmsCommandDispatcher.getClass().getMethod("a", entityPlayerClass.getInstanceClass()).invoke(nmsCommandDispatcher, entityPlayer);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(Player player, ChatMessage chatMessage) {
        player.spigot().sendMessage(chatMessage.getMessage());
        /*
        Object packetPlayOutChat = createPacketPlayOutChat(null, ChatMessageType().valueOf("SYSTEM"));
        EzClass ezClass = PacketPlayOutChat();
        ezClass.setInstance(packetPlayOutChat);
        ezClass.setField("components", new BaseComponent[] {chatMessage.getMessage(player)});
        sendPacket(player, ezClass.getInstance());
        */
    }

    public static void sendPacket(Player player, Object packet) {
        EzClass packetClass = ReflectionUtils.getVersion() >= 9 && ReflectionUtils.getVersion() <= 15 ? new EzClass(ReflectionUtils.getNmsClass("Packet")) : new EzClass("net.minecraft.network.protocol.Packet");
        if (!packetClass.isExtended(packet)) return;
        EzClass craftPlayer = new EzClass(ReflectionUtils.getObcClass("entity.CraftPlayer"));
        craftPlayer.setInstance(player);
        Object entityPlayer = craftPlayer.invokeMethod("getHandle", new Class[0], new Object[0]);
        EzClass entityPlayerClass = new EzClass(entityPlayer.getClass());
        entityPlayerClass.setInstance(entityPlayer);
        EzClass playerConnectionClass = ReflectionUtils.getVersion() >= 9 && ReflectionUtils.getVersion() <= 15 ? new EzClass(ReflectionUtils.getNmsClass("PlayerConnection")) : new EzClass("net.minecraft.server.network.PlayerConnection");
        Object playerConnection = entityPlayerClass.getFieldByType(playerConnectionClass.getInstanceClass());
        playerConnectionClass.setInstance(playerConnection);
        playerConnectionClass.invokeMethod("sendPacket", new Class[] {packetClass.getInstanceClass()}, new Object[] {packet});
    }

    public static void smoothMove(Player player, Location locationToGo) {
        player.setVelocity(player.getVelocity().add(locationToGo.toVector()));
    }

    public static void smoothMoveTwice(Player player, Location first, Location second) {
        smoothMove(player, first);
        smoothMove(player, second);
    }

    public static void smoothMove(Player player, Location... locations) {
        if (locations.length > 0) {
            Loop.foreach(locations, ((integer, location) -> smoothMove(player, location)));
        }
    }

    public static double distance(Player player1, Player player2) {
        return player1.getLocation().distance(player2.getLocation());
    }

    public static boolean hasItemInMainHand(Player player) {
        return player.getInventory().getItemInMainHand().getType() != Material.AIR;
    }

    public static boolean hasItemInOffHand(Player player) {
        return player.getInventory().getItemInOffHand().getType() != Material.AIR;
    }

    public static boolean fullArmored(Player player) {
        boolean helmet = false;
        ItemStack helmetItemStack = player.getInventory().getHelmet();
        if (helmetItemStack != null) {
            helmet = helmetItemStack.getType().equals(Material.LEATHER_HELMET) ||
                    helmetItemStack.getType().equals(Material.CHAINMAIL_HELMET) ||
                    helmetItemStack.getType().equals(Material.IRON_HELMET) ||
                    helmetItemStack.getType().equals(Material.GOLDEN_HELMET) ||
                    helmetItemStack.getType().equals(Material.DIAMOND_HELMET) ||
                    helmetItemStack.getType().equals(Material.TURTLE_HELMET);
            if (!helmet && ReflectionUtils.getVersion() >= 13) {
                helmet = helmetItemStack.getType().equals(Material.valueOf("NETHERITE_HELMET"));
            }
        }
        boolean chestplate = false;
        ItemStack chestplateItemStack = player.getInventory().getHelmet();
        if (chestplateItemStack != null) {
            chestplate = chestplateItemStack.getType().equals(Material.LEATHER_CHESTPLATE) ||
                    chestplateItemStack.getType().equals(Material.CHAINMAIL_CHESTPLATE) ||
                    chestplateItemStack.getType().equals(Material.IRON_CHESTPLATE) ||
                    chestplateItemStack.getType().equals(Material.GOLDEN_CHESTPLATE) ||
                    chestplateItemStack.getType().equals(Material.DIAMOND_CHESTPLATE);
            if (!chestplate && ReflectionUtils.getVersion() >= 13) {
                chestplate = chestplateItemStack.getType().equals(Material.valueOf("NETHERITE_CHESTPLATE"));
            }
        }
        boolean leggings = false;
        ItemStack leggingsItemStack = player.getInventory().getHelmet();
        if (leggingsItemStack != null) {
            leggings = leggingsItemStack.getType().equals(Material.LEATHER_LEGGINGS) ||
                    leggingsItemStack.getType().equals(Material.CHAINMAIL_LEGGINGS) ||
                    leggingsItemStack.getType().equals(Material.IRON_LEGGINGS) ||
                    leggingsItemStack.getType().equals(Material.GOLDEN_LEGGINGS) ||
                    leggingsItemStack.getType().equals(Material.DIAMOND_LEGGINGS);
            if (!leggings && ReflectionUtils.getVersion() >= 13) {
                leggings = leggingsItemStack.getType().equals(Material.valueOf("NETHERITE_LEGGINGS"));
            }
        }
        boolean boots = false;
        ItemStack bootsItemStack = player.getInventory().getHelmet();
        if (bootsItemStack != null) {
            boots = bootsItemStack.getType().equals(Material.LEATHER_BOOTS) ||
                    bootsItemStack.getType().equals(Material.CHAINMAIL_BOOTS) ||
                    bootsItemStack.getType().equals(Material.IRON_BOOTS) ||
                    bootsItemStack.getType().equals(Material.GOLDEN_BOOTS) ||
                    bootsItemStack.getType().equals(Material.DIAMOND_BOOTS);
            if (!boots && ReflectionUtils.getVersion() >= 13) {
                boots = bootsItemStack.getType().equals(Material.valueOf("NETHERITE_BOOTS"));
            }
        }
        return helmet && chestplate && leggings && boots;
    }

    public static void banForever(OfflinePlayer player, String reason) {
        ban(player, reason, null);
    }

    public static void ban(OfflinePlayer player, String reason, Date unbanTime) {
        Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), reason, unbanTime, null);
        if (player.isOnline()) {
            ((Player) player).kickPlayer("You was banned");
        }
    }

    public static void unban(OfflinePlayer player) {
        if (Bukkit.getBanList(BanList.Type.NAME).isBanned(player.getName())) {
            Bukkit.getBanList(BanList.Type.NAME).pardon(player.getName());
        }
    }

    private static Object createPacketPlayOutChat(Object iChatBaseComponent, Object chatMessageType) {
        EzClass ezClass = PacketPlayOutChat();
        ezClass.setConstructor(IChatBaseComponent(), ChatMessageType().getInstanceEnum(), UUID.class);
        ezClass.newInstance(null, chatMessageType, uuid);
        return ezClass.getInstance();
    }

    private static Class<?> IChatBaseComponent() {
        if (ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9) {
            return ReflectionUtils.getNmsClass("IChatBaseComponent");
        } else {
            return ReflectionUtils.getClass("net.minecraft.network.chat.IChatBaseComponent");
        }
    }

    private static final UUID uuid = new UUID(0L, 0L);

    private static EzEnum ChatMessageType() {
        return ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9 ? new EzEnum(Objects.requireNonNull(ReflectionUtils.getNmsClass("ChatMessageType"))) : new EzEnum("net.minecraft.chat.ChatMessageType");
    }

    private static EzClass PacketPlayOutChat() {
        return ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9 ? new EzClass(Objects.requireNonNull(ReflectionUtils.getNmsClass("PacketPlayOutChat"))) : new EzClass("net.minecraft.network.protocol.game.PacketPlayOutChat");
    }

}
