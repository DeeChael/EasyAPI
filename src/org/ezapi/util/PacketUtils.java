package org.ezapi.util;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.ezapi.EasyAPI;
import org.ezapi.block.BlockBreakAnimation;
import org.ezapi.chat.ChatMessage;
import org.ezapi.reflect.EzClass;
import org.ezapi.reflect.EzEnum;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.UUID;

public final class PacketUtils {

    public static Object createPacketPlayOutSetCooldown(Material material, int ticks) {
        EzClass CraftMagicNumbers = new EzClass(ReflectionUtils.getObcClass("util.CraftMagicNumbers"));
        EzClass Item = ReflectionUtils.getVersion() >= 16 ? new EzClass("net.minecraft.world.item.Item") : new EzClass(ReflectionUtils.getNmsClass("Item"));
        Item.setInstance(CraftMagicNumbers.invokeMethod("getItem", new Class[] { Material.class }, new Object[] { material }));
        EzClass PacketPlayOutSetCooldown = ReflectionUtils.getVersion() >= 16 ? new EzClass("net.minecraft.network.protocol.game.PacketPlayOutSetCooldown") : new EzClass(ReflectionUtils.getNmsClass("PacketPlayOutSetCooldown"));
        PacketPlayOutSetCooldown.setConstructor(Item.getInstanceClass(), int.class);
        PacketPlayOutSetCooldown.newInstance(Item.getInstance(), ticks);
        return PacketPlayOutSetCooldown.getInstance();
    }

    public static Object createPacketPlayOutBlockBreakAnimation(@Nullable Player digger, Location blockPosition, BlockBreakAnimation stage) {
        int diggerId = -1;
        if (digger != null) {
            try {
                Object nmsEntity = digger.getClass().getMethod("getHandle").invoke(digger);
                diggerId = (int) nmsEntity.getClass().getMethod("getId").invoke(nmsEntity);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
            }
        }
        EzClass BlockPosition = ReflectionUtils.getVersion() >= 16 ? new EzClass("net.minecraft.core.BlockPosition") : new EzClass(ReflectionUtils.getNmsClass("BlockPosition"));
        BlockPosition.setConstructor(double.class, double.class, double.class);
        BlockPosition.newInstance(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
        EzClass PacketPlayOutBlockBreakAnimation = ReflectionUtils.getVersion() >= 16 ? new EzClass("net.minecraft.network.protocol.game.PacketPlayOutBlockBreakAnimation") : new EzClass(ReflectionUtils.getNmsClass("PacketPlayOutBlockBreakAnimation"));
        PacketPlayOutBlockBreakAnimation.setConstructor(int.class, BlockPosition.getInstanceClass(), int.class);
        PacketPlayOutBlockBreakAnimation.newInstance(diggerId, BlockPosition.getInstance(), stage.getStage());
        return PacketPlayOutBlockBreakAnimation.getInstance();
    }

    private static final UUID CHAT_UUID = new UUID(0L, 0L);

    public static Object createPacketPlayOutChat(ChatMessage message, String locale, int type) {
        if (!(type >= 0 && type <= 2)) return null;

        EzClass PacketPlayOutChat = ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9 ? new EzClass(Objects.requireNonNull(ReflectionUtils.getNmsClass("PacketPlayOutChat"))) : new EzClass("net.minecraft.network.protocol.game.PacketPlayOutChat");
        EzEnum ChatMessageType = ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9 ? new EzEnum(Objects.requireNonNull(ReflectionUtils.getNmsClass("ChatMessageType"))) : new EzEnum("net.minecraft.network.chat.ChatMessageType");
        if (type == 0) {
            ChatMessageType.setInstance("CHAT");
        } else if (type == 2) {
            ChatMessageType.setInstance("GAME_INFO");
        } else {
            ChatMessageType.setInstance("SYSTEM");
        }
        PacketPlayOutChat.setConstructor(ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9 ? ReflectionUtils.getNmsClass("IChatBaseComponent") : ReflectionUtils.getClass("net.minecraft.network.chat.IChatBaseComponent"), ChatMessageType.getInstanceEnum(), UUID.class);
        PacketPlayOutChat.newInstance(null, ChatMessageType.getInstance(), CHAT_UUID);
        PacketPlayOutChat.setField("components", new BaseComponent[] { message.getMessage(locale) });
        return PacketPlayOutChat.getInstance();
    }

    public static Object createPacketPlayOutChat(String message, int type) {
        if (!(type >= 0 && type <= 2)) return null;
        EzClass PacketPlayOutChat = ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9 ? new EzClass(Objects.requireNonNull(ReflectionUtils.getNmsClass("PacketPlayOutChat"))) : new EzClass("net.minecraft.network.protocol.game.PacketPlayOutChat");
        EzEnum ChatMessageType = ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9 ? new EzEnum(Objects.requireNonNull(ReflectionUtils.getNmsClass("ChatMessageType"))) : new EzEnum("net.minecraft.network.chat.ChatMessageType");
        if (type == 0) {
            ChatMessageType.setInstance("CHAT");
        } else if (type == 2) {
            ChatMessageType.setInstance("GAME_INFO");
        } else {
            ChatMessageType.setInstance("SYSTEM");
        }
        EzClass ChatMessage = ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9 ? new EzClass(Objects.requireNonNull(ReflectionUtils.getNmsClass("ChatMessage"))) : new EzClass("net.minecraft.network.chat.ChatMessage");
        ChatMessage.setConstructor(String.class);
        ChatMessage.newInstance(message);
        PacketPlayOutChat.setConstructor(ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9 ? ReflectionUtils.getNmsClass("IChatBaseComponent") : ReflectionUtils.getClass("net.minecraft.network.chat.IChatBaseComponent"), ChatMessageType.getInstanceEnum(), UUID.class);
        PacketPlayOutChat.newInstance(ChatMessage.getInstance(), ChatMessageType.getInstance(), CHAT_UUID);
        return PacketPlayOutChat.getInstance();
    }

    public static Object createPacketPlayOutEntityDestroy(Entity entity) {
        try {
            Class<?> PacketPlayOutEntityDestroy;
            if (ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9) {
                PacketPlayOutEntityDestroy = ReflectionUtils.getNmsClass("PacketPlayOutEntityDestroy");
            } else {
                PacketPlayOutEntityDestroy = ReflectionUtils.getClass("net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy");
            }
            Object nmsEntity = entity.getClass().getMethod("getHandle").invoke(entity);
            return PacketPlayOutEntityDestroy.getConstructor(int[].class).newInstance(new Object[] { new int[] { (int) nmsEntity.getClass().getMethod("getId").invoke(nmsEntity) } });
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
        }
        return null;
    }

    public static Object createPacketPlayOutRespawn(Player player) {
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

}
