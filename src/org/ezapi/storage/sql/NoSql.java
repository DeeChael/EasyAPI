package org.ezapi.storage.sql;

import org.ezapi.storage.StorageContext;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface NoSql extends Closable, Reloadable {

    boolean has(String key);

    List<StorageContext> remove(String key);

    void removeAll();

    List<StorageContext> get(String key);

    void set(String key, List<StorageContext> value);

    void add(String key, StorageContext value);

    void add(String key, StorageContext... value);

    void add(String key, List<StorageContext> value);

    List<String> keys();

    default List<List<StorageContext>> values() {
        List<List<StorageContext>> list = new ArrayList<>();
        for (String key : keys()) {
            list.add(get(key));
        }
        return list;
    }

    default List<StorageContext> allValues() {
        List<StorageContext> list = new ArrayList<>();
        for (List<StorageContext> collection : values()) {
            list.addAll(collection);
        }
        return list;
    }

    default void setAll(NoSql noSql) {
        if (noSql == null || noSql.closed()) return;
        for (String key : noSql.keys()) {
            this.set(key, noSql.get(key));
        }
    }

    @Override
    void close();
}
