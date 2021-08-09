package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

public final class FakePiglin extends FakeLiving {

    public FakePiglin() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.monster.piglin.EntityPiglin", "EntityPiglin"), "PIGLIN", "ao", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
        if (data instanceof Boolean) {
            EzClass EntityPiglin = new EzClass(Ref.getNmsOrOld("world.entity.monster.piglin.EntityPiglin", "EntityPiglin"));
            EntityPiglin.setInstance(entity);
            EntityPiglin.invokeMethod("setBaby", new Class[] {boolean.class}, new Object[] {data});
        }
    }

}
