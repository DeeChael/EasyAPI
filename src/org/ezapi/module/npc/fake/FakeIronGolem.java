package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

public final class FakeIronGolem extends FakeLiving {

    public FakeIronGolem() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.animal.EntityIronGolem", "EntityIronGolem"), "IRON_GOLEM", "P", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
        if (data instanceof CrackLevel) {
            CrackLevel level = (CrackLevel) data;
            EzClass EntityLiving = new EzClass(Ref.getNmsOrOld("world.entity.EntityLiving", "EntityLiving"));
            EntityLiving.setInstance(entity);
            float health = (float) EntityLiving.invokeMethod("getMaxHealth", new Class[0], new Object[0]);
            if (level.equals(CrackLevel.LOW))  {
                health = (float) (health * 0.9);
            } else if (level.equals(CrackLevel.MEDIUM)) {
                health = (float) (health * 0.6);
            } else if (level.equals(CrackLevel.HIGH)) {
                health = (float) (health * 0.3);
            }
            EntityLiving.invokeMethod("setHealth", new Class[] {float.class}, new Object[] {health});
        }
    }

    public static enum CrackLevel {
        NONE, LOW, MEDIUM, HIGH;
    }

}
