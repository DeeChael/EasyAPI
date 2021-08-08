package org.ezapi.module.bossbar;

import org.ezapi.reflect.EzClass;
import org.ezapi.reflect.EzEnum;
import org.ezapi.util.ArrayUtils;
import org.ezapi.util.Ref;

public enum BarColor {

    PINK,
    BLUE,
    RED,
    GREEN,
    YELLOW,
    PURPLE,
    WHITE;

    public org.bukkit.boss.BarColor getBukkit() {
        switch (this) {
            case RED:
                return org.bukkit.boss.BarColor.RED;
            case BLUE:
                return org.bukkit.boss.BarColor.BLUE;
            case GREEN:
                return org.bukkit.boss.BarColor.GREEN;
            case YELLOW:
                return org.bukkit.boss.BarColor.YELLOW;
            case WHITE:
                return org.bukkit.boss.BarColor.WHITE;
            case PURPLE:
                return org.bukkit.boss.BarColor.PURPLE;
            case PINK:
            default:
                return org.bukkit.boss.BarColor.PINK;
        }
    }

    public EzEnum getNms() {
        EzEnum BarColor = new EzEnum(Ref.getNmsOrOld("world.BossBattle$BarColor", "BossBattle$BarColor"));
        if (Ref.getVersion() < 16) {
            BarColor.newInstance(this.name());
        } else {
            char[] chars = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
            BarColor.newInstance(String.valueOf(chars[ArrayUtils.index(values(), this)]));
        }
        return BarColor;
    }

}
