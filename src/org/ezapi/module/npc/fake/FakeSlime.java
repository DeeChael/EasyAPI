package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

public final class FakeSlime extends FakeLiving {

    public FakeSlime() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.monster.EntitySlime", "EntitySlime"), "SLIME", "aD", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
        if (data instanceof Integer) {
            int i = (int) data;
            if (i > 0) {
                EzClass EntitySlime = new EzClass(Ref.getNmsOrOld("world.entity.monster.EntitySlime", "EntitySlime"));
                EntitySlime.setInstance(entity);
                EntitySlime.invokeMethod("setSize", new Class[] {int.class, boolean.class}, new Object[] {i, true});
            }
        }
    }

}
