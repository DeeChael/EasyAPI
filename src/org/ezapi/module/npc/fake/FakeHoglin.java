package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

public final class FakeHoglin extends FakeLiving {

    public FakeHoglin() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.monster.hoglin.EntityHoglin", "EntityHoglin"), "HOGLIN", "L", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
        if (data instanceof Boolean) {
            EzClass EntityAgeable = new EzClass(Ref.getNmsOrOld("world.entity.EntityAgeable", "EntityAgeable"));
            EntityAgeable.setInstance(entity);
            EntityAgeable.invokeMethod("setBaby", new Class[] {boolean.class}, new Object[] {data});
        }
    }

}
