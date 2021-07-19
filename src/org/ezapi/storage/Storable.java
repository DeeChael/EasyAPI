package org.ezapi.storage;

import java.util.List;

public interface Storable {

    boolean has(String key);

    StorageContext remove(String key);

    void removeAll();

    StorageContext get(String key);

    void set(String key, StorageContext value);

    List<String> keys();

    List<StorageContext> values();

    default void setAll(Storable storable) {
        if (storable == null) return;
        for (String key : storable.keys()) {
            this.set(key, storable.get(key));
        }
    }

}
