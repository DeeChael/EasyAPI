package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;

import java.util.List;

public abstract class FakeEntity {

    protected FakeEntity() {
    }

    public abstract EzClass create(String name, Location location);

    public abstract List<EzClass> packet(Object entity);

    public abstract void data(Object entity, Object data);

}
