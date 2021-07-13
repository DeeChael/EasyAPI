package org.ezapi.utils;

public final class ArrayUtils {

    public static <T> boolean contains(T[] array, T item) {
        for (T t : array) {
            if (t.equals(item)) {
                return true;
            }
        }
        return false;
    }

}
