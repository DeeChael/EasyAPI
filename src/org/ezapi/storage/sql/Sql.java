package org.ezapi.storage.sql;

import org.ezapi.storage.StorageContext;

import java.sql.SQLException;
import java.util.List;

public interface Sql {

    void reload();

    boolean has(String key);

    StorageContext remove(String key);

    void removeAll();

    StorageContext get(String key);

    void set(String key, StorageContext value);

    List<String> keys();

    List<StorageContext> values();

    default void setAll(Sql sql) {
        if (sql == null || sql.closed()) return;
        for (String key : sql.keys()) {
            this.set(key, sql.get(key));
        }
    }

    void close() throws SQLException;

    boolean closed();

}
