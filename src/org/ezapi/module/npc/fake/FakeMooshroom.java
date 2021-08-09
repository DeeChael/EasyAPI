package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.reflect.EzEnum;
import org.ezapi.util.Ref;

public final class FakeMooshroom extends FakeLiving {

    public FakeMooshroom() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.animal.EntityMushroomCow", "EntityMushroomCow"), "MOOSHROOM", "ah", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
        if (data instanceof boolean[]) {
            boolean[] bools = (boolean[]) data;
            if (bools.length >= 1) {
                EzClass EntityFox = new EzClass(Ref.getNmsOrOld("world.entity.animal.EntityMushroomCow", "EntityMushroomCow"));
                EntityFox.setInstance(entity);
                EzEnum Type = new EzEnum(Ref.getInnerClass(EntityFox.getInstanceClass(), "Type"));
                if (bools[0]) {
                    Type.newInstance(Ref.getVersion() >= 16 ? "b" : "BROWN");
                } else {
                    Type.newInstance(Ref.getVersion() >= 16 ? "a" : "RED");
                }
                if (Ref.getVersion() == 11) {
                    EntityFox.invokeMethod("a", new Class[]{Type.getInstanceEnum()}, new Object[]{Type.getInstance()});
                } else {
                    EntityFox.invokeMethod("setVariant", new Class[]{Type.getInstanceEnum()}, new Object[]{Type.getInstance()});
                }
                if (bools.length >= 2) {
                    EzClass EntityAgeable = new EzClass(Ref.getNmsOrOld("world.entity.EntityAgeable", "EntityAgeable"));
                    EntityAgeable.setInstance(entity);
                    EntityAgeable.invokeMethod("setBaby", new Class[] {boolean.class}, new Object[] {bools[1]});
                }
            }
        }
    }

}
