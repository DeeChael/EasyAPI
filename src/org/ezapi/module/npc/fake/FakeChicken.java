package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

public final class FakeChicken extends FakeLiving {

    public FakeChicken() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.animal.EntityChicken", "EntityChicken"), "CHICKEN", "l", name, location);
    }

    @Override
    public void data(Object entity, Object data) {}

}
