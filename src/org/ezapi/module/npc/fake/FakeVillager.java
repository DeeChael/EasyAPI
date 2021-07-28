package org.ezapi.module.npc.fake;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public final class FakeVillager extends FakeEntity {

    public FakeVillager() {
        super(ReflectionUtils.getNmsOrOld("world.entity.npc.EntityVillager", "EntityVillager"));
    }

    @Override
    public EzClass create(String name, Location location) {
        EzClass EntityVillager = new EzClass(ReflectionUtils.getNmsOrOld("world.entity.npc.EntityVillager", "EntityVillager"));
        EzClass MinecraftServer = new EzClass(ReflectionUtils.getNmsOrOld("server.MinecraftServer", "MinecraftServer"));
        EzClass World = new EzClass(ReflectionUtils.getNmsOrOld("world.level.World", "World"));
        EzClass EntityTypes = new EzClass(ReflectionUtils.getNmsOrOld("world.entity.EntityTypes", "EntityTypes"));
        EzClass ChatMessage = new EzClass(ReflectionUtils.getNmsOrOld("network.chat.ChatMessage", "ChatMessage"));
        ChatMessage.setConstructor(String.class);
        ChatMessage.newInstance(name);
        try {
            EzClass CraftServer = new EzClass(ReflectionUtils.getObcClass("CraftServer"));
            CraftServer.setInstance(Bukkit.getServer());
            MinecraftServer.setInstance(CraftServer.invokeMethod("getServer", new Class[0], new Object[0]));
            World.setInstance(location.getWorld().getClass().getMethod("getHandle").invoke(location.getWorld()));
            if (ReflectionUtils.getVersion() >= 11) {
                if (ReflectionUtils.getVersion() >= 16) {
                    EntityTypes.setInstance(EntityTypes.getStaticField("aV"));
                } else {
                    EntityTypes.setInstance(EntityTypes.getStaticField("VILLAGER"));
                }
                EntityVillager.setConstructor(EntityTypes.getInstanceClass(), World.getInstanceClass());
                EntityVillager.newInstance(EntityTypes.getInstance(), World.getInstance());
            } else {
                EntityVillager.setConstructor(World.getInstanceClass());
                EntityVillager.newInstance(World.getInstance());
            }
            EzClass Entity = new EzClass(ReflectionUtils.getNmsOrOld("world.entity.Entity", "Entity"));
            Entity.setInstance(EntityVillager.getInstance());
            Entity.invokeMethod("setCustomName", new Class[]{ReflectionUtils.getNmsOrOld("network.chat.IChatBaseComponent", "IChatBaseComponent")}, new Object[]{ChatMessage.getInstance()});
            Entity.invokeMethod("setLocation", new Class[] {double.class, double.class, double.class, float.class, float.class}, new Object[] {location.getX(), location.getY(), location.getZ(), 0.0f, 0.0f});
            Entity.invokeMethod("setNoGravity", new Class[]{boolean.class}, new Object[]{true});
            Entity.invokeMethod("setCustomNameVisible", new Class[]{boolean.class}, new Object[]{true});
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return EntityVillager;
    }

    @Override
    public List<EzClass> packet(Object entity) {
        EzClass EntityLiving = new EzClass(ReflectionUtils.getNmsOrOld("world.entity.EntityLiving", "EntityLiving"));
        EzClass Entity = new EzClass(ReflectionUtils.getNmsOrOld("world.entity.Entity", "Entity"));
        Entity.setInstance(entity);
        int id = (int) Entity.invokeMethod("getId", new Class[0], new Object[0]);
        EzClass PacketPlayOutSpawnEntityLiving = new EzClass(ReflectionUtils.getNmsOrOld("network.protocol.game.PacketPlayOutSpawnEntityLiving", "PacketPlayOutSpawnEntityLiving"));
        PacketPlayOutSpawnEntityLiving.setConstructor(EntityLiving.getInstanceClass());
        PacketPlayOutSpawnEntityLiving.newInstance(entity);
        EzClass PacketPlayOutEntityMetadata = new EzClass(ReflectionUtils.getNmsOrOld("network.protocol.game.PacketPlayOutEntityMetadata", "PacketPlayOutEntityMetadata"));
        PacketPlayOutEntityMetadata.setConstructor(int.class, ReflectionUtils.getNmsOrOld("network.syncher.DataWatcher", "DataWatcher"), boolean.class);
        PacketPlayOutEntityMetadata.newInstance(id, Entity.invokeMethod("getDataWatcher", new Class[0], new Object[0]), true);
        List<EzClass> list = new ArrayList<>();
        list.add(PacketPlayOutSpawnEntityLiving);
        list.add(PacketPlayOutEntityMetadata);
        return list;
    }

    @Override
    public void data(Object entity, Object data) {
        if (!(ReflectionUtils.getVersion() >= 11)) return;
        if (data instanceof VillagerData) {

        }
    }

    public static class VillagerData {

        private final VillagerProfession profession;

        private final VillagerType type;

        private final boolean isBaby;

        public VillagerData(VillagerProfession profession, VillagerType type, boolean isBaby) {
            if (ReflectionUtils.getVersion() <= 10) throw new RuntimeException("Minecraft Server is less than 1.14 so cannot set villager data");
            this.profession = profession;
            this.type = type;
            this.isBaby = isBaby;
        }

        public boolean isBaby() {
            return isBaby;
        }

        public VillagerProfession getProfession() {
            return profession;
        }

        public VillagerType getType() {
            return type;
        }

    }

    public enum VillagerType {
        DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA;
    }

    public enum VillagerProfession {

        NONE, ARMORER, BUTCHER, CARTOGRAPHER, CLERIC, FARMER, FISHERMAN, FLETCHER, LEATHERWORKER, LIBRARIAN, MASON, NITWIT, SHEPHERD, TOOLSMITH, WEAPONSMITH;

    }

}
