package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class FakeLiving extends FakeEntity {

    protected final EzClass create(Class<?> entityType, String typeOldName, String typeNewName, String name, Location location) {
        EzClass EntityIDontKnow = new EzClass(entityType);
        EzClass World = new EzClass(Ref.getNmsOrOld("world.level.World", "World"));
        EzClass EntityTypes = new EzClass(Ref.getNmsOrOld("world.entity.EntityTypes", "EntityTypes"));
        EzClass ChatMessage = new EzClass(Ref.getNmsOrOld("network.chat.ChatMessage", "ChatMessage"));
        ChatMessage.setConstructor(String.class);
        ChatMessage.newInstance(name);
        try {
            World.setInstance(location.getWorld().getClass().getMethod("getHandle").invoke(location.getWorld()));
            if (Ref.getVersion() >= 11) {
                if (Ref.getVersion() >= 16) {
                    EntityTypes.setInstance(EntityTypes.getStaticField(typeNewName));
                } else {
                    EntityTypes.setInstance(EntityTypes.getStaticField(typeOldName));
                }
                EntityIDontKnow.setConstructor(EntityTypes.getInstanceClass(), World.getInstanceClass());
                EntityIDontKnow.newInstance(EntityTypes.getInstance(), World.getInstance());
            } else {
                EntityIDontKnow.setConstructor(World.getInstanceClass());
                EntityIDontKnow.newInstance(World.getInstance());
            }
            EzClass Entity = new EzClass(Ref.getNmsOrOld("world.entity.Entity", "Entity"));
            Entity.setInstance(EntityIDontKnow.getInstance());
            Entity.invokeMethod("setCustomName", new Class[]{Ref.getNmsOrOld("network.chat.IChatBaseComponent", "IChatBaseComponent")}, new Object[]{ChatMessage.getInstance()});
            Entity.invokeMethod("setLocation", new Class[] {double.class, double.class, double.class, float.class, float.class}, new Object[] {location.getX(), location.getY(), location.getZ(), 0.0f, 0.0f});
            Entity.invokeMethod("setNoGravity", new Class[]{boolean.class}, new Object[]{true});
            Entity.invokeMethod("setCustomNameVisible", new Class[]{boolean.class}, new Object[]{true});
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return EntityIDontKnow;
    }

    @Override
    public List<EzClass> packet(Object entity) {
        EzClass EntityLiving = new EzClass(Ref.getNmsOrOld("world.entity.EntityLiving", "EntityLiving"));
        EzClass Entity = new EzClass(Ref.getNmsOrOld("world.entity.Entity", "Entity"));
        Entity.setInstance(entity);
        int id = (int) Entity.invokeMethod("getId", new Class[0], new Object[0]);
        EzClass PacketPlayOutSpawnEntityLiving = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutSpawnEntityLiving", "PacketPlayOutSpawnEntityLiving"));
        PacketPlayOutSpawnEntityLiving.setConstructor(EntityLiving.getInstanceClass());
        PacketPlayOutSpawnEntityLiving.newInstance(entity);
        EzClass PacketPlayOutEntityMetadata = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutEntityMetadata", "PacketPlayOutEntityMetadata"));
        PacketPlayOutEntityMetadata.setConstructor(int.class, Ref.getNmsOrOld("network.syncher.DataWatcher", "DataWatcher"), boolean.class);
        PacketPlayOutEntityMetadata.newInstance(id, Entity.invokeMethod("getDataWatcher", new Class[0], new Object[0]), true);
        List<EzClass> list = new ArrayList<>();
        list.add(PacketPlayOutSpawnEntityLiving);
        list.add(PacketPlayOutEntityMetadata);
        return list;
    }

}
