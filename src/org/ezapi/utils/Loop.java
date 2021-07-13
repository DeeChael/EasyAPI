package org.ezapi.utils;

import org.ezapi.function.NonReturnWithOne;
import org.ezapi.function.NonReturnWithTwo;

public final class Loop {

    public static void range(int times, NonReturnWithOne<Integer> nonReturnWithOne) {
        for (int i = 0; i < times; i++) {
            nonReturnWithOne.apply(i);
        }
    }

    public static <T> void foreach(T[] array, NonReturnWithTwo<Integer,T> nonReturnWithOne) {
        for (int i = 0; i < array.length; i++) {
            nonReturnWithOne.apply(i, array[i]);
        }
    }

}
