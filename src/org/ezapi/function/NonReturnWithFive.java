package org.ezapi.function;

@FunctionalInterface
public interface NonReturnWithFive<A,B,C,D,E> {

    void apply(A a, B b, C c, D d, E e);

}
