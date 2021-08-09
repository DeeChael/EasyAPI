package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

public final class FakeZoglin extends FakeLiving {

    public FakeZoglin() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.monster.EntityZoglin", "EntityZoglin"), "ZOGLIN", "bd", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
        if (data instanceof Boolean) {
            EzClass EntityZoglin = new EzClass(Ref.getNmsOrOld("world.entity.monster.EntityZoglin", "EntityZoglin"));
            EntityZoglin.setInstance(entity);
            EntityZoglin.invokeMethod("setBaby", new Class[] {boolean.class}, new Object[] {data});
        }
    }

}
