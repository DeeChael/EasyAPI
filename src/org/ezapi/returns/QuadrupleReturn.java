package org.ezapi.returns;

public final class QuadrupleReturn<F,S,T,FO> {

    private F first;

    private S second;

    private T third;

    private FO fourth;

    public QuadrupleReturn(F first, S second, T third, FO fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public void setThird(T third) {
        this.third = third;
    }

    public void setFourth(FO fourth) {
        this.fourth = fourth;
    }

    public F first() {
        return first;
    }

    public S second() {
        return second;
    }

    public T third() {
        return third;
    }

    public FO fourth() {
        return fourth;
    }

}
