package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.module.npc.NPCType;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;
import sun.reflect.Reflection;

public final class FakeMagmaCube extends FakeLiving {

    public FakeMagmaCube() {
        if (Reflection.getCallerClass() != NPCType.class) {
            throw new RuntimeException("You cannot call this class without NPCType class!!");
        }
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.monster.EntityMagmaCube", "EntityMagmaCube"), "MAGMA_CUBE", "X", name, location);
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
