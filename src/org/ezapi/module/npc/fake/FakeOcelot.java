package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

public final class FakeOcelot extends FakeLiving {

    public FakeOcelot() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.animal.EntityOcelot", "EntityOcelot"), "OCELOT", "ai", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
        if (Ref.getVersion() <= 10) {
            if (data instanceof FakeCat.CatData) {
                FakeCat.CatData catData = (FakeCat.CatData) data;
                EzClass EntityTameableAnimal = new EzClass(Ref.getNmsOrOld("world.entity.EntityTameableAnimal", "EntityTameableAnimal"));
                EntityTameableAnimal.setInstance(entity);
                EntityTameableAnimal.invokeMethod("setTamed", new Class[] {boolean.class}, new Object[] {catData.isTamed()});
                EntityTameableAnimal.invokeMethod("setSitting", new Class[] {boolean.class}, new Object[] {catData.isSitting()});
                EzClass EntityAgeable = new EzClass(Ref.getNmsOrOld("world.entity.EntityAgeable", "EntityAgeable"));
                EntityAgeable.setInstance(entity);
                EntityAgeable.invokeMethod("setBaby", new Class[] {boolean.class}, new Object[] {catData.isBaby()});
            }
        } else {
            if (data instanceof Boolean) {
                boolean isBaby = (boolean) data;
                EzClass EntityAgeable = new EzClass(Ref.getNmsOrOld("world.entity.EntityAgeable", "EntityAgeable"));
                EntityAgeable.setInstance(entity);
                EntityAgeable.invokeMethod("setBaby", new Class[] {boolean.class}, new Object[] {isBaby});
            }
        }
    }

}
