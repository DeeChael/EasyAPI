package org.ezapi.util;

import java.io.Serializable;
import java.util.*;

public final class MultiMap<K,V> implements Serializable, Cloneable {

    private final HashMap<K, List<V>> map = new HashMap<>();

    public MultiMap() {
    }

    public int size() {
        if (this.isEmpty()) return 0;
        return this.map.size();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public boolean containsKey(K key) {
        if (this.isEmpty()) return false;
        return this.map.containsKey(key);
    }

    public boolean containsValue(V value) {
        if (this.isEmpty()) return false;
        for (List<V> list : this.values()) {
            return list.contains(value);
        }
        return false;
    }

    public List<V> get(K key) {
        if (this.containsKey(key)) return this.map.get(key);
        return new ArrayList<>();
    }

    public List<V> remove(K key) {
        if (this.containsKey(key)) return this.map.remove(key);
        return new ArrayList<>();
    }

    public void putAll(MultiMap<K, V> m) {
        if (m.isEmpty()) return;
        for (K key : m.keySet()) {
            this.add(key, m.get(key));
        }
    }

    public void clear() {
        if (this.isEmpty()) return;
        this.map.clear();
    }

    public Set<K> keySet() {
        if (this.isEmpty()) return new HashSet<>();
        return this.map.keySet();
    }

    public void add(K key, V value) {
        if (!this.containsKey(key)) this.map.put(key, new ArrayList<>());
        this.map.get(key).add(value);
    }

    public void add(K key, Collection<V> values) {
        for (V value : values) {
            this.add(key, value);
        }
    }

    public void add(K key, List<V> values) {
        for (V value : values) {
            this.add(key, value);
        }
    }

    @SafeVarargs
    public final void add(K key, V... values) {
        for (V value : values) {
            this.add(key, value);
        }
    }

    public Collection<List<V>> values() {
        return this.map.values();
    }

    public Collection<V> valuesAll() {
        Collection<V> vs = new HashSet<>();
        for (List<V> collection : this.values()) {
            vs.addAll(collection);
        }
        return vs;
    }

}
