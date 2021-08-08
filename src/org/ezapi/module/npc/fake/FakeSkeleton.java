package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

public final class FakeSkeleton extends FakeLiving {

    public FakeSkeleton() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.monster.EntitySkeleton", "EntitySkeleton"), "SKELETON", "aB", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
    }

}
