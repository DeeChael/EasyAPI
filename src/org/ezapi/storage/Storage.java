package org.ezapi.storage;

import java.util.List;

public interface Storage {

    boolean has(String key);

    String remove(String key);

    void removeAll();

    String get(String key);

    void set(String key, String value);

    List<String> keys();

    List<String> values();

    default void setAll(Storage storage) {
        if (storage == null) return;
        for (String key : storage.keys()) {
            this.set(key, storage.get(key));
        }
    }

}
