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
        EzClass CraftMagicNumbers = new EzClass(Ref.getObcClass("util.CraftMagicNumbers"));
        EzClass Item = Ref.getVersion() >= 16 ? new EzClass("net.minecraft.world.item.Item") : new EzClass(Ref.getNmsClass("Item"));
        Item.setInstance(CraftMagicNumbers.invokeMethod("getItem", new Class[] { Material.class }, new Object[] { material }));
        EzClass PacketPlayOutSetCooldown = Ref.getVersion() >= 16 ? new EzClass("net.minecraft.network.protocol.game.PacketPlayOutSetCooldown") : new EzClass(Ref.getNmsClass("PacketPlayOutSetCooldown"));
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
        EzClass BlockPosition = Ref.getVersion() >= 16 ? new EzClass("net.minecraft.core.BlockPosition") : new EzClass(Ref.getNmsClass("BlockPosition"));
        BlockPosition.setConstructor(double.class, double.class, double.class);
        BlockPosition.newInstance(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
        EzClass PacketPlayOutBlockBreakAnimation = Ref.getVersion() >= 16 ? new EzClass("net.minecraft.network.protocol.game.PacketPlayOutBlockBreakAnimation") : new EzClass(Ref.getNmsClass("PacketPlayOutBlockBreakAnimation"));
        PacketPlayOutBlockBreakAnimation.setConstructor(int.class, BlockPosition.getInstanceClass(), int.class);
        PacketPlayOutBlockBreakAnimation.newInstance(diggerId, BlockPosition.getInstance(), stage.getStage());
        return PacketPlayOutBlockBreakAnimation.getInstance();
    }

    private static final UUID CHAT_UUID = new UUID(0L, 0L);

    public static Object createPacketPlayOutChat(ChatMessage message, String locale, int type) {
        if (!(type >= 0 && type <= 2)) return null;

        EzClass PacketPlayOutChat = Ref.getVersion() <= 15 && Ref.getVersion() >= 9 ? new EzClass(Objects.requireNonNull(Ref.getNmsClass("PacketPlayOutChat"))) : new EzClass("net.minecraft.network.protocol.game.PacketPlayOutChat");
        EzEnum ChatMessageType = Ref.getVersion() <= 15 && Ref.getVersion() >= 9 ? new EzEnum(Objects.requireNonNull(Ref.getNmsClass("ChatMessageType"))) : new EzEnum("net.minecraft.network.chat.ChatMessageType");
        if (type == 0) {
            ChatMessageType.setInstance("CHAT");
        } else if (type == 2) {
            ChatMessageType.setInstance("GAME_INFO");
        } else {
            ChatMessageType.setInstance("SYSTEM");
        }
        PacketPlayOutChat.setConstructor(Ref.getVersion() <= 15 && Ref.getVersion() >= 9 ? Ref.getNmsClass("IChatBaseComponent") : Ref.getClass("net.minecraft.network.chat.IChatBaseComponent"), ChatMessageType.getInstanceEnum(), UUID.class);
        PacketPlayOutChat.newInstance(null, ChatMessageType.getInstance(), CHAT_UUID);
        PacketPlayOutChat.setField("components", new BaseComponent[] { message.getMessage(locale) });
        return PacketPlayOutChat.getInstance();
    }

    public static Object createPacketPlayOutChat(String message, int type) {
        if (!(type >= 0 && type <= 2)) return null;
        EzClass PacketPlayOutChat = Ref.getVersion() <= 15 && Ref.getVersion() >= 9 ? new EzClass(Objects.requireNonNull(Ref.getNmsClass("PacketPlayOutChat"))) : new EzClass("net.minecraft.network.protocol.game.PacketPlayOutChat");
        EzEnum ChatMessageType = Ref.getVersion() <= 15 && Ref.getVersion() >= 9 ? new EzEnum(Objects.requireNonNull(Ref.getNmsClass("ChatMessageType"))) : new EzEnum("net.minecraft.network.chat.ChatMessageType");
        if (type == 0) {
            ChatMessageType.setInstance("CHAT");
        } else if (type == 2) {
            ChatMessageType.setInstance("GAME_INFO");
        } else {
            ChatMessageType.setInstance("SYSTEM");
        }
        EzClass ChatMessage = Ref.getVersion() <= 15 && Ref.getVersion() >= 9 ? new EzClass(Objects.requireNonNull(Ref.getNmsClass("ChatMessage"))) : new EzClass("net.minecraft.network.chat.ChatMessage");
        ChatMessage.setConstructor(String.class);
        ChatMessage.newInstance(message);
        PacketPlayOutChat.setConstructor(Ref.getVersion() <= 15 && Ref.getVersion() >= 9 ? Ref.getNmsClass("IChatBaseComponent") : Ref.getClass("net.minecraft.network.chat.IChatBaseComponent"), ChatMessageType.getInstanceEnum(), UUID.class);
        PacketPlayOutChat.newInstance(ChatMessage.getInstance(), ChatMessageType.getInstance(), CHAT_UUID);
        return PacketPlayOutChat.getInstance();
    }

    public static Object createPacketPlayOutEntityDestroy(Entity entity) {
        try {
            Class<?> PacketPlayOutEntityDestroy;
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                PacketPlayOutEntityDestroy = Ref.getNmsClass("PacketPlayOutEntityDestroy");
            } else {
                PacketPlayOutEntityDestroy = Ref.getClass("net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy");
            }
            Object nmsEntity = entity.getClass().getMethod("getHandle").invoke(entity);
            return PacketPlayOutEntityDestroy.getConstructor(int[].class).newInstance(new Object[] { new int[] { (int) nmsEntity.getClass().getMethod("getId").invoke(nmsEntity) } });
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
        }
        return null;
    }

    public static Object createPacketPlayOutRespawn(Player player) {
        EzClass packetPlayerOutRespawn = Ref.getVersion() >= 16 ? new EzClass("net.minecraft.network.protocol.game.PacketPlayOutRespawn") : new EzClass(Ref.getNmsClass("PacketPlayOutRespawn"));
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            EzClass entityPlayerClass = Ref.getVersion() >= 9 && Ref.getVersion() <= 15 ? new EzClass(Ref.getNmsClass("EntityPlayer")) : new EzClass("net.minecraft.server.level.EntityPlayer");
            entityPlayerClass.setInstance(entityPlayer);
            EzClass World = Ref.getVersion() >= 16 ? new EzClass("net.minecraft.world.level.World") : new EzClass(Ref.getNmsClass("World"));
            EzClass WorldServer = Ref.getVersion() >= 16 ? new EzClass("net.minecraft.server.level.WorldServer") : new EzClass(Ref.getNmsClass("WorldServer"));
            WorldServer.setInstance(player.getWorld().getClass().getMethod("getHandle").invoke(player.getWorld()));
            World.setInstance(WorldServer.getInstance());
            if (Ref.getVersion() == 9) {
                EzClass GeneratorAccess = new EzClass(Ref.getNmsClass("GeneratorAccess"));
                GeneratorAccess.setInstance(World.getInstance());
                EzEnum EnumDifficulty = new EzEnum(Ref.getNmsClass("EnumDifficulty"));
                EnumDifficulty.setInstance(GeneratorAccess.invokeMethod("getDifficulty", new Class[0], new Object[0]));
                EzClass WorldType = new EzClass(Ref.getNmsClass("WorldType"));
                WorldType.setInstance(World.invokeMethod("R", new Class[0], new Object[0]));
                EzEnum EnumGamemode = new EzEnum(Ref.getNmsClass("EnumGamemode"));
                EzClass PlayerInteractManager = new EzClass(Ref.getNmsClass("PlayerInteractManager"));
                PlayerInteractManager.setInstance(entityPlayerClass.getField("playerInteractManager"));
                EnumGamemode.setInstance(PlayerInteractManager.invokeMethod("getGameMode", new Class[0], new Object[0]));
                packetPlayerOutRespawn.setConstructor(int.class, EnumDifficulty.getInstanceEnum(), WorldType.getInstanceClass(), EnumGamemode.getInstanceEnum());
                packetPlayerOutRespawn.newInstance(WorldServer.getField("dimension"), EnumDifficulty.getInstance(), WorldType.getInstance(), EnumGamemode.getInstance());
            } else if (Ref.getVersion() == 10) {
                EzClass GeneratorAccess = new EzClass(Ref.getNmsClass("GeneratorAccess"));
                GeneratorAccess.setInstance(World.getInstance());
                EzClass DimensionManager = new EzClass(Ref.getNmsClass("DimensionManager"));
                DimensionManager.setInstance(WorldServer.getField("dimension"));
                EzEnum EnumDifficulty = new EzEnum(Ref.getNmsClass("EnumDifficulty"));
                EnumDifficulty.setInstance(GeneratorAccess.invokeMethod("getDifficulty", new Class[0], new Object[0]));
                EzClass WorldType = new EzClass(Ref.getNmsClass("WorldType"));
                WorldType.setInstance(World.invokeMethod("S", new Class[0], new Object[0]));
                EzEnum EnumGamemode = new EzEnum(Ref.getNmsClass("EnumGamemode"));
                EzClass PlayerInteractManager = new EzClass(Ref.getNmsClass("PlayerInteractManager"));
                PlayerInteractManager.setInstance(entityPlayerClass.getField("playerInteractManager"));
                EnumGamemode.setInstance(PlayerInteractManager.invokeMethod("getGameMode", new Class[0], new Object[0]));
                packetPlayerOutRespawn.setConstructor(DimensionManager.getInstanceClass(), EnumDifficulty.getInstanceEnum(), WorldType.getInstanceClass(), EnumGamemode.getInstanceEnum());
                packetPlayerOutRespawn.newInstance(DimensionManager.getInstance(), EnumDifficulty.getInstance(), WorldType.getInstance(), EnumGamemode.getInstance());
            } else if (Ref.getVersion() == 11) {
                EzClass DimensionManager = new EzClass(Ref.getNmsClass("DimensionManager"));
                DimensionManager.setInstance(WorldServer.getField("dimension"));
                EzClass WorldType = new EzClass(Ref.getNmsClass("WorldType"));
                WorldType.setInstance(World.invokeMethod("P", new Class[0], new Object[0]));
                EzEnum EnumGamemode = new EzEnum(Ref.getNmsClass("EnumGamemode"));
                EzClass PlayerInteractManager = new EzClass(Ref.getNmsClass("PlayerInteractManager"));
                PlayerInteractManager.setInstance(entityPlayerClass.getField("playerInteractManager"));
                EnumGamemode.setInstance(PlayerInteractManager.invokeMethod("getGameMode", new Class[0], new Object[0]));
                packetPlayerOutRespawn.setConstructor(DimensionManager.getInstanceClass(), WorldType.getInstanceClass(), EnumGamemode.getInstanceEnum());
                packetPlayerOutRespawn.newInstance(DimensionManager.getInstance(), WorldType.getInstance(), EnumGamemode.getInstance());
            } else if (Ref.getVersion() == 12) {
                EzClass DimensionManager = new EzClass(Ref.getNmsClass("DimensionManager"));
                DimensionManager.setInstance(WorldServer.getField("dimension"));
                long seed = (long) WorldServer.invokeMethod("getSeed", new Class[0], new Object[0]);
                EzClass WorldType = new EzClass(Ref.getNmsClass("WorldType"));
                WorldType.setInstance(World.invokeMethod("P", new Class[0], new Object[0]));
                EzEnum EnumGamemode = new EzEnum(Ref.getNmsClass("EnumGamemode"));
                EzClass PlayerInteractManager = new EzClass(Ref.getNmsClass("PlayerInteractManager"));
                PlayerInteractManager.setInstance(entityPlayerClass.getField("playerInteractManager"));
                EnumGamemode.setInstance(PlayerInteractManager.invokeMethod("getGameMode", new Class[0], new Object[0]));
                packetPlayerOutRespawn.setConstructor(DimensionManager.getInstanceClass(), long.class, WorldType.getInstanceClass(), EnumGamemode.getInstanceEnum());
                packetPlayerOutRespawn.newInstance(DimensionManager.getInstance(), seed, WorldType.getInstance(), EnumGamemode.getInstance());
            } else if (Ref.getVersion() == 13) {
                EzClass ResourceKey = new EzClass(Ref.getNmsClass("ResourceKey"));
                Object dimension = World.invokeMethod("getTypeKey", new Class[0], new Object[0]);
                Object world = World.invokeMethod("getDimensionKey", new Class[0], new Object[0]);
                EzEnum EnumGamemode = new EzEnum(Ref.getNmsClass("EnumGamemode"));
                EzClass PlayerInteractManager = new EzClass(Ref.getNmsClass("PlayerInteractManager"));
                PlayerInteractManager.setInstance(entityPlayerClass.getField("playerInteractManager"));
                EnumGamemode.setInstance(PlayerInteractManager.invokeMethod("getGameMode", new Class[0], new Object[0]));
                long seed = (long) WorldServer.invokeMethod("getSeed", new Class[0], new Object[0]);
                boolean debug = (boolean) World.invokeMethod("isDebugWorld", new Class[0], new Object[0]);
                boolean flat = (boolean) WorldServer.invokeMethod("isFlatWorld", new Class[0], new Object[0]);
                packetPlayerOutRespawn.setConstructor(ResourceKey.getInstanceClass(), ResourceKey.getInstanceClass(), long.class, EnumGamemode.getInstanceEnum(), EnumGamemode.getInstanceEnum(), boolean.class, boolean.class, boolean.class);
                packetPlayerOutRespawn.newInstance(dimension, world, seed, EnumGamemode.getInstance(), EnumGamemode.getInstance(), debug, flat, true);
            } else if (Ref.getVersion() == 14 || Ref.getVersion() == 15) {
                EzClass DimensionManager = new EzClass(Ref.getNmsClass("DimensionManager"));
                DimensionManager.setInstance(World.invokeMethod("getDimensionManager", new Class[0], new Object[0]));
                EzClass ResourceKey = new EzClass(Ref.getNmsClass("ResourceKey"));
                ResourceKey.setInstance(World.invokeMethod("getDimensionKey", new Class[0], new Object[0]));
                EzEnum EnumGamemode = new EzEnum(Ref.getNmsClass("EnumGamemode"));
                EzClass PlayerInteractManager = new EzClass(Ref.getNmsClass("PlayerInteractManager"));
                PlayerInteractManager.setInstance(entityPlayerClass.getField("playerInteractManager"));
                EnumGamemode.setInstance(PlayerInteractManager.invokeMethod("getGameMode", new Class[0], new Object[0]));
                long seed = (long) WorldServer.invokeMethod("getSeed", new Class[0], new Object[0]);
                boolean debug = (boolean) World.invokeMethod("isDebugWorld", new Class[0], new Object[0]);
                boolean flat = (boolean) WorldServer.invokeMethod("isFlatWorld", new Class[0], new Object[0]);
                packetPlayerOutRespawn.setConstructor(DimensionManager.getInstanceClass(), ResourceKey.getInstanceClass(), long.class, EnumGamemode.getInstanceEnum(), EnumGamemode.getInstanceEnum(), boolean.class, boolean.class, boolean.class);
                packetPlayerOutRespawn.newInstance(DimensionManager.getInstance(), ResourceKey.getInstance(), seed, EnumGamemode.getInstance(), EnumGamemode.getInstance(), debug, flat, true);
            } else if (Ref.getVersion() == 16) {
                EzClass DimensionManager = new EzClass("net.minecraft.world.level.dimension.DimensionManager");
                DimensionManager.setInstance(World.invokeMethod("getDimensionManager", new Class[0], new Object[0]));
                EzClass ResourceKey = new EzClass(Ref.getNmsClass("ResourceKey"));
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
