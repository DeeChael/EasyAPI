package org.ezapi.function;

public interface NonReturnWithFour<T,V,E,C> {

    void apply(T t, V v, E e, C c);

}