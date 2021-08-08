package org.ezapi.module.hologram;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.ezapi.chat.ChatMessage;
import org.ezapi.function.NonReturnWithTwo;
import org.ezapi.module.packet.play.in.PlayInUseEntityPacket;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.PlayerUtils;
import org.ezapi.util.Ref;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public final class TextHologram implements Hologram {

    private final EzClass World = new EzClass(Ref.getNmsOrOld("world.level.World", "World"));

    private final World bukkitWorld;

    private ChatMessage text;

    private final Map<Player,EzClass> viewers = new HashMap<>();

    private final List<Player> hasShown = new ArrayList<>();

    private Location location;

    private boolean dropped = false;

    private void defaultOnClick(Player player, PlayInUseEntityPacket.ClickType clickType) {}

    public TextHologram(ChatMessage text, World world, Location location) {
        this.text = text;
        this.bukkitWorld = world;
        this.location = location.clone().add(0.0, -1.0, 0.0);
        try {
            this.World.setInstance(world.getClass().getMethod("getHandle").invoke(world));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ChatMessage getText() {
        return text;
    }

    @Override
    public void setText(ChatMessage text) {
        if (isDropped()) return;
        this.text = text;
        if (hasShown.size() > 0) {
            for (Player player : new ArrayList<>(hasShown)) {
                EzClass ChatMessage = Ref.getVersion() <= 15 && Ref.getVersion() >= 9 ? new EzClass(Objects.requireNonNull(Ref.getNmsClass("ChatMessage"))) : new EzClass("net.minecraft.network.chat.ChatMessage");
                ChatMessage.setConstructor(String.class);
                ChatMessage.newInstance(text.getText(player));
                EzClass Entity = new EzClass(Ref.getNmsOrOld("world.entity.Entity", "Entity"));
                Entity.setInstance(viewers.get(player).getInstance());
                int id = (int) Entity.invokeMethod("getId", new Class[0], new Object[0]);
                Entity.invokeMethod("setCustomName", new Class[]{Ref.getNmsOrOld("network.chat.IChatBaseComponent", "IChatBaseComponent")}, new Object[]{ChatMessage.getInstance()});
                EzClass PacketPlayOutEntityMetadata = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutEntityMetadata", "PacketPlayOutEntityMetadata"));
                PacketPlayOutEntityMetadata.setConstructor(int.class, Ref.getNmsOrOld("network.syncher.DataWatcher", "DataWatcher"), boolean.class);
                PacketPlayOutEntityMetadata.newInstance(id, Entity.invokeMethod("getDataWatcher", new Class[0], new Object[0]), true);
                PlayerUtils.sendPacket(player, PacketPlayOutEntityMetadata.getInstance());
            }
        }
    }

    @Override
    public World getWorld() {
        return bukkitWorld;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        if (isDropped()) return;
        this.location = location.clone().add(0.0, -1.0, 0.0);
        if (hasShown.size() > 0) {
            for (Player player : new ArrayList<>(hasShown)) {
                refresh(player);
            }
        }
    }

    @Override
    public void addViewer(Player player) {
        if (isDropped()) return;
        if (!viewers.containsKey(player)) {
            EzClass ChatMessage = Ref.getVersion() <= 15 && Ref.getVersion() >= 9 ? new EzClass(Objects.requireNonNull(Ref.getNmsClass("ChatMessage"))) : new EzClass("net.minecraft.network.chat.ChatMessage");
            ChatMessage.setConstructor(String.class);
            ChatMessage.newInstance(text.getText(player));
            EzClass EntityArmorStand = new EzClass(Ref.getNmsOrOld("world.entity.decoration.EntityArmorStand", "EntityArmorStand"));
            EzClass Entity = new EzClass(Ref.getNmsOrOld("world.entity.Entity", "Entity"));
            EntityArmorStand.setConstructor(World.getInstanceClass(), double.class, double.class, double.class);
            EntityArmorStand.newInstance(World.getInstance(), location.getX(), location.getY(), location.getZ());
            Entity.setInstance(EntityArmorStand.getInstance());
            EntityArmorStand.invokeMethod("setInvisible", new Class[]{boolean.class}, new Object[]{true});
            EntityArmorStand.invokeMethod("setSmall", new Class[]{boolean.class}, new Object[]{true});
            EntityArmorStand.invokeMethod("setArms", new Class[]{boolean.class}, new Object[]{false});
            EntityArmorStand.invokeMethod("setBasePlate", new Class[]{boolean.class}, new Object[]{false});
            Entity.invokeMethod("setNoGravity", new Class[]{boolean.class}, new Object[]{true});
            Entity.invokeMethod("setCustomName", new Class[]{Ref.getNmsOrOld("network.chat.IChatBaseComponent", "IChatBaseComponent")}, new Object[]{ChatMessage.getInstance()});
            Entity.invokeMethod("setCustomNameVisible", new Class[]{boolean.class}, new Object[]{true});
            viewers.put(player, EntityArmorStand);
            refresh(player);
        }
    }

    private void reload() {
        List<Player> players = new ArrayList<>(viewers.keySet());
        for (Player player : players) {
            removeViewer(player);
            addViewer(player);
        }
    }

    @Override
    public void refresh(Player player) {
        if (isDropped()) return;
        if (viewers.containsKey(player)) {
            destroy(player);
            EzClass ChatMessage = Ref.getVersion() <= 15 && Ref.getVersion() >= 9 ? new EzClass(Objects.requireNonNull(Ref.getNmsClass("ChatMessage"))) : new EzClass("net.minecraft.network.chat.ChatMessage");
            ChatMessage.setConstructor(String.class);
            ChatMessage.newInstance(text.getText(player));
            EzClass EntityLiving = new EzClass(Ref.getNmsOrOld("world.entity.EntityLiving", "EntityLiving"));
            EzClass Entity = new EzClass(Ref.getNmsOrOld("world.entity.Entity", "Entity"));
            Entity.setInstance(viewers.get(player).getInstance());
            int id = (int) Entity.invokeMethod("getId", new Class[0], new Object[0]);
            Entity.invokeMethod("setCustomName", new Class[]{Ref.getNmsOrOld("network.chat.IChatBaseComponent", "IChatBaseComponent")}, new Object[]{ChatMessage.getInstance()});
            Entity.invokeMethod("setPosition", new Class[] {double.class, double.class, double.class}, new Object[] {location.getX(), location.getY(), location.getZ()});
            EzClass PacketPlayOutSpawnEntityLiving = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutSpawnEntityLiving", "PacketPlayOutSpawnEntityLiving"));
            PacketPlayOutSpawnEntityLiving.setConstructor(EntityLiving.getInstanceClass());
            PacketPlayOutSpawnEntityLiving.newInstance(viewers.get(player).getInstance());
            EzClass PacketPlayOutEntityMetadata = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutEntityMetadata", "PacketPlayOutEntityMetadata"));
            PacketPlayOutEntityMetadata.setConstructor(int.class, Ref.getNmsOrOld("network.syncher.DataWatcher", "DataWatcher"), boolean.class);
            PacketPlayOutEntityMetadata.newInstance(id, Entity.invokeMethod("getDataWatcher", new Class[0], new Object[0]), true);
            PlayerUtils.sendPacket(player, PacketPlayOutSpawnEntityLiving.getInstance());
            PlayerUtils.sendPacket(player, PacketPlayOutEntityMetadata.getInstance());
            destroy(player);
            hasShown.add(player);
        }
    }

    @Override
    public void destroy(Player player) {
        if (isDropped()) return;
        if (viewers.containsKey(player)) {
            if (hasShown.contains(player)) {
                EzClass Entity = new EzClass(Ref.getNmsOrOld("world.entity.Entity", "Entity"));
                Entity.setInstance(viewers.get(player).getInstance());
                int id = (int) Entity.invokeMethod("getId", new Class[0], new Object[0]);
                EzClass PacketPlayOutEntityDestroy = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutEntityDestroy", "PacketPlayOutEntityDestroy"));
                PacketPlayOutEntityDestroy.setConstructor(int[].class);
                PacketPlayOutEntityDestroy.newInstance(new Object[] {new int[] {id}});
                PlayerUtils.sendPacket(player, PacketPlayOutEntityDestroy.getInstance());
                hasShown.remove(player);
            }
        }
    }

    @Override
    public void removeViewer(Player player) {
        if (isDropped()) return;
        if (viewers.containsKey(player)) {
            destroy(player);
            EzClass Entity = new EzClass(Ref.getNmsOrOld("world.entity.Entity", "Entity"));
            Entity.setInstance(viewers.get(player).getInstance());
            Entity.invokeMethod("die", new Class[0], new Object[0]);
            viewers.remove(player);
        }
    }

    @Override
    public List<Player> getViewers() {
        if (isDropped()) return new ArrayList<>();
        return new ArrayList<>(viewers.keySet());
    }

    @Override
    public void removeAll() {
        if (isDropped()) return;
        for (Player player : getViewers()) {
            removeViewer(player);
        }
    }

    @Override
    public void drop() {
        if (!dropped) {
            removeAll();
            dropped = true;
        }
    }

    @Override
    public boolean isDropped() {
        return dropped;
    }

}
