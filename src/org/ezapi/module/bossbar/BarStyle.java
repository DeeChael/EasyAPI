package org.ezapi.module.bossbar;

import org.ezapi.reflect.EzEnum;
import org.ezapi.util.ArrayUtils;
import org.ezapi.util.Ref;

public enum BarStyle {

    PROGRESS,
    NOTCHED_6,
    NOTCHED_10,
    NOTCHED_12,
    NOTCHED_20;

    public org.bukkit.boss.BarStyle getBukkit() {
        switch (this) {
            case NOTCHED_6:
                return org.bukkit.boss.BarStyle.SEGMENTED_6;
            case NOTCHED_10:
                return org.bukkit.boss.BarStyle.SEGMENTED_10;
            case NOTCHED_12:
                return org.bukkit.boss.BarStyle.SEGMENTED_12;
            case NOTCHED_20:
                return org.bukkit.boss.BarStyle.SEGMENTED_20;
            case PROGRESS:
            default:
                return org.bukkit.boss.BarStyle.SOLID;
        }
    }

    public EzEnum getNms() {
        EzEnum BarStyle = new EzEnum(Ref.getNmsOrOld("world.BossBattle$BarStyle", "BossBattle$BarStyle"));
        if (Ref.getVersion() < 16) {
            BarStyle.newInstance(this.name());
        } else {
            char[] chars = new char[] {'a', 'b', 'c', 'd', 'e'};
            BarStyle.newInstance(String.valueOf(chars[ArrayUtils.index(values(), this)]));
        }
        return BarStyle;
    }

}
