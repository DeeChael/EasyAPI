package org.ezapi.module.npc.fake;

import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.reflect.EzEnum;
import org.ezapi.util.Ref;

public final class FakePanda extends FakeLiving {

    public FakePanda() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.animal.EntityPanda", "EntityPanda"), "PANDA", "ak", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
        if (data instanceof PandaData) {
            PandaData pandaData = (PandaData) data;
            boolean isBaby = pandaData.isBaby();
            EzEnum gene = pandaData.getGene().getNms();
            EzClass EntityPanda = new EzClass(Ref.getNmsOrOld("world.entity.animal.EntityPanda", "EntityPanda"));
            EntityPanda.setInstance(entity);
            EntityPanda.invokeMethod("setMainGene", new Class[] {gene.getInstanceEnum()}, new Object[] {gene.getInstance()});
            EzClass EntityAgeable = new EzClass(Ref.getNmsOrOld("world.entity.EntityAgeable", "EntityAgeable"));
            EntityAgeable.setInstance(entity);
            EntityAgeable.invokeMethod("setBaby", new Class[] {boolean.class}, new Object[] {isBaby});
        }
    }

    public static class PandaData {

        private final PandaGene gene;

        private final boolean isBaby;

        public PandaData(PandaGene gene, boolean isBaby) {
            this.gene = gene != null ? gene : PandaGene.NORMAL;
            this.isBaby = isBaby;
        }

        public boolean isBaby() {
            return isBaby;
        }

        public PandaGene getGene() {
            return gene;
        }

    }

    public static enum PandaGene {
        NORMAL, LAZY, WORRIED, PLAYFUL, BROWN, WEAK, AGGRESSIVE;

        public EzEnum getNms() {
            EzEnum Gene = new EzEnum(Ref.getInnerClass(Ref.getNmsOrOld("world.entity.animal.EntityPanda", "EntityPanda"), "Gene"));
            if (Ref.getVersion() >= 16) {
                switch (this) {
                    case LAZY:
                        Gene.newInstance("b");
                        break;
                    case WORRIED:
                        Gene.newInstance("c");
                        break;
                    case PLAYFUL:
                        Gene.newInstance("d");
                        break;
                    case BROWN:
                        Gene.newInstance("e");
                        break;
                    case WEAK:
                        Gene.newInstance("f");
                        break;
                    case AGGRESSIVE:
                        Gene.newInstance("g");
                        break;
                    case NORMAL:
                    default:
                        Gene.newInstance("a");
                        break;
                }
            } else {
                Gene.newInstance(this.name());
            }
            return Gene;
        }

    }

}
