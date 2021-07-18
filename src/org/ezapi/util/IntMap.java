package org.ezapi.util;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public final class IntMap<K,V> {

    private final List<Entry<K,V>> elements = new ArrayList<>();

    public int size() {
        if (elements.isEmpty()) return 0;
        return elements.size();
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public V remove(K key) {
        if (this.containsKey(key)) {
            Entry<K,V> entry = getEntry(key);
            this.elements.remove(entry);
            return entry.getValue();
        }
        return null;
    }

    public boolean containsKey(K key) {
        return getValue(key) != null;
    }

    public boolean containsValue(V value) {
        return getKey(value) != null;
    }

    public K getKey(V value) {
        for (Entry<K,V> entry : elements) {
            if (entry.getValue().equals(value)) return entry.getKey();
        }
        return null;
    }

    public K getKey(V value, K defaultKey) {
        K result = getKey(value);
        return result != null ? result : defaultKey;
    }

    public V getValue(K key) {
        for (Entry<K,V> entry : elements) {
            if (entry.getKey().equals(key)) return entry.getValue();
        }
        return null;
    }

    public V getValue(K key, V defaultValue) {
        V result = getValue(key);
        return result != null ? result : defaultValue;
    }

    public K getKey(int position) {
        if (elements.size() == 0) return null;
        return elements.get(position).getKey();
    }

    public K getKey(int position, K defaultKey) {
        K result = getKey(position);
        return result != null ? result : defaultKey;
    }

    public V getValue(int position) {
        if (elements.size() == 0) return null;
        return elements.get(position).getValue();
    }

    public V getValue(int position, V defaultValue) {
        V result = getValue(position);
        return result != null ? result : defaultValue;
    }

    public void put(K key, V value) {
        if (this.containsKey(key)) {
            this.getEntry(key).setValue(value);
        } else {
            this.elements.add(new SimpleEntry<>(key, value));
        }
    }

    public void put(int position, K key, V value) {
        if (this.containsKey(key)) {
            this.remove(key);
        }
        if (position > this.size()) position = this.size();
        this.elements.set(position, new SimpleEntry<>(key, value));
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    public void putAll(IntMap<? extends K, ? extends V> intMap) {
        for (Entry<? extends K, ? extends V> entry : intMap.elements) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    public void clear() {
        this.elements.clear();
    }

    public List<K> keys() {
        List<K> keys = new ArrayList<>();
        for (Entry<K,V> entry : elements) {
            keys.add(entry.getKey());
        }
        return keys;
    }

    public List<V> values() {
        List<V> values = new ArrayList<>();
        for (Entry<K,V> entry : elements) {
            values.add(entry.getValue());
        }
        return values;
    }

    public List<Entry<K, V>> entrySet() {
        return elements;
    }

    private Entry<K,V> getEntry(K key) {
        for (Entry<K,V> entry : elements) {
            if (entry.getKey().equals(key)) return entry;
        }
        return null;
    }

}
