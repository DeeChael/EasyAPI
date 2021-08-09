package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

public final class FakeZombieVillager extends FakeLiving {

    public FakeZombieVillager() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.monster.EntityZombieVillager", "EntityZombieVillager"), "ZOMBIE_VILLAGER", "bg", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
        if (Ref.getVersion() <= 10) return;
        if (data instanceof FakeVillager.VillagerData) {
            FakeVillager.VillagerData villagerData = (FakeVillager.VillagerData) data;
            EzClass VillagerData = villagerData.getNms();
            EzClass EntityZombieVillager = new EzClass(Ref.getNmsOrOld("world.entity.monster.EntityZombieVillager", "EntityZombieVillager"));
            EntityZombieVillager.setInstance(entity);
            EntityZombieVillager.invokeMethod("setVillagerData", new Class[] {VillagerData.getInstanceClass()}, new Object[] {VillagerData.getInstance()});
            EzClass EntityZombie = new EzClass(Ref.getNmsOrOld("world.entity.monster.EntityZombie", "EntityZombie"));
            EntityZombie.setInstance(entity);
            EntityZombie.invokeMethod("setBaby", new Class[] {boolean.class}, new Object[] {villagerData.isBaby()});
        }
    }

}
