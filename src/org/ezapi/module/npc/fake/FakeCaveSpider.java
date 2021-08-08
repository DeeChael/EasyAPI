package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.module.npc.NPCType;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;
import sun.reflect.Reflection;

public final class FakeCaveSpider extends FakeLiving {

    public FakeCaveSpider() {
        if (Reflection.getCallerClass() != NPCType.class) {
            throw new RuntimeException("You cannot call this class without NPCType class!!");
        }
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.monster.EntityCaveSpider", "EntityCaveSpider"), "CAVE_SPIDER", "k", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
    }

}
