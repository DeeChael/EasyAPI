package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

public final class FakePigZombie extends FakeLiving {

    public FakePigZombie() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.monster.EntityPigZombie", "EntityPigZombie"), Ref.getVersion() <= 12 ? "ZOMBIE_PIGMAN" : "ZOMBIFIED_PIGLIN", "bh", name, location);
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
