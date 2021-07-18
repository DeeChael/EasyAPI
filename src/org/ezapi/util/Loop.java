package org.ezapi.util;

import org.ezapi.function.NonReturnWithOne;
import org.ezapi.function.NonReturnWithTwo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public static <T> void foreach(Collection<T> collection, NonReturnWithTwo<Integer,T> nonReturnWithTwo) {
        List<T> list = new ArrayList<>(collection);
        for (int i = 0; i < collection.size(); i++) {
            nonReturnWithTwo.apply(i, list.get(i));
        }
    }

}
