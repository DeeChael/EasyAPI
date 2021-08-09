package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

public final class FakeCat extends FakeLiving {

    public FakeCat() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.animal.EntityCat", "EntityCat"), "CAT", "j", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
        if (data instanceof CatData) {
            CatData catData = (CatData) data;
            EzClass EntityCat = new EzClass(Ref.getNmsOrOld("world.entity.animal.EntityCat", "EntityCat"));
            EntityCat.setInstance(entity);
            EntityCat.invokeMethod("setCatType", new Class[] {int.class}, new Object[] {catData.getType().getType()});
            EzClass EntityTameableAnimal = new EzClass(Ref.getNmsOrOld("world.entity.EntityTameableAnimal", "EntityTameableAnimal"));
            EntityTameableAnimal.setInstance(entity);
            EntityTameableAnimal.invokeMethod("setTamed", new Class[] {boolean.class}, new Object[] {catData.isTamed()});
            EntityTameableAnimal.invokeMethod("setSitting", new Class[] {boolean.class}, new Object[] {catData.isSitting()});
            EzClass EntityAgeable = new EzClass(Ref.getNmsOrOld("world.entity.EntityAgeable", "EntityAgeable"));
            EntityAgeable.setInstance(entity);
            EntityAgeable.invokeMethod("setBaby", new Class[] {boolean.class}, new Object[] {catData.isBaby});
        }
    }

    public static class CatData {

        private final CatType type;

        private final boolean isTamed;

        private final boolean isSitting;

        private final boolean isBaby;

        public CatData(CatType type, boolean isTamed, boolean isSitting, boolean isBaby) {
            this.type = type;
            this.isTamed = isTamed;
            this.isSitting = isSitting;
            this.isBaby = isBaby;
        }

        public CatType getType() {
            return type;
        }

        public boolean isTamed() {
            return isTamed;
        }

        public boolean isSitting() {
            return isSitting;
        }

        public boolean isBaby() {
            return isBaby;
        }

    }

    public static enum CatType {
        TABBY(0), BLACK(1), RED(2), SIAMESE(3), BRITISH_SHORTHAIR(4), CALICO(5), PERSIAN(6), RAGDOLL(7), WHITE(8), JELLIE(9), ALL_BLACK(10);

        private final int type;

        CatType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

    }

}
