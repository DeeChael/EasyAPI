package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

public final class FakeHoglin extends FakeLiving {

    public FakeHoglin() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.monster.EntityHoglin", "EntityHoglin"), "HOGLIN", "L", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
    }

}
