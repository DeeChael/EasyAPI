package org.ezapi.util;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ezapi.chat.ChatMessage;
import org.ezapi.reflect.EzClass;
import org.ezapi.reflect.EzEnum;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public final class PlayerUtils {

    public static void reset(Player player) {
        reset(new Player[] {player});
    }

    public static void reset(Player... players) {
        reset(Arrays.asList(players));
    }

    public static void reset(List<Player> players) {
        for (Player player : players) {
            sendPacket(player, createResetPacket());
        }
    }

    public static void clear(Player player) {
        clear(new Player[] {player});
    }

    public static void clear(Player... players) {
        clear(Arrays.asList(players));
    }

    public static void clear(List<Player> players) {
        for (Player player : players) {
            sendPacket(player, createClearPacket());
        }
    }

    public static void titleWithSubtitle(ChatMessage title, ChatMessage subtitle, Player player) {
        titleWithSubtitle(title, subtitle, new Player[] { player });
    }

    public static void titleWithSubtitle(ChatMessage title, ChatMessage subtitle, Player... players) {
        titleWithSubtitle(title, subtitle, Arrays.asList(players));
    }

    public static void titleWithSubtitle(ChatMessage title, ChatMessage subtitle, List<Player> players) {
        titleWithSubtitle(title, subtitle, 10, 70, 20, players);
    }

    public static void titleWithSubtitle(ChatMessage title, ChatMessage subtitle, int fadeIn, int stay, int fadeOut, Player player) {
        titleWithSubtitle(title, subtitle, fadeIn, stay, fadeOut, new Player[] {player});
    }

    public static void titleWithSubtitle(ChatMessage title, ChatMessage subtitle, int fadeIn, int stay, int fadeOut, Player... players) {
        titleWithSubtitle(title, subtitle, fadeIn, stay, fadeOut, Arrays.asList(players));
    }

    public static void titleWithSubtitle(ChatMessage title, ChatMessage subtitle, int fadeIn, int stay, int fadeOut, List<Player> players) {
        Object timesPacket = createTimesPacket(fadeIn, stay, fadeOut);
        for (Player player : players) {
            Object titlePacket = createTitlePacket(title.getText(player));
            Object subtitlePacket = createSubtitlePacket(title.getText(player));
            sendPacket(player, timesPacket);
            sendPacket(player, titlePacket);
            sendPacket(player, subtitlePacket);
        }
    }

    public static void titleWithSubtitle(ChatMessage title, ChatMessage subtitle, int titleFadeIn, int titleStay, int titleFadeOut, int subtitleFadeIn, int subtitleStay, int subtitleFadeOut, Player player) {
        titleWithSubtitle(title, subtitle, titleFadeIn, titleStay, titleFadeOut, subtitleFadeIn, subtitleStay, subtitleFadeOut, new Player[] {player});
    }

    public static void titleWithSubtitle(ChatMessage title, ChatMessage subtitle, int titleFadeIn, int titleStay, int titleFadeOut, int subtitleFadeIn, int subtitleStay, int subtitleFadeOut, Player... players) {
        titleWithSubtitle(title, subtitle, titleFadeIn, titleStay, titleFadeOut, subtitleFadeIn, subtitleStay, subtitleFadeOut, Arrays.asList(players));
    }

    public static void titleWithSubtitle(ChatMessage title, ChatMessage subtitle, int titleFadeIn, int titleStay, int titleFadeOut, int subtitleFadeIn, int subtitleStay, int subtitleFadeOut, List<Player> players) {
        Object titleTimesPacket = createTimesPacket(titleFadeIn, titleStay, titleFadeOut);
        Object subtitleTimesPacket = createTimesPacket(subtitleFadeIn, subtitleStay, subtitleFadeOut);
        for (Player player : players) {
            Object titlePacket = createTitlePacket(title.getText(player));
            Object subtitlePacket = createSubtitlePacket(subtitle.getText(player));
            sendPacket(player, titleTimesPacket);
            sendPacket(player, titlePacket);
            sendPacket(player, subtitleTimesPacket);
            sendPacket(player, subtitlePacket);
        }
    }

    public static void actionbar(ChatMessage title, int fadeIn, int stay, int fadeOut, Player player) {
        actionbar(title, fadeIn, stay, fadeOut, new Player[] {player});
    }

    public static void actionbar(ChatMessage title, int fadeIn, int stay, int fadeOut, Player... players) {
        actionbar(title, fadeIn, stay, fadeOut, Arrays.asList(players));
    }

    public static void actionbar(ChatMessage title, Player player) {
        actionbar(title, Collections.singletonList(player));
    }

    public static void actionbar(ChatMessage title, Player... players) {
        actionbar(title, Arrays.asList(players));
    }

    public static void actionbar(ChatMessage title, List<Player> players) {
        actionbar(title, 10, 70, 20, players);
    }

    public static void actionbar(ChatMessage title, int fadeIn, int stay, int fadeOut, List<Player> players) {
        Object timesPacket = createTimesPacket(fadeIn, stay, fadeOut);
        for (Player player : players) {
            Object actionbarPacket = createActionbarPacket(title.getText(player));
            sendPacket(player, timesPacket);
            sendPacket(player, actionbarPacket);
        }
    }

    public static void subtitle(ChatMessage title, int fadeIn, int stay, int fadeOut, Player player) {
        subtitle(title, fadeIn, stay, fadeOut, new Player[] {player});
    }

    public static void subtitle(ChatMessage title, int fadeIn, int stay, int fadeOut, Player... players) {
        subtitle(title, fadeIn, stay, fadeOut, Arrays.asList(players));
    }

    public static void subtitle(ChatMessage title, Player player) {
        subtitle(title, Collections.singletonList(player));
    }

    public static void subtitle(ChatMessage title, Player... players) {
        subtitle(title, Arrays.asList(players));
    }

    public static void subtitle(ChatMessage title, List<Player> players) {
        subtitle(title, 10, 70, 20, players);
    }

    public static void subtitle(ChatMessage title, int fadeIn, int stay, int fadeOut, List<Player> players) {
        Object timesPacket = createTimesPacket(fadeIn, stay, fadeOut);
        for (Player player : players) {
            Object subtitlePacket = createSubtitlePacket(title.getText(player));
            sendPacket(player, timesPacket);
            sendPacket(player, subtitlePacket);
        }
    }

    public static void title(ChatMessage title, int fadeIn, int stay, int fadeOut, Player player) {
        title(title, fadeIn, stay, fadeOut, new Player[] {player});
    }

    public static void title(ChatMessage title, int fadeIn, int stay, int fadeOut, Player... players) {
        title(title, fadeIn, stay, fadeOut, Arrays.asList(players));
    }

    public static void title(ChatMessage title, Player player) {
        title(title, Collections.singletonList(player));
    }

    public static void title(ChatMessage title, Player... players) {
        title(title, Arrays.asList(players));
    }

    public static void title(ChatMessage title, List<Player> players) {
        title(title, 10, 70, 20, players);
    }

    public static void title(ChatMessage title, int fadeIn, int stay, int fadeOut, List<Player> players) {
        Object timesPacket = createTimesPacket(fadeIn, stay, fadeOut);
        for (Player player : players) {
            Object titlePacket = createTitlePacket(title.getText(player));
            sendPacket(player, timesPacket);
            sendPacket(player, titlePacket);
        }
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
        //player.spigot().sendMessage(chatMessage.getMessage(player));
        Object packetPlayOutChat = createPacketPlayOutChat(null, ChatMessageType().valueOf("SYSTEM"));
        EzClass ezClass = PacketPlayOutChat();
        ezClass.setInstance(packetPlayOutChat);
        ezClass.setField("components", new BaseComponent[] {chatMessage.getMessage(player)});
        sendPacket(player, ezClass.getInstance());
    }

    public static void sendPacket(Player player, Object packet) {
        EzClass packetClass = ReflectionUtils.getVersion() >= 9 && ReflectionUtils.getVersion() <= 15 ? new EzClass(ReflectionUtils.getNmsClass("Packet")) : new EzClass("net.minecraft.network.protocol.Packet");
        if (!packetClass.isExtended(packet)) return;
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            EzClass entityPlayerClass = ReflectionUtils.getVersion() >= 9 && ReflectionUtils.getVersion() <= 15 ? new EzClass(ReflectionUtils.getNmsClass("EntityPlayer")) : new EzClass("net.minecraft.server.level.EntityPlayer");
            entityPlayerClass.setInstance(entityPlayer);
            EzClass playerConnectionClass = ReflectionUtils.getVersion() >= 9 && ReflectionUtils.getVersion() <= 15 ? new EzClass(ReflectionUtils.getNmsClass("PlayerConnection")) : new EzClass("net.minecraft.server.network.PlayerConnection");
            Object playerConnection = entityPlayerClass.getField(ReflectionUtils.getVersion() >= 9 && ReflectionUtils.getVersion() <= 15 ? "playerConnection" : "b");
            playerConnectionClass.setInstance(playerConnection);
            playerConnectionClass.invokeMethod("sendPacket", new Class[] {packetClass.getInstanceClass()}, new Object[] {packet});
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
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

    private static Object createTimesPacket(int fadeIn, int stay, int fadeOut) {
        EzClass packetCreator = ReflectionUtils.getVersion() >= 16 ? new EzClass("net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket") : new EzClass(ReflectionUtils.getNmsClass("PacketPlayOutTitle"));
        packetCreator.setConstructor(int.class, int.class, int.class);
        packetCreator.newInstance(fadeIn, stay, fadeOut);
        return packetCreator.getInstance();
    }

    private static Object createTitlePacket(String text) {
        EzClass chatMessageClass = ReflectionUtils.getVersion() >= 16 ? new EzClass("net.minecraft.network.chat.ChatMessage") : new EzClass(ReflectionUtils.getNmsClass("ChatMessage"));
        chatMessageClass.setConstructor(String.class);
        chatMessageClass.newInstance(text);
        Object chatMessage = chatMessageClass.getInstance();
        if (ReflectionUtils.getVersion() >= 16) {
            EzClass packetCreator = new EzClass("net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket");
            packetCreator.setConstructor(IChatBaseComponent());
            packetCreator.newInstance(chatMessage);
            return packetCreator.getInstance();
        } else {
            EzEnum EnumTitleActionClass = new EzEnum(ReflectionUtils.getNmsClass("PacketPlayOutTitle$EnumTitleAction"));
            EnumTitleActionClass.newInstance("TITLE");
            EzClass packetCreator = new EzClass(ReflectionUtils.getNmsClass("PacketPlayOutTitle"));
            packetCreator.setConstructor(EnumTitleActionClass.getInstanceEnum(), IChatBaseComponent());
            packetCreator.newInstance(EnumTitleActionClass.getInstance(), chatMessage);
            return packetCreator.getInstance();
        }
    }

    private static Object createSubtitlePacket(String text) {
        EzClass chatMessageClass = ReflectionUtils.getVersion() >= 16 ? new EzClass("net.minecraft.network.chat.ChatMessage") : new EzClass(ReflectionUtils.getNmsClass("ChatMessage"));
        chatMessageClass.setConstructor(String.class);
        chatMessageClass.newInstance(text);
        Object chatMessage = chatMessageClass.getInstance();
        if (ReflectionUtils.getVersion() >= 16) {
            EzClass packetCreator = new EzClass("net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket");
            packetCreator.setConstructor(IChatBaseComponent());
            packetCreator.newInstance(chatMessage);
            return packetCreator.getInstance();
        } else {
            EzEnum EnumTitleActionClass = new EzEnum(ReflectionUtils.getNmsClass("PacketPlayOutTitle$EnumTitleAction"));
            EnumTitleActionClass.newInstance("SUBTITLE");
            EzClass packetCreator = new EzClass(ReflectionUtils.getNmsClass("PacketPlayOutTitle"));
            packetCreator.setConstructor(EnumTitleActionClass.getInstanceEnum(), IChatBaseComponent());
            packetCreator.newInstance(EnumTitleActionClass.getInstance(), chatMessage);
            return packetCreator.getInstance();
        }
    }

    private static Object createActionbarPacket(String text) {
        EzClass chatMessageClass = ReflectionUtils.getVersion() >= 16 ? new EzClass("net.minecraft.network.chat.ChatMessage") : new EzClass(ReflectionUtils.getNmsClass("ChatMessage"));
        chatMessageClass.setConstructor(String.class);
        chatMessageClass.newInstance(text);
        Object chatMessage = chatMessageClass.getInstance();
        if (ReflectionUtils.getVersion() >= 16) {
            EzClass packetCreator = new EzClass("net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket");
            packetCreator.setConstructor(IChatBaseComponent());
            packetCreator.newInstance(chatMessage);
            return packetCreator.getInstance();
        } else {
            EzEnum EnumTitleActionClass = new EzEnum(ReflectionUtils.getNmsClass("PacketPlayOutTitle$EnumTitleAction"));
            EnumTitleActionClass.newInstance("ACTIONBAR");
            EzClass packetCreator = new EzClass(ReflectionUtils.getNmsClass("PacketPlayOutTitle"));
            packetCreator.setConstructor(EnumTitleActionClass.getInstanceEnum(), IChatBaseComponent());
            packetCreator.newInstance(EnumTitleActionClass.getInstance(), chatMessage);
            return packetCreator.getInstance();
        }
    }

    private static Object createClearPacket() {
        if (ReflectionUtils.getVersion() >= 16) {
            EzClass packetCreator = new EzClass("net.minecraft.network.protocol.game.ClientboundClearTitlesPacket");
            packetCreator.setConstructor(boolean.class);
            packetCreator.newInstance(false);
            return packetCreator.getInstance();
        } else {
            EzEnum EnumTitleActionClass = new EzEnum(ReflectionUtils.getNmsClass("PacketPlayOutTitle$EnumTitleAction"));
            EnumTitleActionClass.newInstance("CLEAR");
            EzClass packetCreator = new EzClass(ReflectionUtils.getNmsClass("PacketPlayOutTitle"));
            packetCreator.setConstructor(EnumTitleActionClass.getInstanceEnum(), IChatBaseComponent());
            packetCreator.newInstance(EnumTitleActionClass.getInstance(), null);
            return packetCreator.getInstance();
        }
    }

    private static Object createResetPacket() {
        if (ReflectionUtils.getVersion() >= 16) {
            EzClass packetCreator = new EzClass("net.minecraft.network.protocol.game.ClientboundClearTitlesPacket");
            packetCreator.setConstructor(boolean.class);
            packetCreator.newInstance(true);
            return packetCreator.getInstance();
        } else {
            EzEnum EnumTitleActionClass = new EzEnum(ReflectionUtils.getNmsClass("PacketPlayOutTitle$EnumTitleAction"));
            EnumTitleActionClass.newInstance("RESET");
            EzClass packetCreator = new EzClass(ReflectionUtils.getNmsClass("PacketPlayOutTitle"));
            packetCreator.setConstructor(EnumTitleActionClass.getInstanceEnum(), IChatBaseComponent());
            packetCreator.newInstance(EnumTitleActionClass.getInstance(), null);
            return packetCreator.getInstance();
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
