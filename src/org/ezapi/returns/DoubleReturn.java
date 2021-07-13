package org.ezapi.returns;

public final class DoubleReturn<F,S> {

    private F first;

    private S second;

    public DoubleReturn(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public F first() {
        return first;
    }

    public S second() {
        return second;
    }

}