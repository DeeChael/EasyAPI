package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.module.npc.NPCType;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;
import sun.reflect.Reflection;

import java.lang.reflect.InvocationTargetException;

public final class FakeBlaze extends FakeEntityLiving {

    public FakeBlaze() {
        if (Reflection.getCallerClass() != NPCType.class) {
            throw new RuntimeException("You cannot call this class without NPCType class!!");
        }
    }

    @Override
    public EzClass create(String name, Location location) {
        EzClass EntityBlaze = new EzClass(Ref.getNmsOrOld("world.entity.monster.EntityBlaze", "EntityBlaze"));
        EzClass World = new EzClass(Ref.getNmsOrOld("world.level.World", "World"));
        EzClass EntityTypes = new EzClass(Ref.getNmsOrOld("world.entity.EntityTypes", "EntityTypes"));
        EzClass ChatMessage = new EzClass(Ref.getNmsOrOld("network.chat.ChatMessage", "ChatMessage"));
        ChatMessage.setConstructor(String.class);
        ChatMessage.newInstance(name);
        try {
            World.setInstance(location.getWorld().getClass().getMethod("getHandle").invoke(location.getWorld()));
            if (Ref.getVersion() >= 11) {
                if (Ref.getVersion() >= 16) {
                    EntityTypes.setInstance(EntityTypes.getStaticField("h"));
                } else {
                    EntityTypes.setInstance(EntityTypes.getStaticField("BLAZE"));
                }
                EntityBlaze.setConstructor(EntityTypes.getInstanceClass(), World.getInstanceClass());
                EntityBlaze.newInstance(EntityTypes.getInstance(), World.getInstance());
            } else {
                EntityBlaze.setConstructor(World.getInstanceClass());
                EntityBlaze.newInstance(World.getInstance());
            }
            EzClass Entity = new EzClass(Ref.getNmsOrOld("world.entity.Entity", "Entity"));
            Entity.setInstance(EntityBlaze.getInstance());
            Entity.invokeMethod("setCustomName", new Class[]{Ref.getNmsOrOld("network.chat.IChatBaseComponent", "IChatBaseComponent")}, new Object[]{ChatMessage.getInstance()});
            Entity.invokeMethod("setLocation", new Class[] {double.class, double.class, double.class, float.class, float.class}, new Object[] {location.getX(), location.getY(), location.getZ(), 0.0f, 0.0f});
            Entity.invokeMethod("setNoGravity", new Class[]{boolean.class}, new Object[]{true});
            Entity.invokeMethod("setCustomNameVisible", new Class[]{boolean.class}, new Object[]{true});
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return EntityBlaze;
    }

    @Override
    public void data(Object entity, Object data) {
    }

}
