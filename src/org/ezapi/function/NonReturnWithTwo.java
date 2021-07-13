package org.ezapi.function;

@FunctionalInterface
public interface NonReturnWithTwo<T,V> {

    void apply(T t, V v);

}