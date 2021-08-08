package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

import java.util.List;

public abstract class FakeEntity {

    protected FakeEntity(Class<?> nmsEntityClass) {
        EzClass Entity = new EzClass(Ref.getNmsOrOld("world.entity.Entity", "Entity"));
        if (!Entity.getInstanceClass().isAssignableFrom(nmsEntityClass)) throw new IllegalArgumentException("Need a nms entity class");
    }

    public abstract EzClass create(String name, Location location);

    public abstract List<EzClass> packet(Object entity);

    public abstract void data(Object entity, Object data);

}
