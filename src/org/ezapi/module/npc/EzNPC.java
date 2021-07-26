package org.ezapi.module.npc;

import io.netty.channel.Channel;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.ezapi.EasyAPI;
import org.ezapi.chat.ChatMessage;
import org.ezapi.function.NonReturnWithTwo;
import org.ezapi.module.packet.NMSPackets;
import org.ezapi.module.packet.Protocol;
import org.ezapi.module.packet.play.in.InteractEntity;
import org.ezapi.reflect.EzClass;
import org.ezapi.reflect.EzEnum;
import org.ezapi.util.PlayerUtils;
import org.ezapi.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EzNPC implements NPC {

    private final NPCType<?> type;

    private final ChatMessage name;

    private Location location;

    private boolean look = false;

    private final Map<Player,EzClass> viewers = new HashMap<>();

    private final Map<Player, BukkitTask> tasks = new HashMap<>();

    private final List<Player> hasShown = new ArrayList<>();

    private Object data = null;

    private Object lastData = null;

    private final Protocol protocol;

    private boolean dropped = false;

    private NonReturnWithTwo<Player, InteractEntity.ClickType> onClick = this::defaultOnClick;

    private void defaultOnClick(Player player, InteractEntity.ClickType clickType) {}

    public EzNPC(NPCType<?> type, ChatMessage name, Location location) {
        this.type = type;
        this.name = name;
        this.location = location;
        protocol = new Protocol(EasyAPI.getInstance()) {
            @Override
            public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
                if (packet.getClass().equals(NMSPackets.PacketPlayInUseEntity.getInstanceClass())) {
                    if (viewers.containsKey(sender)) {
                        if (hasShown.contains(sender)) {
                            EzClass Entity = new EzClass(ReflectionUtils.getNmsOrOld("world.entity.Entity", "Entity"));
                            Entity.setInstance(viewers.get(sender).getInstance());
                            int id = (int) Entity.invokeMethod("getId", new Class[0], new Object[0]);

                            EzClass PacketPlayInUseEntity = new EzClass(NMSPackets.PacketPlayInUseEntity.getInstanceClass());
                            PacketPlayInUseEntity.setInstance(packet);
                            int entityId = (int) PacketPlayInUseEntity.getField("a");
                            if (id == entityId) {
                                InteractEntity.ClickType type = InteractEntity.ClickType.RIGHT;
                                if (ReflectionUtils.getVersion() >= 16) {
                                    EzClass EnumEntityUseAction = new EzClass(ReflectionUtils.getClass(NMSPackets.PacketPlayInUseEntity.getInstanceClass().getName() + "$EnumEntityUseAction"));
                                    EzClass b = new EzClass(ReflectionUtils.getClass(NMSPackets.PacketPlayInUseEntity.getInstanceClass().getName() + "$b"));
                                    EnumEntityUseAction.setInstance(PacketPlayInUseEntity.getField("b"));
                                    if (EnumEntityUseAction.invokeMethod("a", new Class[0], new Object[0]).equals(b.getStaticField("b"))) {
                                        type = InteractEntity.ClickType.LEFT;
                                    }
                                } else {
                                    EzEnum EnumEntityUseAction = new EzEnum(ReflectionUtils.getClass(NMSPackets.PacketPlayInUseEntity.getInstanceClass().getName() + "$EnumEntityUseAction"));
                                    EnumEntityUseAction.setInstance(PacketPlayInUseEntity.getField("action"));
                                    if (EnumEntityUseAction.getInstance().equals(EnumEntityUseAction.valueOf("ATTACK"))) {
                                        type = InteractEntity.ClickType.LEFT;
                                    }
                                }
                                onClick.apply(sender, type);
                            }
                        }
                    }
                }
                return super.onPacketInAsync(sender, channel, packet);
            }
        };
    }

    public void setOnClick(NonReturnWithTwo<Player, InteractEntity.ClickType> onClick) {
        if (isDropped()) return;
        this.onClick = onClick;
    }

    public void setData(Object data) {
        if (isDropped()) return;
        this.data = data;
    }

    public void reloadData(Player player) {
        if (isDropped()) return;
        if (data != null) {
            if (viewers.containsKey(player)) {
                if (hasShown.contains(player)) {
                    this.type.setSpecialData(this.viewers.get(player).getInstance(), data);
                }
            }
        }
    }

    public void look(boolean look) {
        if (isDropped()) return;
        this.look = look;
    }

    @Override
    public void setLocation(Location location) {
        if (isDropped()) return;
        Location result = location.clone();
        result.setWorld(this.location.getWorld());
        this.location = result;
        if (hasShown.size() > 0) {
            for (Player player : hasShown) {
                refresh(player);
            }
        }
    }

    @Override
    public void addViewer(Player player) {
        if (isDropped()) return;
        if (!viewers.containsKey(player)) {
            viewers.put(player, type.createNPCEntity(this.name.getText(player), this.location));
            refresh(player);
            tasks.put(player, new BukkitRunnable() {
                @Override
                public void run() {
                    if (lastData != data) {
                        if (hasShown.contains(player)) {
                            lastData = data;
                            reloadData(player);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    refresh(player);
                                }
                            }.runTask(EasyAPI.getInstance());
                        }
                    }
                    if (look) {
                        if (hasShown.contains(player)) {
                            EzClass Entity = new EzClass(ReflectionUtils.getNmsOrOld("world.entity.Entity", "Entity"));
                            Entity.setInstance(viewers.get(player).getInstance());
                            float yaw = 0.0f;
                            float pitch = 0.0f;
                            if (look) {
                                Location loc = location.clone();
                                loc.setDirection(player.getLocation().clone().subtract(loc).toVector());
                                yaw = loc.getYaw();
                                pitch = loc.getPitch();
                            }
                            EzClass PacketPlayOutEntityLook = new EzClass(ReflectionUtils.getNmsOrOld("network.protocol.game.PacketPlayOutEntity$PacketPlayOutEntityLook", "PacketPlayOutEntity$PacketPlayOutEntityLook"));
                            PacketPlayOutEntityLook.setConstructor(int.class, byte.class, byte.class, boolean.class);
                            int id = (int) Entity.invokeMethod("getId", new Class[0], new Object[0]);
                            PacketPlayOutEntityLook.newInstance(id, ((byte) ((yaw % 360) * 256 / 360)), ((byte) ((pitch % 360) * 256 / 360)), false);
                            EzClass PacketPlayOutEntityHeadRotation = new EzClass(ReflectionUtils.getNmsOrOld("network.protocol.game.PacketPlayOutEntityHeadRotation", "PacketPlayOutEntityHeadRotation"));
                            PacketPlayOutEntityHeadRotation.setConstructor(Entity.getInstanceClass(), byte.class);
                            PacketPlayOutEntityHeadRotation.newInstance(Entity.getInstance(), ((byte) ((yaw % 360) * 256 / 360)));
                            PlayerUtils.sendPacket(player, PacketPlayOutEntityLook.getInstance());
                            PlayerUtils.sendPacket(player, PacketPlayOutEntityHeadRotation.getInstance());
                        }
                    }
                }
            }.runTaskTimerAsynchronously(EasyAPI.getInstance(), 1L, 1L));
        }
    }

    @Override
    public void removeViewer(Player player) {
        if (isDropped()) return;
        if (viewers.containsKey(player)) {
            destroy(player);
            EzClass Entity = new EzClass(ReflectionUtils.getNmsOrOld("world.entity.Entity", "Entity"));
            Entity.setInstance(viewers.get(player).getInstance());
            Entity.invokeMethod("die", new Class[0], new Object[0]);
            viewers.remove(player);
            tasks.remove(player).cancel();
        }
    }

    @Override
    public void refresh(Player player) {
        if (isDropped()) return;
        if (viewers.containsKey(player)) {
            destroy(player);
            EzClass Entity = new EzClass(ReflectionUtils.getNmsOrOld("world.entity.Entity", "Entity"));
            Entity.setInstance(viewers.get(player).getInstance());
            float yaw = 0.0f;
            float pitch = 0.0f;
            if (look) {
                Location location = this.location.clone();
                location.setDirection(player.getLocation().clone().subtract(location).toVector());
                yaw = location.getYaw();
                pitch = location.getPitch();
            }
            Entity.invokeMethod("setLocation", new Class[] {double.class,double.class,double.class,float.class,float.class}, new Object[] {location.getX(), location.getY(), location.getZ(),yaw,pitch});
            reloadData(player);
            try {
                for (EzClass packet : this.type.createSpawnPacket(this.viewers.get(player).getInstance())) {
                    PlayerUtils.sendPacket(player, packet.getInstance());
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
            hasShown.add(player);
        }
    }

    @Override
    public void destroy(Player player) {
        if (isDropped()) return;
        if (viewers.containsKey(player)) {
            if (hasShown.contains(player)) {
                EzClass Entity = new EzClass(ReflectionUtils.getNmsOrOld("world.entity.Entity", "Entity"));
                Entity.setInstance(viewers.get(player).getInstance());
                int id = (int) Entity.invokeMethod("getId", new Class[0], new Object[0]);
                EzClass PacketPlayOutEntityDestroy = new EzClass(ReflectionUtils.getNmsOrOld("network.protocol.game.PacketPlayOutEntityDestroy", "PacketPlayOutEntityDestroy"));
                PacketPlayOutEntityDestroy.setConstructor(int[].class);
                PacketPlayOutEntityDestroy.newInstance(new Object[] {new int[] {id}});
                PlayerUtils.sendPacket(player, PacketPlayOutEntityDestroy.getInstance());
                hasShown.remove(player);
            }
        }
    }

    @Override
    public List<Player> getViewers() {
        if (isDropped()) return new ArrayList<>();
        return new ArrayList<>(this.viewers.keySet());
    }

    @Override
    public void removeAll() {
        if (isDropped()) return;
        for (Player player : getViewers()) {
            removeViewer(player);
        }
    }

    public void drop() {
        if (!dropped) {
            removeAll();
            protocol.close();
            dropped = true;
        }
    }

    public boolean isDropped() {
        return dropped;
    }
}
