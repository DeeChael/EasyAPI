package org.ezapi.module.npc.fake;

import org.ezapi.reflect.EzClass;
import org.ezapi.util.ReflectionUtils;

public abstract class FakeEntity {

    protected FakeEntity(Class<?> nmsEntityClass) {
        EzClass Entity = new EzClass(ReflectionUtils.getNmsOrOld("world.entity.Entity", "Entity"));
        if (!Entity.getInstanceClass().isAssignableFrom(nmsEntityClass)) throw new IllegalArgumentException("Need a nms entity class");
    }

}
