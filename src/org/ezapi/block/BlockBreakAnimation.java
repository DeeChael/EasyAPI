package org.ezapi.block;

/**
 * Block break stage, be used for PacketPlayOutBlockBreakAnimation
 */
public enum BlockBreakAnimation {

    CLEAR(-1),
    DESTROY_0(0),
    DESTROY_1(1),
    DESTROY_2(2),
    DESTROY_3(3),
    DESTROY_4(4),
    DESTROY_5(5),
    DESTROY_6(6),
    DESTROY_7(7),
    DESTROY_8(8),
    DESTROY_9(9);

    private final int stage;

    BlockBreakAnimation(int stage) {
        this.stage = stage;
    }

    /**
     * Get block break stage in nms
     * @return break stage
     */
    public int getStage() {
        return stage;
    }

    /**
     * Get block break stage by nms integer stage</br>
     * over 9 will be return DESTROY_9</br>
     * less then 0 will return CLEAR
     *
     * @param i nms break stage
     * @return break stage
     */
    public static BlockBreakAnimation valueOf(int i) {
        if (i >= 9) return DESTROY_9;
        switch (i) {
            case 0:
                return DESTROY_0;
            case 1:
                return DESTROY_1;
            case 2:
                return DESTROY_2;
            case 3:
                return DESTROY_3;
            case 4:
                return DESTROY_4;
            case 5:
                return DESTROY_5;
            case 6:
                return DESTROY_6;
            case 7:
                return DESTROY_7;
            case 8:
                return DESTROY_8;
        }
        return CLEAR;
    }

}
