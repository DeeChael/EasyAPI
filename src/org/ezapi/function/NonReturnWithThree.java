package org.ezapi.function;

@FunctionalInterface
public interface NonReturnWithThree<T,V,E> {

    void apply(T t, V v, E e);

}