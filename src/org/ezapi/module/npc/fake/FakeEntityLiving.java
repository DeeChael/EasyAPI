package org.ezapi.module.npc.fake;

import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

import java.util.ArrayList;
import java.util.List;

public abstract class FakeEntityLiving extends FakeEntity {

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
