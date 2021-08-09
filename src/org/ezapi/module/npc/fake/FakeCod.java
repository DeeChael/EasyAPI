package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

public final class FakeCod extends FakeLiving {

    public FakeCod() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.animal.EntityCod", "EntityCod"), "COD", "m", name, location);
    }

    @Override
    public void data(Object entity, Object data) {}

}
