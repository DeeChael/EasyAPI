package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

public final class FakeDrowned extends FakeLiving {

    public FakeDrowned() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.monster.EntityDrowned", "EntityDrowned"), "DROWNED", "s", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
        if (data instanceof Boolean) {
            EzClass EntityZombie = new EzClass(Ref.getNmsOrOld("world.entity.monster.EntityZombie", "EntityZombie"));
            EntityZombie.setInstance(entity);
            EntityZombie.invokeMethod("setBaby", new Class[] {boolean.class}, new Object[] {data});
        }
    }

}
