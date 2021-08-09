package org.ezapi.module.npc.fake;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.ezapi.reflect.EzClass;
import org.ezapi.reflect.EzEnum;
import org.ezapi.util.Ref;

public final class FakeSheep extends FakeLiving {

    public FakeSheep() {
    }

    @Override
    public EzClass create(String name, Location location) {
        return this.create(Ref.getNmsOrOld("world.entity.animal.EntitySheep", "EntitySheep"), "SHEEP", "ax", name, location);
    }

    @Override
    public void data(Object entity, Object data) {
        if (data instanceof SheepData) {
            SheepData sheepData = (SheepData) data;
            EzEnum EnumColor = sheepData.getNms();
            EzClass EntitySheep = new EzClass(Ref.getNmsOrOld("world.entity.animal.EntitySheep", "EntitySheep"));
            EntitySheep.setInstance(entity);
            EntitySheep.invokeMethod("setColor", new Class[] {EnumColor.getInstanceEnum()}, new Object[] {EnumColor.getInstance()});
            EzClass EntityAgeable = new EzClass(Ref.getNmsOrOld("world.entity.EntityAgeable", "EntityAgeable"));
            EntityAgeable.setInstance(entity);
            EntityAgeable.invokeMethod("setBaby", new Class[] {boolean.class}, new Object[] {sheepData.isBaby()});
        }
    }

    public static class SheepData {

        private final DyeColor color;

        private final boolean isBaby;

        public SheepData(DyeColor color, boolean isBaby) {
            this.color = color != null ? color : DyeColor.WHITE;
            this.isBaby = isBaby;
        }

        public boolean isBaby() {
            return isBaby;
        }

        public DyeColor getColor() {
            return color;
        }

        public EzEnum getNms() {
            EzEnum EnumColor = new EzEnum(Ref.getNmsOrOld("world.item.EnumColor", "EnumColor"));
            if (Ref.getVersion() >= 16) {
                switch (color) {
                    case ORANGE:
                        EnumColor.newInstance("b");
                        break;
                    case MAGENTA:
                        EnumColor.newInstance("c");
                        break;
                    case LIGHT_BLUE:
                        EnumColor.newInstance("d");
                        break;
                    case YELLOW:
                        EnumColor.newInstance("e");
                        break;
                    case LIME:
                        EnumColor.newInstance("f");
                        break;
                    case PINK:
                        EnumColor.newInstance("g");
                        break;
                    case GRAY:
                        EnumColor.newInstance("h");
                        break;
                    case LIGHT_GRAY:
                        EnumColor.newInstance("i");
                        break;
                    case CYAN:
                        EnumColor.newInstance("j");
                        break;
                    case PURPLE:
                        EnumColor.newInstance("k");
                        break;
                    case BLUE:
                        EnumColor.newInstance("l");
                        break;
                    case BROWN:
                        EnumColor.newInstance("m");
                        break;
                    case GREEN:
                        EnumColor.newInstance("n");
                        break;
                    case RED:
                        EnumColor.newInstance("o");
                        break;
                    case BLACK:
                        EnumColor.newInstance("p");
                        break;
                    case WHITE:
                    default:
                        EnumColor.newInstance("a");
                        break;
                }
            } else {
                EnumColor.newInstance(color.name());
            }
            return EnumColor;
        }

    }

}
