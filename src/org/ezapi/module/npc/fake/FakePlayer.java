package org.ezapi.module.npc.fake;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.reflect.EzEnum;
import org.ezapi.util.Ref;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class FakePlayer extends FakeEntity {

    public FakePlayer() {
    }

    @Override
    public EzClass create(String name, Location location) {
        EzClass EntityPlayer = new EzClass(Ref.getNmsOrOld("server.level.EntityPlayer", "EntityPlayer"));
        EzClass MinecraftServer = new EzClass(Ref.getNmsOrOld("server.MinecraftServer", "MinecraftServer"));
        EzClass WorldServer = new EzClass(Ref.getNmsOrOld("server.level.WorldServer", "WorldServer"));
        try {
            EzClass CraftServer = new EzClass(Ref.getObcClass("CraftServer"));
            CraftServer.setInstance(Bukkit.getServer());
            MinecraftServer.setInstance(CraftServer.invokeMethod("getServer", new Class[0], new Object[0]));
            WorldServer.setInstance(location.getWorld().getClass().getMethod("getHandle").invoke(location.getWorld()));
            if (Ref.getVersion() >= 16) {
                EntityPlayer.setConstructor(MinecraftServer.getInstanceClass(), WorldServer.getInstanceClass(), GameProfile.class);
                EntityPlayer.newInstance(MinecraftServer.getInstance(), WorldServer.getInstance(), new GameProfile(UUID.randomUUID(), name));
            } else {
                EzClass PlayerInteractManager = new EzClass(Ref.getNmsClass("PlayerInteractManager"));
                PlayerInteractManager.setConstructor(WorldServer.getInstanceClass());
                PlayerInteractManager.newInstance(WorldServer.getInstance());
                EntityPlayer.setConstructor(MinecraftServer.getInstanceClass(), WorldServer.getInstanceClass(), GameProfile.class, PlayerInteractManager.getInstanceClass());
                EntityPlayer.newInstance(MinecraftServer.getInstance(), WorldServer.getInstance(), new GameProfile(UUID.randomUUID(), name), PlayerInteractManager.getInstance());
            }
            EzClass Entity = new EzClass(Ref.getNmsOrOld("world.entity.Entity", "Entity"));
            Entity.setInstance(EntityPlayer.getInstance());
            Entity.invokeMethod("setLocation", new Class[] {double.class, double.class, double.class, float.class, float.class}, new Object[] {location.getX(), location.getY(), location.getZ(), 0.0f, 0.0f});
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace(System.out);
            e.printStackTrace();
        }
        return EntityPlayer;
    }

    @Override
    public List<EzClass> packet(Object entity) {
        EzClass Add = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutPlayerInfo", "PacketPlayOutPlayerInfo"));
        EzClass Remove = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutPlayerInfo", "PacketPlayOutPlayerInfo"));
        EzEnum EnumPlayerInfoAction = new EzEnum(Add.getInstanceClass().getName() + "$EnumPlayerInfoAction");
        EzClass EntityPlayer = new EzClass(Ref.getNmsOrOld("server.level.EntityPlayer", "EntityPlayer"));
        EntityPlayer.setInstance(entity);
        Add.setConstructor(EnumPlayerInfoAction.getInstanceEnum(), Ref.getArrayClass(EntityPlayer.getInstanceClass()));
        Remove.setConstructor(EnumPlayerInfoAction.getInstanceEnum(), Ref.getArrayClass(EntityPlayer.getInstanceClass()));
        Object[] objects = (Object[]) Array.newInstance(EntityPlayer.getInstanceClass(), 1);
        objects[0] = entity;
        if (Ref.getVersion() >= 16) {
            Add.newInstance(EnumPlayerInfoAction.valueOf("a"), objects);
            Remove.newInstance(EnumPlayerInfoAction.valueOf("e"), objects);
        } else {
            Add.newInstance(EnumPlayerInfoAction.valueOf("ADD_PLAYER"), objects);
            Remove.newInstance(EnumPlayerInfoAction.valueOf("REMOVE_PLAYER"), objects);
        }

        EzClass PacketPlayOutNamedEntitySpawn = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutNamedEntitySpawn", "PacketPlayOutNamedEntitySpawn"));
        EzClass EntityHuman = new EzClass(Ref.getNmsOrOld("world.entity.player.EntityHuman", "EntityHuman"));
        PacketPlayOutNamedEntitySpawn.setConstructor(EntityHuman.getInstanceClass());
        PacketPlayOutNamedEntitySpawn.newInstance(entity);
        List<EzClass> list = new ArrayList<>();
        list.add(Add);
        list.add(PacketPlayOutNamedEntitySpawn);
        list.add(Remove);
        return list;
    }

    @Override
    public void data(Object entity, Object owner) {
        if (!(owner instanceof String)) return;
        try {
            EzClass EntityPlayer = new EzClass(Ref.getNmsOrOld("server.level.EntityPlayer", "EntityPlayer"));
            EntityPlayer.setInstance(entity);
            EzClass EntityHuman = new EzClass(Ref.getNmsOrOld("world.entity.player.EntityHuman", "EntityHuman"));
            EntityHuman.setInstance(EntityPlayer.getInstance());
            GameProfile gameProfile = (GameProfile) EntityHuman.invokeMethod("getProfile", new Class[0], new Object[0]);
            URL getIdUrl = new URL("https://api.mojang.com/users/profiles/minecraft/" + ((String) owner).toLowerCase());
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
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
