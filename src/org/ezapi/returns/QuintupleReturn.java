package org.ezapi.returns;

public final class QuintupleReturn<F,S,T,FO,FI> {

    private F first;

    private S second;

    private T third;

    private FO fourth;

    private FI fifth;

    public QuintupleReturn(F first, S second, T third, FO fourth, FI fifth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
        this.fifth = fifth;
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

    public void setFifth(FI fifth) {
        this.fifth = fifth;
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

    public FI fifth() {
        return fifth;
    }

}
