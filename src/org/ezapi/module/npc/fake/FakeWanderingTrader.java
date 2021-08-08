package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.module.npc.NPCType;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;
import sun.reflect.Reflection;

public final class FakeWanderingTrader extends FakeLiving {

    public FakeWanderingTrader() {
        if (Reflection.getCallerClass() != NPCType.class) {
            throw new RuntimeException("You cannot call this class without NPCType class!!");
        }
    }

    @Override
    public EzClass create(String name, Location location) {
        EzClass WanderingTrader = this.create(Ref.getNmsOrOld("world.entity.monster.EntityCaveSpider", "EntityCaveSpider"), "WANDERING_TRADER", "aX", name, location);
        if (Ref.getVersion() == 11) {
            WanderingTrader.invokeMethod("s", new Class[] {int.class}, new Object[] {-1});
        } else if (Ref.getVersion() >= 12 && Ref.getVersion() <= 15) {
            WanderingTrader.invokeMethod("u", new Class[] {int.class}, new Object[] {-1});
        } else if (Ref.getVersion() >= 16) {
            WanderingTrader.invokeMethod("setDespawnDelay", new Class[] {int.class}, new Object[] {-1});
        }
        return WanderingTrader;
    }

    @Override
    public void data(Object entity, Object data) {
    }

}
