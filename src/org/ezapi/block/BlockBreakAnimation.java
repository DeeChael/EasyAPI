package org.ezapi.block;

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

    public int getStage() {
        return stage;
    }

}
