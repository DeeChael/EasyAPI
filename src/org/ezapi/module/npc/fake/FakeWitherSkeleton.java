package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.module.npc.NPCType;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;
import sun.reflect.Reflection;

public final class FakeWitherSkeleton extends FakeLiving {

    public FakeWitherSkeleton() {
        if (Reflection.getCallerClass() != NPCType.class) {
            throw new RuntimeException("You cannot call this class without NPCType class!!");
        }
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.monster.EntitySkeletonWither", "EntitySkeletonWither"), "WITHER_SKELETON", "ba", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
    }

}
