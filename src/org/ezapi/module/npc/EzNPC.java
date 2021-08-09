package org.ezapi.module.npc;

import com.mojang.datafixers.util.Pair;
import io.netty.channel.Channel;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.ezapi.EasyAPI;
import org.ezapi.chat.ChatMessage;
import org.ezapi.function.NonReturnWithTwo;
import org.ezapi.module.packet.play.in.PlayInUseEntityPacket.ClickType;
import org.ezapi.module.packet.NMSPackets;
import org.ezapi.module.packet.Protocol;
import org.ezapi.reflect.EzClass;
import org.ezapi.reflect.EzEnum;
import org.ezapi.util.PlayerUtils;
import org.ezapi.util.Ref;
import org.ezapi.util.item.ItemUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EzNPC implements NPC {

    private NPCType<?> type;

    private ChatMessage name;

    private Location location;

    private boolean look = false;

    private final Map<Player,EzClass> viewers = new HashMap<>();

    private final Map<Player, BukkitTask> tasks = new HashMap<>();

    private final List<Player> hasShown = new ArrayList<>();

    private Object data = null;

    private Object lastData = null;

    private final Protocol protocol;

    private boolean dropped = false;

    private NonReturnWithTwo<Player, ClickType> onClick = this::defaultOnClick;

    private void defaultOnClick(Player player, ClickType clickType) {}

    private ItemStack main_hand = new ItemStack(Material.AIR);
    private ItemStack off_hand = new ItemStack(Material.AIR);
    private ItemStack head = new ItemStack(Material.AIR);
    private ItemStack chest = new ItemStack(Material.AIR);
    private ItemStack legs = new ItemStack(Material.AIR);
    private ItemStack feet = new ItemStack(Material.AIR);

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
                            EzClass Entity = new EzClass(Ref.getNmsOrOld("world.entity.Entity", "Entity"));
                            Entity.setInstance(viewers.get(sender).getInstance());
                            int id = (int) Entity.invokeMethod("getId", new Class[0], new Object[0]);
                            EzClass PacketPlayInUseEntity = new EzClass(NMSPackets.PacketPlayInUseEntity.getInstanceClass());
                            PacketPlayInUseEntity.setInstance(packet);
                            int entityId = (int) PacketPlayInUseEntity.getField("a");
                            EzEnum EnumHand = new EzEnum(Ref.getNmsOrOld("world.EnumHand", "EnumHand"));
                            if (id == entityId) {
                                ClickType type = ClickType.UNKNOWN;
                                if (Ref.getVersion() >= 16) {
                                    EnumHand.newInstance("a");
                                    EzClass EnumEntityUseAction = new EzClass(Ref.getClass(NMSPackets.PacketPlayInUseEntity.getInstanceClass().getName() + "$EnumEntityUseAction"));
                                    EzClass b = new EzClass(Ref.getClass(NMSPackets.PacketPlayInUseEntity.getInstanceClass().getName() + "$b"));
                                    EnumEntityUseAction.setInstance(PacketPlayInUseEntity.getField("b"));
                                    if (EnumEntityUseAction.invokeMethod("a", new Class[0], new Object[0]).equals(b.getStaticField("b"))) {
                                        type = ClickType.LEFT;
                                    } else if (EnumEntityUseAction.invokeMethod("a", new Class[0], new Object[0]).equals(b.getStaticField("a"))) {
                                        EzClass d = new EzClass(PacketPlayInUseEntity.getInstanceClass().getName() + "$d");
                                        d.setInstance(EnumEntityUseAction.getInstance());
                                        if (EnumHand.getInstance().equals(d.getField("a"))) {
                                            type = ClickType.RIGHT;
                                        }
                                    }
                                } else {
                                    EnumHand.newInstance("MAIN_HAND");
                                    EzEnum EnumEntityUseAction = new EzEnum(Ref.getClass(NMSPackets.PacketPlayInUseEntity.getInstanceClass().getName() + "$EnumEntityUseAction"));
                                    EnumEntityUseAction.setInstance(PacketPlayInUseEntity.getField("action"));
                                    if (EnumEntityUseAction.getInstance().equals(EnumEntityUseAction.valueOf("ATTACK"))) {
                                        type = ClickType.LEFT;
                                    } else if (EnumEntityUseAction.getInstance().equals(EnumEntityUseAction.valueOf("INTERACT"))) {
                                        if (EnumHand.getInstance().equals(PacketPlayInUseEntity.getField("d"))) {
                                            type = ClickType.RIGHT;
                                        }
                                    }
                                }
                                if (type != ClickType.UNKNOWN) {
                                    onClick.apply(sender, type);
                                }
                            }
                        }
                    }
                }
                return super.onPacketInAsync(sender, channel, packet);
            }
        };
    }

    /**
     * Set click event
     * @param onClick function
     */
    public void setOnClick(NonReturnWithTwo<Player, ClickType> onClick) {
        if (isDropped()) return;
        if (onClick == null) return;
        this.onClick = onClick;
    }

    @Override
    public void setItemInMainHand(ItemStack itemStack) {
        if (isDropped()) return;
        this.main_hand = itemStack;
        refreshAll();
    }

    @Override
    public void setItemInOffHand(ItemStack itemStack) {
        if (isDropped()) return;
        this.off_hand = itemStack;
        refreshAll();
    }

    @Override
    public void setBoots(ItemStack itemStack) {
        if (isDropped()) return;
        this.feet = itemStack;
        refreshAll();
    }

    @Override
    public void setLeggings(ItemStack itemStack) {
        if (isDropped()) return;
        this.legs = itemStack;
        refreshAll();
    }

    @Override
    public void setChestplate(ItemStack itemStack) {
        if (isDropped()) return;
        this.chest = itemStack;
        refreshAll();
    }

    @Override
    public void setHelmet(ItemStack itemStack) {
        if (isDropped()) return;
        this.head = itemStack;
        refreshAll();
    }

    @Override
    public void setName(ChatMessage name) {
        if (isDropped()) return;
        this.name = name;
        refreshAll();
    }

    @Override
    public void setType(NPCType<?> type) {
        if (isDropped()) return;
        this.type = type;
        reload();
    }

    public void move(Location location) {
        Location original = this.location.clone();
        this.location = location;
        Location loc = location.clone().subtract(original.clone());
        if (loc.getX() <= 8.0 && loc.getY() <= 8.0 && loc.getZ() <= 8.0) {
            for (Player player : getViewers()) {
                /*
                Entity entity = (Entity) viewers.get(player).getInstance();
                entity.setPosition(location.getX(), location.getY(), location.getZ());
                PacketPlayOutEntity packetPlayOutEntity = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(entity.getId(), (short) ((x) * 4096), (short) (y * 4096), (short) (z * 4096), ((byte) ((loc.getYaw() % 360) * 256 / 360)), ((byte) ((loc.getPitch() % 360) * 256 / 360)), true);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutEntity);
                */
            }
        } else {

        }
    }

    @Override
    public NPCType<?> getType() {
        return type;
    }

    @Override
    public void setData(Object data) {
        if (isDropped()) return;
        this.data = data;
    }

    @Override
    public Object getData() {
        return data;
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

    @Override
    public void look(boolean look) {
        if (isDropped()) return;
        this.look = look;
    }

    @Override
    public void lookAt(Player player, Location target) {
        if (isDropped()) return;
        EzClass Entity = new EzClass(Ref.getNmsOrOld("world.entity.Entity", "Entity"));
        Entity.setInstance(viewers.get(player).getInstance());
        float height = (float) Entity.invokeMethod("getHeadHeight", new Class[0], new Object[0]);
        Location loc = location.clone().add(0.0, height, 0.0);
        loc.setDirection(target.clone().subtract(loc).toVector());
        float yaw = loc.getYaw();
        float pitch = loc.getPitch();
        EzClass PacketPlayOutEntityLook = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutEntity$PacketPlayOutEntityLook", "PacketPlayOutEntity$PacketPlayOutEntityLook"));
        PacketPlayOutEntityLook.setConstructor(int.class, byte.class, byte.class, boolean.class);
        int id = (int) Entity.invokeMethod("getId", new Class[0], new Object[0]);
        PacketPlayOutEntityLook.newInstance(id, ((byte) ((yaw % 360) * 256 / 360)), ((byte) ((pitch % 360) * 256 / 360)), false);
        EzClass PacketPlayOutEntityHeadRotation = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutEntityHeadRotation", "PacketPlayOutEntityHeadRotation"));
        PacketPlayOutEntityHeadRotation.setConstructor(Entity.getInstanceClass(), byte.class);
        PacketPlayOutEntityHeadRotation.newInstance(Entity.getInstance(), ((byte) ((yaw % 360) * 256 / 360)));
        PlayerUtils.sendPacket(player, PacketPlayOutEntityLook.getInstance());
        PlayerUtils.sendPacket(player, PacketPlayOutEntityHeadRotation.getInstance());
    }

    /*
    public void moveTo(Player player, Location location) {
        if (isDropped()) return;
        EzClass Entity = new EzClass(Ref.getNmsOrOld("world.entity.Entity", "Entity"));
        Entity.setInstance(viewers.get(player).getInstance());
        if (!look) {
            lookAt(player, location);
        }
        EzClass PacketPlayOutRelEntityMove = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutEntity$PacketPlayOutRelEntityMove", "PacketPlayOutEntity$PacketPlayOutRelEntityMove"));
        PacketPlayOutRelEntityMove.setConstructor(int.class, short.class, short.class, short.class, boolean.class);
        int id = (int) Entity.invokeMethod("getId", new Class[0], new Object[0]);
        PacketPlayOutRelEntityMove.newInstance(id, ((short) location.getX()), ((short) location.getY()), ((short) location.getZ()), false);
        PlayerUtils.sendPacket(player, PacketPlayOutRelEntityMove.getInstance());
    }
    */

    @Override
    public void setLocation(Location location) {
        if (isDropped()) return;
        Location result = location.clone();
        result.setWorld(this.location.getWorld());
        this.location = result;
        if (hasShown.size() > 0) {
            for (Player player : new ArrayList<>(hasShown)) {
                refresh(player);
            }
        }
    }

    private void reload() {
        List<Player> players = new ArrayList<>(viewers.keySet());
        for (Player player : players) {
            removeViewer(player);
            addViewer(player);
            reloadData(player);
            refresh(player);
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
                            lookAt(player, player.getLocation().clone().add(0.0, player.isSneaking() ? 1.32 : 1.62, 0.0));
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
            EzClass Entity = new EzClass(Ref.getNmsOrOld("world.entity.Entity", "Entity"));
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
            EzClass Entity = new EzClass(Ref.getNmsOrOld("world.entity.Entity", "Entity"));
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
                EzClass EntityLiving = new EzClass(Ref.getNmsOrOld("world.entity.EntityLiving", "EntityLiving"));
                if (EntityLiving.getInstanceClass().isInstance(this.viewers.get(player).getInstance())) {
                    List<Pair<Object, Object>> list = new ArrayList<>();
                    EzEnum EnumItemSlot = new EzEnum(Ref.getNmsOrOld("world.entity.EnumItemSlot", "EnumItemSlot"));
                    if (Ref.getVersion() >= 16) {
                        if (main_hand.getType() != Material.AIR) list.add(new Pair<>(EnumItemSlot.valueOf("a"), ItemUtils.asNMSCopy(main_hand)));
                        if (off_hand.getType() != Material.AIR) list.add(new Pair<>(EnumItemSlot.valueOf("b"), ItemUtils.asNMSCopy(off_hand)));
                        if (head.getType() != Material.AIR) list.add(new Pair<>(EnumItemSlot.valueOf("f"), ItemUtils.asNMSCopy(head)));
                        if (chest.getType() != Material.AIR) list.add(new Pair<>(EnumItemSlot.valueOf("e"), ItemUtils.asNMSCopy(chest)));
                        if (legs.getType() != Material.AIR) list.add(new Pair<>(EnumItemSlot.valueOf("d"), ItemUtils.asNMSCopy(legs)));
                        if (feet.getType() != Material.AIR) list.add(new Pair<>(EnumItemSlot.valueOf("c"), ItemUtils.asNMSCopy(feet)));
                    } else {
                        if (main_hand.getType() != Material.AIR) list.add(new Pair<>(EnumItemSlot.valueOf("MAINHAND"), ItemUtils.asNMSCopy(main_hand)));
                        if (off_hand.getType() != Material.AIR) list.add(new Pair<>(EnumItemSlot.valueOf("OFFHAND"), ItemUtils.asNMSCopy(off_hand)));
                        if (head.getType() != Material.AIR) list.add(new Pair<>(EnumItemSlot.valueOf("HEAD"), ItemUtils.asNMSCopy(head)));
                        if (chest.getType() != Material.AIR) list.add(new Pair<>(EnumItemSlot.valueOf("CHEST"), ItemUtils.asNMSCopy(chest)));
                        if (legs.getType() != Material.AIR) list.add(new Pair<>(EnumItemSlot.valueOf("LEGS"), ItemUtils.asNMSCopy(legs)));
                        if (feet.getType() != Material.AIR) list.add(new Pair<>(EnumItemSlot.valueOf("FEET"), ItemUtils.asNMSCopy(feet)));
                    }
                    if (list.size() > 0) {
                        EzClass PacketPlayOutEntityEquipment = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutEntityEquipment", "PacketPlayOutEntityEquipment"));
                        PacketPlayOutEntityEquipment.setConstructor(int.class, List.class);
                        PacketPlayOutEntityEquipment.newInstance(Entity.invokeMethod("getId", new Class[0], new Object[0]), list);
                        PlayerUtils.sendPacket(player, PacketPlayOutEntityEquipment.getInstance());
                    }
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

    @Override
    public void drop() {
        if (!dropped) {
            removeAll();
            protocol.close();
            dropped = true;
        }
    }

    @Override
    public boolean isDropped() {
        return dropped;
    }

}
