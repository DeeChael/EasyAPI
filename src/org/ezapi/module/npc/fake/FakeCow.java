package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

public final class FakeCow extends FakeLiving {

    public FakeCow() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.animal.EntityCow", "EntityCow"), "COW", "n", name, location);
    }

    @Override
    public void data(Object entity, Object data) {}

}
