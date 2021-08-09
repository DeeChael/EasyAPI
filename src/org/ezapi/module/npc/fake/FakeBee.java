package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

public final class FakeBee extends FakeLiving {

    public FakeBee() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.animal.EntityBee", "EntityBee"), "BEE", "g", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
        if (data instanceof boolean[]) {
            boolean[] bools = (boolean[]) data;
            if (bools.length >= 1) {
                EzClass EntityBee = new EzClass(Ref.getNmsOrOld("world.entity.animal.EntityBee", "EntityBee"));
                EntityBee.setInstance(entity);
                EntityBee.invokeMethod("setHasNectar", new Class[] {boolean.class}, new Object[] {bools[0]});
                if (bools.length >= 2) {
                    EntityBee.invokeMethod("setHasStung", new Class[] {boolean.class}, new Object[] {bools[1]});
                    if (bools.length >= 3) {
                        EzClass EntityAgeable = new EzClass(Ref.getNmsOrOld("world.entity.EntityAgeable", "EntityAgeable"));
                        EntityAgeable.setInstance(entity);
                        EntityAgeable.invokeMethod("setBaby", new Class[] {boolean.class}, new Object[] {bools[2]});
                    }
                }
            }
        }
    }

}
