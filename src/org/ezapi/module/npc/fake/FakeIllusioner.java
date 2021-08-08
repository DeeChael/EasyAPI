package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

public final class FakeIllusioner extends FakeLiving {

    public FakeIllusioner() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.monster.EntityIllagerIllusioner", "EntityIllagerIllusioner"), "ILLUSIONER", "O", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
    }

}
