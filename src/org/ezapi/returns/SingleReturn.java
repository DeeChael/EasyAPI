package org.ezapi.returns;

public final class SingleReturn<V> {

    private V value;

    public SingleReturn(V value) {
        this.value = value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public V first() {
        return value;
    }

}
