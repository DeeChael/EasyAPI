package org.ezapi.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ezapi.EasyAPI;
import org.ezapi.chat.ChatMessage;
import org.ezapi.reflect.EzClass;
import org.ezapi.reflect.EzEnum;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public final class PlayerUtils {

    public static boolean hasOnlineAccount(Player player) {
        try {
            URL getIdUrl = new URL("https://api.mojang.com/users/profiles/minecraft/" + player.getName().toLowerCase());
            HttpURLConnection getIdUrlConnection = (HttpURLConnection) getIdUrl.openConnection();
            return getIdUrlConnection.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            return false;
        }
    }

    public static void resetSkin(Player player) {
        if (hasOnlineAccount(player)) {
            skin(player, player.getName());
        } else {
            EzClass craftPlayerClass = new EzClass(ReflectionUtils.getObcClass("entity.CraftPlayer"));
            craftPlayerClass.setInstance(player);
            GameProfile gameProfile = (GameProfile) craftPlayerClass.invokeMethod("getProfile", new Class[0], new Object[0]);
            if (gameProfile.getProperties().containsKey("textures")) {
                gameProfile.getProperties().removeAll("textures");
            }
        }
    }

    public static void syncSkin(Player player) {
        if (hasOnlineAccount(player)) {
            skin(player, player.getName());
        }
    }

    public static UUID getOnlineUniqueId(Player player) {
        if (hasOnlineAccount(player)) {
            try {
                URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + player.getName().toLowerCase());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    return UUID.fromString(StringUtils.noLineUUIDtoLined(new JsonParser().parse(new InputStreamReader(connection.getInputStream())).getAsJsonObject().get("id").getAsString()));
                }
            } catch (IOException ignored) {
            }
        }
        return player.getUniqueId();
    }

    public static void skin(Player player, String skinOwner) {
        EzClass craftPlayerClass = new EzClass(ReflectionUtils.getObcClass("entity.CraftPlayer"));
        craftPlayerClass.setInstance(player);
        GameProfile gameProfile = (GameProfile) craftPlayerClass.invokeMethod("getProfile", new Class[0], new Object[0]);
        try {
            URL getIdUrl = new URL("https://api.mojang.com/users/profiles/minecraft/" + skinOwner.toLowerCase());
            HttpURLConnection getIdUrlConnection = (HttpURLConnection) getIdUrl.openConnection();
            if (getIdUrlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String id = new JsonParser().parse(new InputStreamReader(getIdUrlConnection.getInputStream())).getAsJsonObject().get("id").getAsString();
                URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + id + "?unsigned=false");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    JsonObject jsonObject = new JsonParser().parse(new InputStreamReader(connection.getInputStream())).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
                    String value = jsonObject.get("value").getAsString();
                    String signature = jsonObject.get("signature").getAsString();
                    if (gameProfile.getProperties().containsKey("textures")) {
                        gameProfile.getProperties().removeAll("textures");
                    }
                    gameProfile.getProperties().put("textures", new Property("textures", value, signature));
                    Object remove = createPacketPlayOutPlayerInfoRemovePlayer(player);
                    Object add = createPacketPlayOutPlayerInfoAddPlayer(player);
                    Object respawn = createPacketPlayOutRespawn(player);
                    Object destroy = createPacketPlayOutEntityDestroy(player);
                    Object spawn = createPacketPlayOutNamedEntitySpawn(player);
                    for (Player target : Bukkit.getOnlinePlayers()) {
                        if (target.equals(player)) {
                            ItemStack[] contents = player.getInventory().getContents();
                            boolean isFlying = player.isFlying();
                            Location location = target.getLocation();
                            sendPacket(target, remove);
                            sendPacket(target, add);
                            sendPacket(target, respawn);
                            target.teleport(location);
                            player.getInventory().setContents(contents);
                            player.setFlying(isFlying);
                            continue;
                        }
                        Location location = target.getLocation();
                        sendPacket(target, remove);
                        sendPacket(target, add);
                        sendPacket(target, destroy);
                        sendPacket(target, spawn);
                        target.teleport(location);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void resetTitle(Player player) {
        resetTitle(new Player[] {player});
    }

    public static void resetTitle(Player... players) {
        resetTitle(Arrays.asList(players));
    }

    public static void resetTitle(List<Player> players) {
        for (Player player : players) {
            sendPacket(player, createResetPacket());
        }
    }

    public static void clearTitle(Player player) {
        clearTitle(new Player[] {player});
    }

    public static void clearTitle(Player... players) {
        clearTitle(Arrays.asList(players));
    }

    public static void clearTitle(List<Player> players) {
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

    /*

    public static void actionbar(ChatMessage title, int fadeIn, int stay, int fadeOut, Player player) {
        actionbar(title, fadeIn, stay, fadeOut, new Player[] {player});
    }

    public static void actionbar(ChatMessage title, int fadeIn, int stay, int fadeOut, Player... players) {
        actionbar(title, fadeIn, stay, fadeOut, Arrays.asList(players));
    }

     */

    public static void actionbar(ChatMessage title, Player player) {
        actionbar(title, Collections.singletonList(player));
    }

    public static void actionbar(ChatMessage title, Player... players) {
        actionbar(title, Arrays.asList(players));
    }

    public static void actionbar(ChatMessage title, List<Player> players) {
        for (Player player : players) {
            sendPacket(player, createActionbarPacket(title.getText(player)));
        }
    }

    /*

    public static void actionbar(ChatMessage title, int fadeIn, int stay, int fadeOut, List<Player> players) {
        Object timesPacket = createTimesPacket(fadeIn, stay, fadeOut);
        for (Player player : players) {
            Object actionbarPacket = createActionbarPacket(title.getText(player));
            sendPacket(player, timesPacket);
            sendPacket(player, actionbarPacket);
        }
    }

    */

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

    private static Object createPacketPlayOutPlayerInfoAddPlayer(Player player) {
        EzClass packetPlayerOutPlayerInfo = ReflectionUtils.getVersion() >= 16 ? new EzClass("net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo") : new EzClass(ReflectionUtils.getNmsClass("PacketPlayOutPlayerInfo"));
        EzEnum enumPlayerInfoAction = new EzEnum(packetPlayerOutPlayerInfo.getInstanceClass().getName() + "$EnumPlayerInfoAction");
        if (ReflectionUtils.getVersion() >= 16) {
            enumPlayerInfoAction.newInstance("a");
        } else {
            enumPlayerInfoAction.newInstance("ADD_PLAYER");
        }
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            EzClass entityPlayerClass = ReflectionUtils.getVersion() >= 9 && ReflectionUtils.getVersion() <= 15 ? new EzClass(ReflectionUtils.getNmsClass("EntityPlayer")) : new EzClass("net.minecraft.server.level.EntityPlayer");
            entityPlayerClass.setInstance(entityPlayer);
            packetPlayerOutPlayerInfo.setConstructor(enumPlayerInfoAction.getInstanceEnum(), ReflectionUtils.getArrayClassFromClass(entityPlayerClass.getInstanceClass()));
            Object[] objects = (Object[]) Array.newInstance(entityPlayerClass.getInstanceClass(), 1);
            objects[0] = entityPlayer;
            packetPlayerOutPlayerInfo.newInstance(enumPlayerInfoAction.getInstance(), objects);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
        }
        return packetPlayerOutPlayerInfo.getInstance();
    }

    private static Object createPacketPlayOutPlayerInfoRemovePlayer(Player player) {
        EzClass packetPlayerOutPlayerInfo = ReflectionUtils.getVersion() >= 16 ? new EzClass("net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo") : new EzClass(ReflectionUtils.getNmsClass("PacketPlayOutPlayerInfo"));
        EzEnum enumPlayerInfoAction = new EzEnum(packetPlayerOutPlayerInfo.getInstanceClass().getName() + "$EnumPlayerInfoAction");
        if (ReflectionUtils.getVersion() >= 16) {
            enumPlayerInfoAction.newInstance("e");
        } else {
            enumPlayerInfoAction.newInstance("REMOVE_PLAYER");
        }
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            EzClass entityPlayerClass = ReflectionUtils.getVersion() >= 9 && ReflectionUtils.getVersion() <= 15 ? new EzClass(ReflectionUtils.getNmsClass("EntityPlayer")) : new EzClass("net.minecraft.server.level.EntityPlayer");
            entityPlayerClass.setInstance(entityPlayer);
            packetPlayerOutPlayerInfo.setConstructor(enumPlayerInfoAction.getInstanceEnum(), ReflectionUtils.getArrayClassFromClass(entityPlayerClass.getInstanceClass()));
            Object[] objects = (Object[]) Array.newInstance(entityPlayerClass.getInstanceClass(), 1);
            objects[0] = entityPlayer;
            packetPlayerOutPlayerInfo.newInstance(enumPlayerInfoAction.getInstance(), objects);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
        }
        return packetPlayerOutPlayerInfo.getInstance();
    }

    private static Object createPacketPlayOutNamedEntitySpawn(Player player) {
        EzClass PacketPlayOutNamedEntitySpawn = ReflectionUtils.getVersion() >= 16 ? new EzClass("net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn") : new EzClass(ReflectionUtils.getNmsClass("PacketPlayOutNamedEntitySpawn"));
        EzClass EntityHuman = ReflectionUtils.getVersion() >= 16 ? new EzClass("net.minecraft.world.entity.player.EntityHuman") : new EzClass(ReflectionUtils.getNmsClass("EntityHuman"));
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            PacketPlayOutNamedEntitySpawn.setConstructor(EntityHuman.getInstanceClass());
            PacketPlayOutNamedEntitySpawn.newInstance(entityPlayer);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
        }
        return PacketPlayOutNamedEntitySpawn.getInstance();
    }

    private static Object createPacketPlayOutEntityDestroy(Player player) {
        try {
            return PacketPlayOutEntityDestroy().getConstructor(int[].class).newInstance(new Object[] { new int[] {getId(player)} });
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
        }
        return null;
    }

    private static int getId(Player player) {
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            return (int) entityPlayer.getClass().getMethod("getId").invoke(entityPlayer);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static Class<?> PacketPlayOutEntityDestroy() {
        if (ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9) {
            return ReflectionUtils.getNmsClass("PacketPlayOutEntityDestroy");
        } else {
            return ReflectionUtils.getClass("net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy");
        }
    }

    private static Object createPacketPlayOutRespawn(Player player) {
        EzClass packetPlayerOutRespawn = ReflectionUtils.getVersion() >= 16 ? new EzClass("net.minecraft.network.protocol.game.PacketPlayOutRespawn") : new EzClass(ReflectionUtils.getNmsClass("PacketPlayOutRespawn"));
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            EzClass entityPlayerClass = ReflectionUtils.getVersion() >= 9 && ReflectionUtils.getVersion() <= 15 ? new EzClass(ReflectionUtils.getNmsClass("EntityPlayer")) : new EzClass("net.minecraft.server.level.EntityPlayer");
            entityPlayerClass.setInstance(entityPlayer);
            EzClass World = ReflectionUtils.getVersion() >= 16 ? new EzClass("net.minecraft.world.level.World") : new EzClass(ReflectionUtils.getNmsClass("World"));
            EzClass WorldServer = ReflectionUtils.getVersion() >= 16 ? new EzClass("net.minecraft.server.level.WorldServer") : new EzClass(ReflectionUtils.getNmsClass("WorldServer"));
            WorldServer.setInstance(player.getWorld().getClass().getMethod("getHandle").invoke(player.getWorld()));
            World.setInstance(WorldServer.getInstance());
            if (ReflectionUtils.getVersion() == 9) {
                EzClass GeneratorAccess = new EzClass(ReflectionUtils.getNmsClass("GeneratorAccess"));
                GeneratorAccess.setInstance(World.getInstance());
                EzEnum EnumDifficulty = new EzEnum(ReflectionUtils.getNmsClass("EnumDifficulty"));
                EnumDifficulty.setInstance(GeneratorAccess.invokeMethod("getDifficulty", new Class[0], new Object[0]));
                EzClass WorldType = new EzClass(ReflectionUtils.getNmsClass("WorldType"));
                WorldType.setInstance(World.invokeMethod("R", new Class[0], new Object[0]));
                EzEnum EnumGamemode = new EzEnum(ReflectionUtils.getNmsClass("EnumGamemode"));
                EzClass PlayerInteractManager = new EzClass(ReflectionUtils.getNmsClass("PlayerInteractManager"));
                PlayerInteractManager.setInstance(entityPlayerClass.getField("playerInteractManager"));
                EnumGamemode.setInstance(PlayerInteractManager.invokeMethod("getGameMode", new Class[0], new Object[0]));
                packetPlayerOutRespawn.setConstructor(int.class, EnumDifficulty.getInstanceEnum(), WorldType.getInstanceClass(), EnumGamemode.getInstanceEnum());
                packetPlayerOutRespawn.newInstance(WorldServer.getField("dimension"), EnumDifficulty.getInstance(), WorldType.getInstance(), EnumGamemode.getInstance());
            } else if (ReflectionUtils.getVersion() == 10) {
                EzClass GeneratorAccess = new EzClass(ReflectionUtils.getNmsClass("GeneratorAccess"));
                GeneratorAccess.setInstance(World.getInstance());
                EzClass DimensionManager = new EzClass(ReflectionUtils.getNmsClass("DimensionManager"));
                DimensionManager.setInstance(WorldServer.getField("dimension"));
                EzEnum EnumDifficulty = new EzEnum(ReflectionUtils.getNmsClass("EnumDifficulty"));
                EnumDifficulty.setInstance(GeneratorAccess.invokeMethod("getDifficulty", new Class[0], new Object[0]));
                EzClass WorldType = new EzClass(ReflectionUtils.getNmsClass("WorldType"));
                WorldType.setInstance(World.invokeMethod("S", new Class[0], new Object[0]));
                EzEnum EnumGamemode = new EzEnum(ReflectionUtils.getNmsClass("EnumGamemode"));
                EzClass PlayerInteractManager = new EzClass(ReflectionUtils.getNmsClass("PlayerInteractManager"));
                PlayerInteractManager.setInstance(entityPlayerClass.getField("playerInteractManager"));
                EnumGamemode.setInstance(PlayerInteractManager.invokeMethod("getGameMode", new Class[0], new Object[0]));
                packetPlayerOutRespawn.setConstructor(DimensionManager.getInstanceClass(), EnumDifficulty.getInstanceEnum(), WorldType.getInstanceClass(), EnumGamemode.getInstanceEnum());
                packetPlayerOutRespawn.newInstance(DimensionManager.getInstance(), EnumDifficulty.getInstance(), WorldType.getInstance(), EnumGamemode.getInstance());
            } else if (ReflectionUtils.getVersion() == 11) {
                EzClass DimensionManager = new EzClass(ReflectionUtils.getNmsClass("DimensionManager"));
                DimensionManager.setInstance(WorldServer.getField("dimension"));
                EzClass WorldType = new EzClass(ReflectionUtils.getNmsClass("WorldType"));
                WorldType.setInstance(World.invokeMethod("P", new Class[0], new Object[0]));
                EzEnum EnumGamemode = new EzEnum(ReflectionUtils.getNmsClass("EnumGamemode"));
                EzClass PlayerInteractManager = new EzClass(ReflectionUtils.getNmsClass("PlayerInteractManager"));
                PlayerInteractManager.setInstance(entityPlayerClass.getField("playerInteractManager"));
                EnumGamemode.setInstance(PlayerInteractManager.invokeMethod("getGameMode", new Class[0], new Object[0]));
                packetPlayerOutRespawn.setConstructor(DimensionManager.getInstanceClass(), WorldType.getInstanceClass(), EnumGamemode.getInstanceEnum());
                packetPlayerOutRespawn.newInstance(DimensionManager.getInstance(), WorldType.getInstance(), EnumGamemode.getInstance());
            } else if (ReflectionUtils.getVersion() == 12) {
                EzClass DimensionManager = new EzClass(ReflectionUtils.getNmsClass("DimensionManager"));
                DimensionManager.setInstance(WorldServer.getField("dimension"));
                long seed = (long) WorldServer.invokeMethod("getSeed", new Class[0], new Object[0]);
                EzClass WorldType = new EzClass(ReflectionUtils.getNmsClass("WorldType"));
                WorldType.setInstance(World.invokeMethod("P", new Class[0], new Object[0]));
                EzEnum EnumGamemode = new EzEnum(ReflectionUtils.getNmsClass("EnumGamemode"));
                EzClass PlayerInteractManager = new EzClass(ReflectionUtils.getNmsClass("PlayerInteractManager"));
                PlayerInteractManager.setInstance(entityPlayerClass.getField("playerInteractManager"));
                EnumGamemode.setInstance(PlayerInteractManager.invokeMethod("getGameMode", new Class[0], new Object[0]));
                packetPlayerOutRespawn.setConstructor(DimensionManager.getInstanceClass(), long.class, WorldType.getInstanceClass(), EnumGamemode.getInstanceEnum());
                packetPlayerOutRespawn.newInstance(DimensionManager.getInstance(), seed, WorldType.getInstance(), EnumGamemode.getInstance());
            } else if (ReflectionUtils.getVersion() == 13) {
                EzClass ResourceKey = new EzClass(ReflectionUtils.getNmsClass("ResourceKey"));
                Object dimension = World.invokeMethod("getTypeKey", new Class[0], new Object[0]);
                Object world = World.invokeMethod("getDimensionKey", new Class[0], new Object[0]);
                EzEnum EnumGamemode = new EzEnum(ReflectionUtils.getNmsClass("EnumGamemode"));
                EzClass PlayerInteractManager = new EzClass(ReflectionUtils.getNmsClass("PlayerInteractManager"));
                PlayerInteractManager.setInstance(entityPlayerClass.getField("playerInteractManager"));
                EnumGamemode.setInstance(PlayerInteractManager.invokeMethod("getGameMode", new Class[0], new Object[0]));
                long seed = (long) WorldServer.invokeMethod("getSeed", new Class[0], new Object[0]);
                boolean debug = (boolean) World.invokeMethod("isDebugWorld", new Class[0], new Object[0]);
                boolean flat = (boolean) WorldServer.invokeMethod("isFlatWorld", new Class[0], new Object[0]);
                packetPlayerOutRespawn.setConstructor(ResourceKey.getInstanceClass(), ResourceKey.getInstanceClass(), long.class, EnumGamemode.getInstanceEnum(), EnumGamemode.getInstanceEnum(), boolean.class, boolean.class, boolean.class);
                packetPlayerOutRespawn.newInstance(dimension, world, seed, EnumGamemode.getInstance(), EnumGamemode.getInstance(), debug, flat, true);
            } else if (ReflectionUtils.getVersion() == 14 || ReflectionUtils.getVersion() == 15) {
                EzClass DimensionManager = new EzClass(ReflectionUtils.getNmsClass("DimensionManager"));
                DimensionManager.setInstance(World.invokeMethod("getDimensionManager", new Class[0], new Object[0]));
                EzClass ResourceKey = new EzClass(ReflectionUtils.getNmsClass("ResourceKey"));
                ResourceKey.setInstance(World.invokeMethod("getDimensionKey", new Class[0], new Object[0]));
                EzEnum EnumGamemode = new EzEnum(ReflectionUtils.getNmsClass("EnumGamemode"));
                EzClass PlayerInteractManager = new EzClass(ReflectionUtils.getNmsClass("PlayerInteractManager"));
                PlayerInteractManager.setInstance(entityPlayerClass.getField("playerInteractManager"));
                EnumGamemode.setInstance(PlayerInteractManager.invokeMethod("getGameMode", new Class[0], new Object[0]));
                long seed = (long) WorldServer.invokeMethod("getSeed", new Class[0], new Object[0]);
                boolean debug = (boolean) World.invokeMethod("isDebugWorld", new Class[0], new Object[0]);
                boolean flat = (boolean) WorldServer.invokeMethod("isFlatWorld", new Class[0], new Object[0]);
                packetPlayerOutRespawn.setConstructor(DimensionManager.getInstanceClass(), ResourceKey.getInstanceClass(), long.class, EnumGamemode.getInstanceEnum(), EnumGamemode.getInstanceEnum(), boolean.class, boolean.class, boolean.class);
                packetPlayerOutRespawn.newInstance(DimensionManager.getInstance(), ResourceKey.getInstance(), seed, EnumGamemode.getInstance(), EnumGamemode.getInstance(), debug, flat, true);
            } else if (ReflectionUtils.getVersion() == 16) {
                EzClass DimensionManager = new EzClass("net.minecraft.world.level.dimension.DimensionManager");
                DimensionManager.setInstance(World.invokeMethod("getDimensionManager", new Class[0], new Object[0]));
                EzClass ResourceKey = new EzClass(ReflectionUtils.getNmsClass("ResourceKey"));
                ResourceKey.setInstance(World.invokeMethod("getDimensionKey", new Class[0], new Object[0]));
                EzEnum EnumGamemode = new EzEnum("net.minecraft.world.level.EnumGamemode");
                EzClass PlayerInteractManager = new EzClass("net.minecraft.server.level.PlayerInteractManager");
                PlayerInteractManager.setInstance(entityPlayerClass.getField("d"));
                EnumGamemode.setInstance(PlayerInteractManager.invokeMethod("getGameMode", new Class[0], new Object[0]));
                long seed = (long) WorldServer.invokeMethod("getSeed", new Class[0], new Object[0]);
                boolean debug = (boolean) WorldServer.invokeMethod("isDebugWorld", new Class[0], new Object[0]);
                boolean flat = (boolean) WorldServer.invokeMethod("isFlatWorld", new Class[0], new Object[0]);
                packetPlayerOutRespawn.setConstructor(DimensionManager.getInstanceClass(), ResourceKey.getInstanceClass(), long.class, EnumGamemode.getInstanceEnum(), EnumGamemode.getInstanceEnum(), boolean.class, boolean.class, boolean.class);
                packetPlayerOutRespawn.newInstance(DimensionManager.getInstance(), ResourceKey.getInstance(), seed, EnumGamemode.getInstance(), EnumGamemode.getInstance(), debug, flat, true);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            EasyAPI.getInstance().getLogger().severe(e.toString());
        }
        return packetPlayerOutRespawn.getInstance();
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
        return ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9 ? new EzEnum(Objects.requireNonNull(ReflectionUtils.getNmsClass("ChatMessageType"))) : new EzEnum("net.minecraft.network.chat.ChatMessageType");
    }

    private static EzClass PacketPlayOutChat() {
        return ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9 ? new EzClass(Objects.requireNonNull(ReflectionUtils.getNmsClass("PacketPlayOutChat"))) : new EzClass("net.minecraft.network.protocol.game.PacketPlayOutChat");
    }

}
