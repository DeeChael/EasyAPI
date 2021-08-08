package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

public final class FakeVillager extends FakeLiving {

    public FakeVillager() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.npc.EntityVillager", "EntityVillager"), "VILLAGER", "aV", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
        if (Ref.getVersion() <= 10) return;
        if (data instanceof VillagerData) {
            VillagerData villagerData = (VillagerData) data;
            EzClass VillagerData = villagerData.getNms();
            EzClass EntityVillager = new EzClass(Ref.getNmsOrOld("world.entity.npc.EntityVillager", "EntityVillager"));
            EntityVillager.setInstance(entity);
            EzClass EntityAgeable = new EzClass(Ref.getNmsOrOld("world.entity.EntityAgeable", "EntityAgeable"));
            EntityAgeable.setInstance(entity);
            EntityAgeable.invokeMethod("setBaby", new Class[] {boolean.class}, new Object[] {villagerData.isBaby()});
            EntityVillager.invokeMethod("setVillagerData", new Class[] {VillagerData.getInstanceClass()}, new Object[] {VillagerData.getInstance()});
        }
    }

    public static class VillagerData {

        private final VillagerProfession profession;

        private final VillagerType type;

        private final boolean isBaby;

        public VillagerData(VillagerProfession profession, VillagerType type, boolean isBaby) {
            if (Ref.getVersion() <= 10) throw new RuntimeException("Minecraft Server is less than 1.14 so cannot set villager data");
            this.profession = profession != null ? profession : VillagerProfession.NONE;
            this.type = type != null ? type : VillagerType.PLAINS;
            this.isBaby = isBaby;
        }

        public boolean isBaby() {
            return isBaby;
        }

        public VillagerProfession getProfession() {
            return profession;
        }

        public VillagerType getType() {
            return type;
        }

        public EzClass getNms() {
            EzClass VillagerData = new EzClass(Ref.getNmsOrOld("world.entity.npc.VillagerData", "VillagerData"));
            VillagerData.setConstructor(Ref.getNmsOrOld("world.entity.npc.VillagerType", "VillagerType"), Ref.getNmsOrOld("world.entity.npc.VillagerProfession", "VillagerProfession"), int.class);
            VillagerData.newInstance(type.getNms(), profession.getNms(), 0);
            return VillagerData;
        }

    }

    public enum VillagerType {
        DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA;

        public EzClass getNms() {
            EzClass VillagerType = new EzClass(Ref.getNmsOrOld("world.entity.npc.VillagerType", "VillagerType"));
            if (Ref.getVersion() >= 16) {
                switch (this) {
                    case DESERT:
                        VillagerType.setInstance(VillagerType.getStaticField("a"));
                        break;
                    case JUNGLE:
                        VillagerType.setInstance(VillagerType.getStaticField("b"));
                        break;
                    case SAVANNA:
                        VillagerType.setInstance(VillagerType.getStaticField("d"));
                        break;
                    case SNOW:
                        VillagerType.setInstance(VillagerType.getStaticField("e"));
                        break;
                    case SWAMP:
                        VillagerType.setInstance(VillagerType.getStaticField("f"));
                        break;
                    case TAIGA:
                        VillagerType.setInstance(VillagerType.getStaticField("g"));
                        break;
                    case PLAINS:
                    default:
                        VillagerType.setInstance(VillagerType.getStaticField("c"));
                        break;
                }
            } else {
                VillagerType.setInstance(VillagerType.getStaticField(this.name()));
            }
            return VillagerType;
        }
    }

    public enum VillagerProfession {

        NONE, ARMORER, BUTCHER, CARTOGRAPHER, CLERIC, FARMER, FISHERMAN, FLETCHER, LEATHERWORKER, LIBRARIAN, MASON, NITWIT, SHEPHERD, TOOLSMITH, WEAPONSMITH;

        public EzClass getNms() {
            EzClass VillagerProfession = new EzClass(Ref.getNmsOrOld("world.entity.npc.VillagerProfession", "VillagerProfession"));
            if (Ref.getVersion() >= 16) {
                switch (this) {
                    case ARMORER:
                        VillagerProfession.setInstance(VillagerProfession.getStaticField("b"));
                        break;
                    case BUTCHER:
                        VillagerProfession.setInstance(VillagerProfession.getStaticField("c"));
                        break;
                    case CARTOGRAPHER:
                        VillagerProfession.setInstance(VillagerProfession.getStaticField("d"));
                        break;
                    case CLERIC:
                        VillagerProfession.setInstance(VillagerProfession.getStaticField("e"));
                        break;
                    case FARMER:
                        VillagerProfession.setInstance(VillagerProfession.getStaticField("f"));
                        break;
                    case FISHERMAN:
                        VillagerProfession.setInstance(VillagerProfession.getStaticField("g"));
                        break;
                    case FLETCHER:
                        VillagerProfession.setInstance(VillagerProfession.getStaticField("h"));
                        break;
                    case LEATHERWORKER:
                        VillagerProfession.setInstance(VillagerProfession.getStaticField("i"));
                        break;
                    case LIBRARIAN:
                        VillagerProfession.setInstance(VillagerProfession.getStaticField("j"));
                        break;
                    case MASON:
                        VillagerProfession.setInstance(VillagerProfession.getStaticField("k"));
                        break;
                    case NITWIT:
                        VillagerProfession.setInstance(VillagerProfession.getStaticField("l"));
                        break;
                    case SHEPHERD:
                        VillagerProfession.setInstance(VillagerProfession.getStaticField("m"));
                        break;
                    case TOOLSMITH:
                        VillagerProfession.setInstance(VillagerProfession.getStaticField("n"));
                        break;
                    case WEAPONSMITH:
                        VillagerProfession.setInstance(VillagerProfession.getStaticField("o"));
                        break;
                    case NONE:
                    default:
                        VillagerProfession.setInstance(VillagerProfession.getStaticField("a"));
                        break;
                }
            } else {
                VillagerProfession.setInstance(VillagerProfession.getStaticField(this.name()));
            }
            return VillagerProfession;
        }

    }

}
