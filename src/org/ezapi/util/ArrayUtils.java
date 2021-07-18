package org.ezapi.util;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ArrayUtils {

    public static <T> boolean contains(T[] array, T item) {
        for (T t : array) {
            if (t.equals(item)) {
                return true;
            }
        }
        return false;
    }

    public static boolean equals(Object[] aArray, Object[] bArray) {
        if (aArray.length != bArray.length) return false;
        if (aArray == bArray) return true;
        AtomicBoolean result = new AtomicBoolean(true);
        Loop.foreach(aArray, ((integer, a) -> {
            Object b = bArray[integer];
            if (!(Objects.equals(a, b))) {
                result.set(false);
            }
        }));
        return result.get();
    }

}
