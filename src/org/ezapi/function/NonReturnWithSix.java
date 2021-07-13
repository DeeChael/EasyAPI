package org.ezapi.function;

@FunctionalInterface
public interface NonReturnWithSix<A,B,C,D,E,F> {

    void apply(A a, B b, C c, D d, E e, F f);

}
